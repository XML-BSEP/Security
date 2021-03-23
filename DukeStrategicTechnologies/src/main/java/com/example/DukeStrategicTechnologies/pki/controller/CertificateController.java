package com.example.DukeStrategicTechnologies.pki.controller;

import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/createCertificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;
    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody CreateCertificateDTO dto) throws Exception {
        certificateService.createCertificate(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Exception handleException(Exception e) {
        return new Exception(e);
    }
}
