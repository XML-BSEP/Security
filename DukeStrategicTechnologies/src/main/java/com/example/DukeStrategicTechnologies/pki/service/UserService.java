package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.TemplateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.UserDTO;
import com.example.DukeStrategicTechnologies.pki.mapper.TemplateMapper;
import com.example.DukeStrategicTechnologies.pki.mapper.UserMapper;
import com.example.DukeStrategicTechnologies.pki.model.Account;
import com.example.DukeStrategicTechnologies.pki.model.Authority;
import com.example.DukeStrategicTechnologies.pki.model.Template;
import com.example.DukeStrategicTechnologies.pki.model.User;
import com.example.DukeStrategicTechnologies.pki.repository.AccountRepository;
import com.example.DukeStrategicTechnologies.pki.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public static final String USER_ALREADY_EXIST = "Email already exists!";
    public UserService(){

    }

    public void saveUser(UserDTO userDTO) throws Exception {
        User userExist = userRepository.findByEmail(userDTO.getEmail());
        if (userExist != null) {
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
