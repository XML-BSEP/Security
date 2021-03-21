package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.keystores.KeyStoreWriter;
import com.example.DukeStrategicTechnologies.pki.model.Subject;
import com.example.DukeStrategicTechnologies.pki.model.User;
import com.example.DukeStrategicTechnologies.pki.repository.UserRepository;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.util.List;

@Service
public class CreateCertificateService {

    static
    {
        Security.addProvider(new BouncyCastleProvider());
    }

    private CertificateGenerator certificateGenerator;
    private KeyStoreWriter keyStoreWriter;

    @Autowired
    private UserRepository userRepository;

    public CreateCertificateService() {
        this.keyStoreWriter = new KeyStoreWriter();
        this.certificateGenerator = new CertificateGenerator();
    }


    public void createCertificate(CreateCertificateDTO createCertificateDTO) throws Exception {

        Subject subject = generateSubject(createCertificateDTO);

        User issuer = userRepository.getOne(createCertificateDTO.getIssuerId());
        if (issuer == null){
            throw new Exception();
        }


        String issuerAllias = issuer.getEmail() + createCertificateDTO.getIssuerSerialNumber();












    }




    private Subject generateSubject(CreateCertificateDTO dto) throws Exception {

        KeyPair keyPairSubject = generateKeyPair();

        User user = userRepository.getOne(dto.getSubjectId());
        if (user == null){
            throw new Exception();
        }

        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, user.getCommonName());
        builder.addRDN(BCStyle.SURNAME, user.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, user.getGivenName());
        builder.addRDN(BCStyle.O, user.getOrganization());
        builder.addRDN(BCStyle.OU, user.getOrganizationUnit());
        builder.addRDN(BCStyle.C, user.getState());
        builder.addRDN(BCStyle.E, user.getEmail());

        return new Subject(builder.build(), keyPairSubject);

    }


    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BigInteger generateSerialNumberForCertificate(){
        List<User> users = userRepository.findAll();
        int serialNumber = (int) users.stream().count();
        for (User u : users) { serialNumber +=  u.getCertificateCount().intValue();}
        return BigInteger.valueOf(serialNumber);

    }

    private KeyStore getSelfSignedKeyStore(){

        String filePath = "" + "."

    }






}
