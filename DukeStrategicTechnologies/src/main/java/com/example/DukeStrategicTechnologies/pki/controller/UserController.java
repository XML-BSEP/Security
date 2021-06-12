package com.example.DukeStrategicTechnologies.pki.controller;

import com.example.DukeStrategicTechnologies.pki.dto.*;
import com.example.DukeStrategicTechnologies.pki.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/users",produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_END_ENTITY_USER') || hasAuthority('ROLE_CA')")
    public ResponseEntity<?> getAll() throws Exception{
        LOGGER.info("Handling GETTING ALL USERS");
        List<UserDTO> dtos = userService.getAllUsers();
        return  new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping(value="/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USER')")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws Exception {
        LOGGER.info("Handling ADDING USER");

        userService.saveUser(userDTO);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping(value="/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody NewUserDTO userDTO) throws Exception {
        LOGGER.info("Handling REGISTER USERS");

        userService.register(userDTO);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping(value="/confirmAccount", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> activate(@RequestBody ActivationDTO dto) throws Exception {
        LOGGER.info("Handling CONFIRM ACCOUNT");

        userService.activateAccount(dto);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/resendRegistrationCode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resend(@RequestBody ResendCodeDTO dto) throws Exception {
        LOGGER.info("Handling RESEND REGISTRATION CODE");

        userService.resendCode(dto);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value= "/resetPasswordMail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendResetPasswordCode(@RequestBody ResendCodeDTO dto) throws Exception {
        LOGGER.info("Handling RESET PASSWORD MAIL");

        userService.sendPasswordResetCode(dto);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value= "/resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ResetPassword(@RequestBody PasswordResetDTO dto) throws Exception {
        LOGGER.info("Handling RESET PASSWORD");

        userService.resetPassword(dto);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}
