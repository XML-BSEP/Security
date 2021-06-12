package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.*;
import com.example.DukeStrategicTechnologies.pki.mapper.UserMapper;
import com.example.DukeStrategicTechnologies.pki.model.Account;
import com.example.DukeStrategicTechnologies.pki.model.Authority;
import com.example.DukeStrategicTechnologies.pki.model.User;
import com.example.DukeStrategicTechnologies.pki.repository.AccountRepository;
import com.example.DukeStrategicTechnologies.pki.repository.UserRepository;
import com.google.gson.Gson;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
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

    public UserService(){

    }

    public void register(NewUserDTO userDTO) throws Exception{
        User userExist = userRepository.findByEmail(userDTO.getEmail());
        if (userExist != null) {
            throw new Exception(USER_ALREADY_EXIST);
        }
        User newUser = new User(userDTO.getName(), userDTO.getSurname(), userDTO.getCommonName(), userDTO.getOrganization(), userDTO.getOrganizationUnit(),
                userDTO.getState(), userDTO.getCity(), userDTO.getEmail(), false, 0L);

        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority(2L, "ROLE_END_ENTITY_USER"));

        if(!userDTO.getPassword().equals(userDTO.getConfirmPassword())){
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
        String redisKey = PKI_REDIS+dto.getEmail();

        JedisPool jedisPool = new JedisPool("localhost", 6379);
        RedisDTO redis;
        try (Jedis jedis = jedisPool.getResource()) {
            redis = new Gson().fromJson(jedis.get(redisKey), RedisDTO.class);
            if (redis == null){
                throw new Exception(USER_DOES_NOT_EXIST);
            }
        }
        if (dto.getCode().equals(redis.getCode())) {

            Account newAccount = new Account(redis.getAccount().getEmail(), passwordEncoder.encode(redis.getAccount().getPassword()));
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
            throw new Exception(WRONG_CODE);
        }

    }
    public void resendCode(ResendCodeDTO dto) throws Exception{
        String redisKey = PKI_REDIS+dto.getEmail();

        JedisPool jedisPool = new JedisPool("localhost", 6379);
        RedisDTO redis;
        try (Jedis jedis = jedisPool.getResource()) {
            redis = new Gson().fromJson(jedis.get(redisKey), RedisDTO.class);
            if (redis == null){
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
        User userExist = userRepository.findByEmail(userDTO.getEmail());
        if (userExist != null) {
            throw new Exception(USER_ALREADY_EXIST);
        }

        String commonName = "localhost"; //userDTO.getGivenName() + (userRepository.findAll().size() + 1);

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
