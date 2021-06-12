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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;


@Controller
@CrossOrigin
@RequestMapping("certificate")
public class CertificateController {

    private final Logger LOGGER = LoggerFactory.getLogger(CertificateController.class);
    @Autowired
    private CertificateService certificateService;

    @Autowired
    private Base64Encoder base64Encoder;


    //ADD OTHER ROLES
    @PostMapping("/createCertificate")
    @PreAuthorize("hasRole('ADMIN') || hasRole('CA')")
    public ResponseEntity<?> createCertificate(@RequestBody CreateCertificateDTO dto) throws Exception {
        LOGGER.info("Handling CREATING CERTIFICATE");

        certificateService.createCertificate(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateDTO>> getAll() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        LOGGER.info("Handling GETTING ALL CERTIFICATES");

        List<CertificateDTO> certificateAlias = certificateService.getAllCertificates();
        return new ResponseEntity<>(certificateAlias, HttpStatus.OK);
    }

    @GetMapping("/getAllForSigning")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateDTO>> getAllForSigning() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        LOGGER.info("Handling GETTING ALL CERTIFICATES FOR SIGNING");

        List<CertificateDTO> certificateAlias = new ArrayList<>();
        try{
            certificateAlias = certificateService.getAllCertificatesForSigning();
        }catch(Exception e){
            LOGGER.error("Failed to getting all certificates for signing " +  e.getMessage());
            e.printStackTrace();
        }

        return new ResponseEntity<>(certificateAlias, HttpStatus.OK);
    }

    @PostMapping("/downloadCertificate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_END_ENTITY_USER') || hasAuthority('ROLE_CA')")
    public ResponseEntity<?> downloadCertificate(@RequestBody DownloadCertificateDTO dto) throws Exception {
        LOGGER.info("Handling DOWNLOADING CERTIFICATE");

        base64Encoder.downloadCertificate(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/revokeCertificate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> revokeCertificate(@RequestBody String serialNumber) throws Exception {
        LOGGER.info("Handling REVOKING CERTIFICATE");

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
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_END_ENTITY_USER') || hasAuthority('ROLE_CA')")
    public ResponseEntity<?> getCertificatesByUser() throws Exception{

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info(String.format("Handling GETTING ALL CERTIFICATES FOR SIGNING BY USER, USERNAME %s", ((Account)user).getUsername()));
        String mail = ((Account)user).getUsername();
        List<CertificateDTO> certificatesByUser = certificateService.getAllCertificatesByUser(mail);
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }

    @GetMapping("/getRootCertificates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSelfSignedCertificates() throws Exception{
        LOGGER.info("Handling GETTING ROOT CERTIFICATES");

        List<CertificateDTO> certificatesByUser = certificateService.getRootCertificates();
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }

    @GetMapping("/getPossibleKeyUsages")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_END_ENTITY_USER') || hasAuthority('ROLE_CA')")
    public ResponseEntity<?> getPossibleKeyUsages(@RequestParam("alias") String alias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        LOGGER.info(String.format("Handling GETTING POSSIBLE KEY USAGES, ALIAS %s", alias));

        return new ResponseEntity<>(certificateService.getPossibleKeyUsages(alias), HttpStatus.OK);
    }

    @GetMapping("/getCaCertificatesByUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_CA')")
    public ResponseEntity<?> getCaCertificatesByUser() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {


        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info(String.format("Handling GETTING ALL CERTIFICATES FOR SIGNING BY USER, USERNAME %s", ((Account)user).getUsername()));
        String mail = ((Account)user).getUsername();
        List<CertificateDTO> certificateDTOS = certificateService.getAllCaCertificatesByUser(mail);

        return new ResponseEntity<>(certificateDTOS, HttpStatus.OK);
    }

    @GetMapping("/getEndEntityCertificatesByUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_END_ENTITY_USER') || hasAuthority('ROLE_CA')")
    public ResponseEntity<?> getEndEntityCertificatesByUser() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {


        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info(String.format("Handling GETTING ALL CERTIFICATES FOR SIGNING BY USER, USERNAME %s", ((Account)user).getUsername()));
        String mail = ((Account)user).getUsername();
        List<CertificateDTO> certificateDTOS = certificateService.getAllEndEntityCertificatesByUser(mail);

        return new ResponseEntity<>(certificateDTOS, HttpStatus.OK);
    }

    //ADD OTHER ROLES
    @GetMapping("/getCaCertificates")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_CA')")
    public ResponseEntity<?> getCaCertificates() throws Exception{
        LOGGER.info("Handling GETTING ALL CA CERTIFICATES ");

        List<CertificateDTO> certificatesByUser = certificateService.getCaCertificates();
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }


    //ADD OTHER ROLES
    @GetMapping("/getEndEntityCertificates")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_END_ENTITY_USER') || hasAuthority('ROLE_CA')")
    public ResponseEntity<?> getEndEntityCertificates() throws Exception{
        LOGGER.info("Handling GETTING ALL END ENTITY CERTIFICATES");

        List<CertificateDTO> certificatesByUser = certificateService.getEndEntityCertificates();
        return new ResponseEntity<>(certificatesByUser, HttpStatus.OK);
    }

    @GetMapping("/getAllForSigningByUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_END_ENTITY_USER') || hasAuthority('ROLE_CA')")
    public ResponseEntity<List<CertificateDTO>> getAllForSigningByUser() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String mail = ((Account)user).getUsername();
        LOGGER.info(String.format("Handling GETTING ALL CERTIFICATES FOR SIGNING BY USER, USERNAME %s", ((Account)user).getUsername()));

        List<CertificateDTO> certificateAlias = certificateService.getAllCertificatesForSigningByUser(mail);
        return new ResponseEntity<>(certificateAlias, HttpStatus.OK);
    }

    @PostMapping("/createRootCertificate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createRootCertificate(@RequestBody CreateCertificateDTO dto) throws Exception {
        LOGGER.info("Handling CREATING ROOT CERTIFICATE");
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
