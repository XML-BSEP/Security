package com.example.DukeStrategicTechnologies.pki.controller;

import com.example.DukeStrategicTechnologies.pki.dto.CertificateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.DownloadCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.model.Account;
import com.example.DukeStrategicTechnologies.pki.service.Base64Encoder;
import com.example.DukeStrategicTechnologies.pki.service.CertificateService;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

@RestController
@RequestMapping(value = "/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private Base64Encoder base64Encoder;

    @PostMapping("/createCertificate")
    @PreAuthorize("hasRole('ADMIN') || hasRole('CA')")
    public ResponseEntity<?> createCertificate(@RequestBody CreateCertificateDTO dto) throws Exception {
        certificateService.createCertificate(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateDTO>> getAll() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificateAlias = certificateService.getAllCertificates();
        return new ResponseEntity<>(certificateAlias, HttpStatus.OK);
    }

    @PostMapping("/downloadCertificate")
    public ResponseEntity<?> downloadCertificate(@RequestBody DownloadCertificateDTO dto) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException,
            KeyStoreException, CMSException, OperatorCreationException, NoSuchProviderException, IOException {
        base64Encoder.downloadCertificate(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/revokeCertificate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> revokeCertificate(@RequestBody String serialNumber) {
        certificateService.revokeCertificate(serialNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getCertificatesByUser")
    public ResponseEntity<?> getCertificatesByUser() throws Exception{
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail = ((Account)user).getUsername();
        List<CertificateDTO> certificatesByUser = certificateService.getAllCertificatesByUser(mail);
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }

    @GetMapping(value = "/getRootCertificates")
   // @PreAuthorize("hasRole('ADMIN')")
    //@CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<?> getSelfSignedCertificates() throws Exception{
        List<CertificateDTO> certificatesByUser = certificateService.getRootCertificates();
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }

    @GetMapping("/getCaCertificates")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCaCertificates() throws Exception{
        List<CertificateDTO> certificatesByUser = certificateService.getCaCertificates();
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }

    @GetMapping("/getEndEntityCertificates")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEndEntityCertificates() throws Exception{
        List<CertificateDTO> certificatesByUser = certificateService.getEndEntityCertificates();
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Exception handleException(Exception e) {
        return new Exception(e);
    }
}
