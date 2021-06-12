package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.*;
import com.example.DukeStrategicTechnologies.pki.helpers.Validator;
import com.example.DukeStrategicTechnologies.pki.helpers.validatorErrors.StringInputException;
import com.example.DukeStrategicTechnologies.pki.mapper.UserMapper;
import com.example.DukeStrategicTechnologies.pki.model.Account;
import com.example.DukeStrategicTechnologies.pki.model.Authority;
import com.example.DukeStrategicTechnologies.pki.model.User;
import com.example.DukeStrategicTechnologies.pki.repository.AccountRepository;
import com.example.DukeStrategicTechnologies.pki.repository.UserRepository;
import com.google.gson.Gson;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;



    public static final String USER_ALREADY_EXIST = "Email already exists!";
    public static final String WRONG_PASSWORD = "Passwords do not match!";
    public static final String WRONG_CODE = "Codes do not match!";

    public static final String USER_DOES_NOT_EXIST = "User doesnt exist in db!";

    public static final String PKI_REDIS = "registrationRequest";
    public static final String PKI_PASSWORD_REDIS = "passwordResetRequest";

    public UserService(){

    }

    public void register(NewUserDTO userDTO) throws Exception{
        User userExist = userRepository.findByEmail(userDTO.getEmail());
        if (userExist != null) {
            LOGGER.error("failed registration " + USER_ALREADY_EXIST);
            throw new Exception(USER_ALREADY_EXIST);
        }

        Validator.validateRegistrationForm(userDTO);

        User newUser = new User(userDTO.getName(), userDTO.getSurname(), userDTO.getCommonName(), userDTO.getOrganization(), userDTO.getOrganizationUnit(),
                userDTO.getState(), userDTO.getCity(), userDTO.getEmail(), false, 0L);

        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority(2L, "ROLE_END_ENTITY_USER"));

        if(!userDTO.getPassword().equals(userDTO.getConfirmPassword())){
            LOGGER.error("failed registration " + WRONG_PASSWORD);
            throw new Exception(WRONG_PASSWORD);
        }

        Account newAccount = new Account(userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()));

        newAccount.setRole("User");
        newAccount.setAuthorities(authorities);

        JedisPool jedisPool = new JedisPool("localhost", 6379);
        String redisKey = PKI_REDIS+userDTO.getEmail();
        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = false;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);


        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(redisKey, new Gson().toJson(new RedisDTO(newAccount, newUser, generatedString)));
            System.out.println(jedis.get(redisKey));
        }
//        EmailService em = new EmailService();
//        esendConfirmationEmail(emailService.generateEmailInfo(new RedisDTO(newAccount, newUser, generatedString)));
        emailService.sendConfirmationEmail(EmailService.generateEmailInfo(new RedisDTO(newAccount, newUser, generatedString)));
