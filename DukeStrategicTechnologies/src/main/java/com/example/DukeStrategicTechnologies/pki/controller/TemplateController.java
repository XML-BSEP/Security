package com.example.DukeStrategicTechnologies.pki.controller;

import com.example.DukeStrategicTechnologies.pki.dto.TemplateDTO;
import com.example.DukeStrategicTechnologies.pki.service.TemplateService;
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

    @Autowired
    private TemplateService templateService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createTemplate(@RequestBody TemplateDTO templateDTO) throws Exception {
        templateService.createTemplate(templateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/all")
//    @PreAuthorize("hasRole('ADMIN') || hasRole('CA')")
    public ResponseEntity<?> getAll() throws Exception{
        List<TemplateDTO> templateDTOS = templateService.getAllTemplates();
        return  new ResponseEntity<>(templateDTOS, HttpStatus.OK);
    }
}
