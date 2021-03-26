package com.example.DukeStrategicTechnologies.pki.controller;

import com.example.DukeStrategicTechnologies.pki.dto.CertificateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.DownloadCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.PossibleKeyUsagesDTO;
import com.example.DukeStrategicTechnologies.pki.model.Account;
import com.example.DukeStrategicTechnologies.pki.service.Base64Encoder;
import com.example.DukeStrategicTechnologies.pki.service.CertificateService;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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


@Controller
@CrossOrigin
@RequestMapping("certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private Base64Encoder base64Encoder;


    //ADD OTHER ROLES
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

    @GetMapping("/getAllForSigning")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateDTO>> getAllForSigning() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificateAlias = certificateService.getAllCertificatesForSigning();
        return new ResponseEntity<>(certificateAlias, HttpStatus.OK);
    }

    @PostMapping("/downloadCertificate")
    public ResponseEntity<?> downloadCertificate(@RequestBody DownloadCertificateDTO dto) throws Exception {
        base64Encoder.downloadCertificate(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/revokeCertificate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> revokeCertificate(@RequestBody String serialNumber) throws Exception {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail = ((Account)user).getUsername();
        certificateService.revokeCertificate(serialNumber, mail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO: realizovati i metodu koja vraca samo sertifikate sa kojima sme da potpise druge sertifikate (obican user)
    /*
    * insert code here
    * */

    //ADD ROLES
    @GetMapping("/getCertificatesByUser")
    public ResponseEntity<?> getCertificatesByUser() throws Exception{
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail = ((Account)user).getUsername();
        List<CertificateDTO> certificatesByUser = certificateService.getAllCertificatesByUser(mail);
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }

    @GetMapping("/getRootCertificates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSelfSignedCertificates() throws Exception{
        List<CertificateDTO> certificatesByUser = certificateService.getRootCertificates();
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }

    @GetMapping("/getPossibleKeyUsages")
    public ResponseEntity<?> getPossibleKeyUsages(@RequestParam("alias") String alias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        return new ResponseEntity<>(certificateService.getPossibleKeyUsages(alias), HttpStatus.OK);
    }

    @GetMapping("/getCaCertificatesByUser")
    public ResponseEntity<?> getCaCertificatesByUser() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail = ((Account)user).getUsername();
        List<CertificateDTO> certificateDTOS = certificateService.getAllCaCertificatesByUser(mail);

        return new ResponseEntity<>(certificateDTOS, HttpStatus.OK);
    }

    @GetMapping("/getEndEntityCertificatesByUser")
    public ResponseEntity<?> getEndEntityCertificatesByUser() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail = ((Account)user).getUsername();
        List<CertificateDTO> certificateDTOS = certificateService.getAllEndEntityCertificatesByUser(mail);

        return new ResponseEntity<>(certificateDTOS, HttpStatus.OK);
    }

    //ADD OTHER ROLES
    @GetMapping("/getCaCertificates")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCaCertificates() throws Exception{
        List<CertificateDTO> certificatesByUser = certificateService.getCaCertificates();
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }


    //ADD OTHER ROLES
    @GetMapping("/getEndEntityCertificates")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEndEntityCertificates() throws Exception{
        List<CertificateDTO> certificatesByUser = certificateService.getEndEntityCertificates();
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }

    @GetMapping("/getAllForSigningByUser")

    public ResponseEntity<List<CertificateDTO>> getAllForSigningByUser() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail = ((Account)user).getUsername();
        List<CertificateDTO> certificateAlias = certificateService.getAllCertificatesForSigningByUser(mail);
        return new ResponseEntity<>(certificateAlias, HttpStatus.OK);
    }

    @PostMapping("/createRootCertificate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createRootCertificate(@RequestBody CreateCertificateDTO dto) throws Exception {
        certificateService.createRootCertificate(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Exception handleException(Exception e) {
        return new Exception(e);
    }
}