//
//        accountRepository.save(newAccount);
//        userRepository.save(newUser);

    }

    public void activateAccount(ActivationDTO dto) throws Exception{

        Validator.validateActivationForm(dto);


        String redisKey = PKI_REDIS+dto.getEmail();

        JedisPool jedisPool = new JedisPool("localhost", 6379);
        RedisDTO redis;
        try (Jedis jedis = jedisPool.getResource()) {
            redis = new Gson().fromJson(jedis.get(redisKey), RedisDTO.class);
            if (redis == null){
                LOGGER.error("failed to activate account " + USER_DOES_NOT_EXIST);
                throw new Exception(USER_DOES_NOT_EXIST);
            }
        }
        if (dto.getCode().equals(redis.getCode())) {

            Account newAccount = new Account(redis.getAccount().getEmail(), redis.getAccount().getPassword());
            User newUser = new User(redis.getUser().getGivenName(), redis.getUser().getSurname(), redis.getUser().getCommonName(), redis.getUser().getOrganization(), redis.getUser().getOrganizationUnit(),
                    redis.getUser().getState(), redis.getUser().getCity(), redis.getUser().getEmail(), false, 0L);

            List<Authority> authorities = new ArrayList<>();
            authorities.add(new Authority(2L, "ROLE_END_ENTITY_USER"));
            newAccount.setRole("User");
            newAccount.setAuthorities(authorities);

            accountRepository.save(newAccount);
            userRepository.save(newUser);

            Jedis jedis = jedisPool.getResource();
            jedis.del(redisKey);
        }else{
            LOGGER.error("failed to activate account " + WRONG_CODE);
            throw new Exception(WRONG_CODE);
        }

    }
    public void sendPasswordResetCode(ResendCodeDTO dto) throws Exception{
        Validator.validateResendCodeForm(dto);

        String redisKey = PKI_PASSWORD_REDIS+dto.getEmail();

        JedisPool jedisPool = new JedisPool("localhost", 6379);
        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = false;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);


        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(redisKey,generatedString);
            System.out.println(jedis.get(redisKey));
        }

        emailService.sendPasswordResetEmail(EmailService.generateEmailInfo(dto,generatedString));

    }
    public void resetPassword(PasswordResetDTO dto) throws Exception{
        Validator.validatePasswordResetForm(dto);

        String redisKey = PKI_PASSWORD_REDIS+dto.getEmail();
        String code;
        JedisPool jedisPool = new JedisPool("localhost", 6379);
        try (Jedis jedis = jedisPool.getResource()) {
            code = jedis.get(redisKey);
            if (code == null){
                LOGGER.error("failed to reset password " + USER_DOES_NOT_EXIST);
                throw new Exception(USER_DOES_NOT_EXIST);
            }
        }
        if(code.equals(dto.getCode())){
            if(dto.getPassword().equals(dto.getConfirmedpassword())){
                Account a = accountRepository.findByEmail(dto.getEmail());
                if(a!=null){
                    a.setPassword(passwordEncoder.encode(dto.getPassword()));
                    accountRepository.save(a);
                    try(Jedis jedis = jedisPool.getResource()){
                        jedis.del(redisKey);
                    }

                }
            }else{
                LOGGER.error("failed to reset password " + WRONG_PASSWORD);
                throw new Exception(WRONG_PASSWORD);
            }
        }else{
            LOGGER.error("failed to reset password " + WRONG_CODE);
            throw new Exception(WRONG_CODE);

        }


//        emailService.sendPasswordResetEmail(EmailService.generateEmailInfo(dto,generatedString));

    }


    public void resendCode(ResendCodeDTO dto) throws Exception, StringInputException {
        Validator.validateResendCodeForm(dto);

        String redisKey = PKI_REDIS+dto.getEmail();

        JedisPool jedisPool = new JedisPool("localhost", 6379);
        RedisDTO redis;
        try (Jedis jedis = jedisPool.getResource()) {
            redis = new Gson().fromJson(jedis.get(redisKey), RedisDTO.class);
            if (redis == null){
                LOGGER.error("failed to resend code " + USER_DOES_NOT_EXIST);
                throw new Exception(USER_DOES_NOT_EXIST);
            }
        }

        try(Jedis jedis = jedisPool.getResource()){
            jedis.del(redisKey);
        }

        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = false;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);

        redis.setCode(generatedString);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(redisKey, new Gson().toJson(redis));
//            System.out.println(jedis.get(redisKey));
            emailService.sendConfirmationEmail(EmailService.generateEmailInfo(redis));

        }
    }

    public void saveUser(UserDTO userDTO) throws Exception {
        Validator.validateSaveUserForm(userDTO);
        User userExist = userRepository.findByEmail(userDTO.getEmail());
        if (userExist != null) {
            LOGGER.error("failed to save user " + USER_ALREADY_EXIST);
            throw new Exception(USER_ALREADY_EXIST);
        }

        String commonName = userDTO.getGivenName() + (userRepository.findAll().size() + 1);

        User newUser = new User(userDTO.getGivenName(), userDTO.getSurname(), commonName, userDTO.getOrganization(), userDTO.getOrganizationUnit(),
                userDTO.getState(), userDTO.getCity(), userDTO.getEmail(), false, 0L);

        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority(2L, "ROLE_END_ENTITY_USER"));

        Account newAccount = new Account(userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()));
        newAccount.setRole("User");
        newAccount.setAuthorities(authorities);
        accountRepository.save(newAccount);
        userRepository.save(newUser);
    }

    public List<UserDTO> getAllUsers() {

        List<User> users = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();

        for (User u : users) {
            dtos.add(UserMapper.userToDTO(u));
        }
        return dtos;
    }
}
