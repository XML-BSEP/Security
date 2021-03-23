package com.example.DukeStrategicTechnologies.pki.controller;

import com.example.DukeStrategicTechnologies.pki.dto.CertificateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.service.Base64Encoder;
import com.example.DukeStrategicTechnologies.pki.service.CertificateService;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

@Controller
@RequestMapping("api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private Base64Encoder base64Encoder;

    @PostMapping("/createCertificate")
    public ResponseEntity<?> createCertificate(@RequestBody CreateCertificateDTO dto) throws Exception {
        certificateService.createCertificate(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<CertificateDTO>> getAll() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificateAlias = certificateService.getAllCertificates();
        return new ResponseEntity<>(certificateAlias, HttpStatus.OK);
    }

    @PostMapping("/downloadCertificate")
    public ResponseEntity<?> downloadCertificate(@RequestBody CertificateDTO dto) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException,
            KeyStoreException, CMSException, OperatorCreationException, NoSuchProviderException, IOException {
        boolean retVal = base64Encoder.downloadCertificate(dto);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Exception handleException(Exception e) {
        return new Exception(e);
    }
}
