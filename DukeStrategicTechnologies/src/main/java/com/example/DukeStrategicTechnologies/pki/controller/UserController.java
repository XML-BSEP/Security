package com.example.DukeStrategicTechnologies.pki.controller;

import com.example.DukeStrategicTechnologies.pki.dto.TemplateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.UserDTO;
import com.example.DukeStrategicTechnologies.pki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/users",produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
//    @PreAuthorize("hasRole('ADMIN') || hasRole('CA')")
    public ResponseEntity<?> getAll() throws Exception{
        List<UserDTO> dtos = userService.getAllUsers();
        return  new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping(value="/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws Exception {
        userService.saveUser(userDTO);
        return  new ResponseEntity<>(HttpStatus.OK);

    }
}
