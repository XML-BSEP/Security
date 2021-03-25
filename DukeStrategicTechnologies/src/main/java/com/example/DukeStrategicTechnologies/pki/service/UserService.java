package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.TemplateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.UserDTO;
import com.example.DukeStrategicTechnologies.pki.mapper.TemplateMapper;
import com.example.DukeStrategicTechnologies.pki.mapper.UserMapper;
import com.example.DukeStrategicTechnologies.pki.model.Template;
import com.example.DukeStrategicTechnologies.pki.model.User;
import com.example.DukeStrategicTechnologies.pki.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserService(){
        userMapper = new UserMapper();

    }

    public List<UserDTO> getAllUsers() {

        List<User> users = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();

        for (User u : users) {
            dtos.add(userMapper.userToDTO(u));
        }
        return dtos;
    }
}
