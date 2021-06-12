package com.example.DukeStrategicTechnologies.pki.controller;

import com.example.DukeStrategicTechnologies.pki.dto.TemplateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.UserTemplateDTO;
import com.example.DukeStrategicTechnologies.pki.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/template",produces = MediaType.APPLICATION_JSON_VALUE)
public class TemplateController {

    private final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);
    @Autowired
    private TemplateService templateService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_END_ENTITY_USER') || hasAuthority('ROLE_CA')")
    public ResponseEntity<?> createTemplate(@RequestBody TemplateDTO templateDTO) throws Exception {
        LOGGER.info("Handling ADDING TEMPLATE");
        templateService.createTemplate(templateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_END_ENTITY_USER') || hasAuthority('ROLE_CA')")
    public ResponseEntity<?> getAll() throws Exception{
        LOGGER.info("Handling GETTING ALL TEMPLATES");
        List<TemplateDTO> templateDTOS = templateService.getAllTemplates();
        return  new ResponseEntity<>(templateDTOS, HttpStatus.OK);
    }

    @GetMapping("/allByUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_END_ENTITY_USER') || hasAuthority('ROLE_CA')")
    public ResponseEntity<?> getAllByUser(@RequestParam Long userId) throws Exception{
        LOGGER.info(String.format("Handling GETTING ALL TEMPLATES BY USER ID %s", userId));
        List<TemplateDTO> templateDTOS = templateService.getAllTemplatesByUser(userId);
        return  new ResponseEntity<>(templateDTOS, HttpStatus.OK);
    }
}
