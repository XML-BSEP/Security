package com.example.DukeStrategicTechnologies.pki.util;

import com.example.DukeStrategicTechnologies.pki.util.keystores.KeyStoreWriter;
import com.example.DukeStrategicTechnologies.pki.model.ExtendedCertificateData;
import com.example.DukeStrategicTechnologies.pki.model.Issuer;
import com.example.DukeStrategicTechnologies.pki.model.Subject;
import com.example.DukeStrategicTechnologies.pki.model.User;
import com.example.DukeStrategicTechnologies.pki.model.enums.SignatureAlgorithm;
import com.example.DukeStrategicTechnologies.pki.repository.UserRepository;
import com.example.DukeStrategicTechnologies.pki.service.CertificateGenerator;
import com.example.DukeStrategicTechnologies.pki.service.CertificateService;
import com.example.DukeStrategicTechnologies.pki.util.properties.KeyStoreProperties;
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
import java.util.Locale;

@Component
@EnableAutoConfiguration
public class InitializeCertificate implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CertificateService createCertificateService;

    private CertificateGenerator certificateGenerator;

    private KeyStoreWriter keyStoreWriter;

    private KeyStoreProperties keyStoreProperties;

    public InitializeCertificate() {
        this.certificateGenerator = new CertificateGenerator();
        this.keyStoreWriter = new KeyStoreWriter();
        this.keyStoreProperties = new KeyStoreProperties();
    }

    public void initialize() throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        User root = userRepository.findAll().stream().filter(user -> user.getEmail().equals("root@gmail.com")).findFirst().orElse(null);

        if(root != null) {
            return;
        }


        User newRoot = new User(100L, "root", "rootovic", "rootic", "org1", "orgunit1", "Serbia", "Novi Sad", "root@gmail.com", true, 1L);

        userRepository.save(newRoot);

        String rootPath = KeyStoreProperties.ROOT_FILE + ".jks";
        File f = new File(rootPath);
        if(f.exists()) {
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

        String keyPassword = keyStoreProperties.readKeyStorePass(KeyStoreProperties.ROOT_FILE) + serialNumber;

        String rootAlias = newRoot.getEmail() + serialNumber;

        keyStoreWriter.loadKeyStore(KeyStoreProperties.ROOT_FILE, keyStoreProperties.readKeyStorePass(KeyStoreProperties.ROOT_FILE).toCharArray());
        this.keyStoreWriter.write(rootAlias, keyPair.getPrivate(), keyPassword, new X509Certificate[] {rootCertificate});
        this.keyStoreWriter.saveKeyStore(KeyStoreProperties.ROOT_FILE , keyStoreProperties.readKeyStorePass(KeyStoreProperties.ROOT_FILE).toCharArray());

    }

    private void createKeyStoreFiles() throws IOException {

        this.keyStoreWriter.loadKeyStore(KeyStoreProperties.ROOT_FILE, keyStoreProperties.readKeyStorePass(KeyStoreProperties.ROOT_FILE).toCharArray());
        this.keyStoreWriter.saveKeyStore(KeyStoreProperties.ROOT_FILE , keyStoreProperties.readKeyStorePass(KeyStoreProperties.ROOT_FILE).toCharArray());

        this.keyStoreWriter.loadKeyStore(KeyStoreProperties.CA_FILE, keyStoreProperties.readKeyStorePass(KeyStoreProperties.CA_FILE).toCharArray());
        this.keyStoreWriter.saveKeyStore(KeyStoreProperties.CA_FILE , keyStoreProperties.readKeyStorePass(KeyStoreProperties.CA_FILE).toCharArray());

        this.keyStoreWriter.loadKeyStore(KeyStoreProperties.END_ENTITY_FILE, keyStoreProperties.readKeyStorePass(KeyStoreProperties.END_ENTITY_FILE).toCharArray());
        this.keyStoreWriter.saveKeyStore(KeyStoreProperties.END_ENTITY_FILE, keyStoreProperties.readKeyStorePass(KeyStoreProperties.END_ENTITY_FILE).toCharArray());

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

