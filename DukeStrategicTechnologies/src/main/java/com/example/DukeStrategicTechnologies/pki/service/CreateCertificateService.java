package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.keystores.KeyStoreReader;
import com.example.DukeStrategicTechnologies.pki.keystores.KeyStoreWriter;
import com.example.DukeStrategicTechnologies.pki.mapper.KeyUsagesMapper;
import com.example.DukeStrategicTechnologies.pki.model.ExtendedCertificateData;
import com.example.DukeStrategicTechnologies.pki.model.Issuer;
import com.example.DukeStrategicTechnologies.pki.model.Subject;
import com.example.DukeStrategicTechnologies.pki.model.User;
import com.example.DukeStrategicTechnologies.pki.repository.UserRepository;
import com.example.DukeStrategicTechnologies.pki.util.properties.KeyStoreProperties;
import org.apache.tomcat.jni.Local;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class CreateCertificateService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private CertificateGenerator certificateGenerator;
    private KeyStoreWriter keyStoreWriter;
    private KeyStoreReader keystoreReader;
    private String issuerKeyStorePassword;
    private KeyStoreProperties keyStoreProperties;
    @Autowired
    private UserRepository userRepository;

    public CreateCertificateService() {
        this.keyStoreWriter = new KeyStoreWriter();
        this.keystoreReader = new KeyStoreReader();
        this.certificateGenerator = new CertificateGenerator();
        this.keyStoreProperties = new KeyStoreProperties();
    }


    public void createCertificate(CreateCertificateDTO createCertificateDTO) throws Exception {

        Subject subject = generateSubject(createCertificateDTO);

        User issuer = userRepository.findById(createCertificateDTO.getIssuerId()).get();
        if (issuer == null) {
            throw new Exception();
        }

        String issuerAllias = issuer.getEmail() + createCertificateDTO.getIssuerSerialNumber();
        KeyStore keyStore = getIssuerKeyStoreByAlias(issuerAllias);

        if(keyStore == null) {
            throw new Exception("Znaci...");
        }
        X509Certificate issuerCertificate = getCertificateByAlias(issuerAllias, keyStore);

        String issuerPassword = issuerKeyStorePassword + issuerCertificate.getSerialNumber();
        isCertificateValid(keyStore, issuerAllias);

        User user = userRepository.findById(createCertificateDTO.getSubjectId()).get();
        if (user == null) {
            throw new Exception("Znaci...");
        }

        BigInteger subjectCertificateSerialNumber = generateSerialNumberForCertificate();
        String subjectAlias = user.getEmail() + subjectCertificateSerialNumber;



        ExtendedCertificateData extendedCertificateData = new ExtendedCertificateData(LocalDate.parse("2020-03-12"),
                LocalDate.parse("2020-10-12"),
                createCertificateDTO.getSignatureAlgorithm(),
                KeyUsagesMapper.eyUsagesDTOToKeyUsages(createCertificateDTO),
                KeyUsagesMapper.extendedKeyUsagesDTOToValues(createCertificateDTO),
                subjectCertificateSerialNumber);

        X500Name issureX509Name = new JcaX509CertificateHolder(issuerCertificate).getSubject();
        Key issuerPrivateKey =  keyStore.getKey(issuerAllias, issuerPassword.toCharArray());
        PublicKey issuerPublicKey = issuerCertificate.getPublicKey();
        KeyPair issuerKeyPair = new KeyPair(issuerPublicKey, (PrivateKey) issuerPrivateKey);
        Issuer issuerData = new Issuer(issureX509Name, issuerKeyPair);

        X509Certificate subjectCertificate = certificateGenerator.generateCertificate(subject, issuerData, extendedCertificateData);
        String subjectPassword = "";
        String keyStorePass = "";
        String filePath = "";
        if (isCA(subjectCertificate)) {
            keyStorePass = keyStoreProperties.readKeyStorePass((KeyStoreProperties.CA_FILE));
            filePath = KeyStoreProperties.CA_FILE;
            keyStoreWriter.loadKeyStore(KeyStoreProperties.CA_FILE, keyStorePass.toCharArray());
            subjectPassword = keyStorePass + subjectCertificateSerialNumber;
        }
        else {
            keyStorePass = keyStoreProperties.readKeyStorePass((KeyStoreProperties.END_ENTITY_FILE));
            filePath = KeyStoreProperties.END_ENTITY_FILE;
            keyStoreWriter.loadKeyStore(KeyStoreProperties.END_ENTITY_FILE, keyStorePass.toCharArray());
            subjectPassword = keyStorePass + subjectCertificateSerialNumber;
        }

        Certificate[] certificateChain = createCertificateChain(keyStore, issuerAllias, subjectCertificate);
        keyStoreWriter.write(subjectAlias, subject.getKeyPair().getPrivate(), subjectPassword, certificateChain);
        keyStoreWriter.saveKeyStore(filePath, keyStorePass.toCharArray());

    }


    private boolean isCA(X509Certificate certificate) {
        return certificate.getKeyUsage()[5];
    }
    private Subject generateSubject(CreateCertificateDTO dto) throws Exception {

        KeyPair keyPairSubject = generateKeyPair();

        User user = userRepository.findById(dto.getSubjectId()).get();
        if (user == null) {
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


    public KeyPair generateKeyPair() {
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

    public BigInteger generateSerialNumberForCertificate() throws NoSuchProviderException, NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        return new BigInteger(64, random);

    }

    private KeyStore getSelfSignedKeyStore() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
        return keystoreReader.getKeyStore(KeyStoreProperties.ROOT_FILE, keyStoreProperties.readKeyStorePass(KeyStoreProperties.ROOT_FILE));
    }

    private KeyStore getCAKeystore() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
        return keystoreReader.getKeyStore(KeyStoreProperties.CA_FILE, keyStoreProperties.readKeyStorePass(KeyStoreProperties.CA_FILE));
    }

    private KeyStore getEndEntityKeyStore() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
        return keystoreReader.getKeyStore(KeyStoreProperties.END_ENTITY_FILE, keyStoreProperties.readKeyStorePass(KeyStoreProperties.END_ENTITY_FILE));
    }

    private X509Certificate getCertificateByAlias(String alias, KeyStore keystore) throws KeyStoreException {
        X509Certificate certificate = (X509Certificate) keystore.getCertificate(alias);
        return certificate;
    }

    private KeyStore getIssuerKeyStoreByAlias(String alias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        KeyStore keystore = null;
        if ((keystore = getCAKeystore()).containsAlias(alias)) {
            issuerKeyStorePassword = keyStoreProperties.readKeyStorePass(KeyStoreProperties.CA_FILE);
            return keystore;
        }
        else if ((keystore = getSelfSignedKeyStore()).containsAlias(alias)) {
            issuerKeyStorePassword = keyStoreProperties.readKeyStorePass(KeyStoreProperties.ROOT_FILE);
            return keystore;
        }
        else if((keystore = getEndEntityKeyStore()).containsAlias(alias)) {
            issuerKeyStorePassword = keyStoreProperties.readKeyStorePass(KeyStoreProperties.END_ENTITY_FILE);
            return keystore;
        }
        else {
            return keystore;
        }
    }

    private void isCertificateValid(KeyStore keyStore, String alias) throws KeyStoreException, NoSuchProviderException, CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Certificate[] certificates = keyStore.getCertificateChain(alias);
        for(int i = 0; i < certificates.length; i++) {
            if(i == certificates.length - 1) {
                X509Certificate certificate = (X509Certificate) certificates[i];
                certificate.checkValidity();
                break;
            }
            X509Certificate child = (X509Certificate) certificates[i+1];
            X509Certificate parent = (X509Certificate) certificates[i];

            child.verify(parent.getPublicKey());
            parent.checkValidity();

        }

    }

    private Certificate[] createCertificateChain(KeyStore keyStore, String alias, X509Certificate certificate) throws KeyStoreException {
        Certificate[] certificates = keyStore.getCertificateChain(alias);
        Certificate[] certificateChain = new Certificate[certificates.length + 1];
        for(int i = 0; i < certificates.length; i ++) {
            certificateChain[i] = certificates[i];

            if(i == certificates.length - 1) {
                certificateChain[i+1] = certificate;
            }
        }
        return certificateChain;
    }
}

