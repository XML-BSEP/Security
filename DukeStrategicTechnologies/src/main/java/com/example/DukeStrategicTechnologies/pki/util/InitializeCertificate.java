package com.example.DukeStrategicTechnologies.pki.util;

import com.example.DukeStrategicTechnologies.pki.keystores.KeyStoreWriter;
import com.example.DukeStrategicTechnologies.pki.model.ExtendedCertificateData;
import com.example.DukeStrategicTechnologies.pki.model.Issuer;
import com.example.DukeStrategicTechnologies.pki.model.Subject;
import com.example.DukeStrategicTechnologies.pki.model.User;
import com.example.DukeStrategicTechnologies.pki.model.enums.SignatureAlgorithm;
import com.example.DukeStrategicTechnologies.pki.repository.UserRepository;
import com.example.DukeStrategicTechnologies.pki.service.CertificateGenerator;
import com.example.DukeStrategicTechnologies.pki.service.CreateCertificateService;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.ArrayList;

@Component
@EnableAutoConfiguration
public class InitializeCertificate implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreateCertificateService createCertificateService;


    private CertificateGenerator certificateGenerator;


    private KeyStoreWriter keyStoreWriter;

    public InitializeCertificate() {
        this.certificateGenerator = new CertificateGenerator();
        this.keyStoreWriter = new KeyStoreWriter();
    }

    public void initialize() throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        User root = userRepository.findAll().stream().filter(user -> user.getEmail().equals("root@gmail.com")).findFirst().orElse(null);

        if(root != null) {
            return;
        }


        User newRoot = new User(2L, "root", "rootovic", "rootic", "org1", "orgunit1", "Serbia", "Novi Sad", "root@gmail.com", true, 1L);

        userRepository.save(newRoot);

        File f = new File("root.jks");
        if (f.exists()) {
            return;
        }

        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, newRoot.getCommonName());
        builder.addRDN(BCStyle.SURNAME, newRoot.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, newRoot.getGivenName());
        builder.addRDN(BCStyle.O, newRoot.getOrganization());
        builder.addRDN(BCStyle.OU, newRoot.getOrganizationUnit());
        builder.addRDN(BCStyle.C, newRoot.getState());
        builder.addRDN(BCStyle.E, newRoot.getEmail());

        KeyPair keyPair = createCertificateService.generateKeyPair();

        Issuer issuer = new Issuer(builder.build(), keyPair);
        Subject subject = new Subject(issuer.getX500Name(), issuer.getKeyPair());

        ArrayList<Integer> keyUsageValues = new ArrayList<>();
        ArrayList<KeyPurposeId> extendedKeyUsageValues = new ArrayList<>();

        keyUsageValues.add(KeyUsage.digitalSignature);
        keyUsageValues.add(KeyUsage.nonRepudiation);
        keyUsageValues.add(KeyUsage.keyEncipherment);
        keyUsageValues.add(KeyUsage.dataEncipherment);
        keyUsageValues.add(KeyUsage.keyAgreement);
        keyUsageValues.add(KeyUsage.keyCertSign);
        keyUsageValues.add(KeyUsage.cRLSign);
        keyUsageValues.add(KeyUsage.encipherOnly);
        keyUsageValues.add(KeyUsage.decipherOnly);

        extendedKeyUsageValues.add(KeyPurposeId.id_kp_serverAuth);
        extendedKeyUsageValues.add(KeyPurposeId.id_kp_clientAuth);
        extendedKeyUsageValues.add(KeyPurposeId.id_kp_codeSigning);
        extendedKeyUsageValues.add(KeyPurposeId.id_kp_emailProtection);
        extendedKeyUsageValues.add(KeyPurposeId.id_kp_ipsecEndSystem);
        extendedKeyUsageValues.add(KeyPurposeId.id_kp_ipsecTunnel);
        extendedKeyUsageValues.add(KeyPurposeId.id_kp_ipsecUser);
        extendedKeyUsageValues.add(KeyPurposeId.id_kp_timeStamping);
        extendedKeyUsageValues.add(KeyPurposeId.id_kp_OCSPSigning);

        KeyPurposeId[] extendedKeyUsageArray = new KeyPurposeId[extendedKeyUsageValues.size()];
        extendedKeyUsageValues.toArray(extendedKeyUsageArray);

        LocalDate startDate = LocalDate.parse("2018-02-12");
        LocalDate endtDate = LocalDate.parse("2028-02-12");
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.SHA2_256_RSA;
        BigInteger serialNumber = BigInteger.valueOf(4438154030353282418L);

        ExtendedCertificateData extendedCertificateData = new ExtendedCertificateData(startDate, endtDate, signatureAlgorithm, keyUsageValues,
                extendedKeyUsageArray, serialNumber);

        X509Certificate rootCertificate = certificateGenerator.generateCertificate(subject, issuer, extendedCertificateData);


        createKeyStoreFiles();

        String keyPassword = "123" + serialNumber;

        String rootAlias = newRoot.getEmail() + serialNumber;

        String rootPass = "123";

        keyStoreWriter.write(rootAlias, keyPair.getPrivate(), keyPassword, new X509Certificate[] {rootCertificate});
        keyStoreWriter.saveKeyStore("root.jks", rootPass.toCharArray());

    }

    private void createKeyStoreFiles() throws IOException {

        String selfSignedPass = "123";
        String caPass = "456";
        String endEntityPass = "789";

        this.keyStoreWriter.loadKeyStore("root.jks", selfSignedPass.toCharArray());
        this.keyStoreWriter.loadKeyStore("ca.jks", caPass.toCharArray());
        this.keyStoreWriter.loadKeyStore("end_entity.jks", endEntityPass.toCharArray());

        this.keyStoreWriter.saveKeyStore("root.jks", selfSignedPass.toCharArray());
        this.keyStoreWriter.saveKeyStore("ca.jks", caPass.toCharArray());
        this.keyStoreWriter.saveKeyStore("end_entity.jks", endEntityPass.toCharArray());
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            initialize();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

