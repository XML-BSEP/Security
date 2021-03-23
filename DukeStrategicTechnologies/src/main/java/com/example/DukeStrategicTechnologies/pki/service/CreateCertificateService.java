package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.CertificateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.keystores.KeyStoreReader;
import com.example.DukeStrategicTechnologies.pki.keystores.KeyStoreWriter;
import com.example.DukeStrategicTechnologies.pki.mapper.KeyUsagesMapper;
import com.example.DukeStrategicTechnologies.pki.model.ExtendedCertificateData;
import com.example.DukeStrategicTechnologies.pki.model.Issuer;
import com.example.DukeStrategicTechnologies.pki.model.Subject;
import com.example.DukeStrategicTechnologies.pki.model.User;
import com.example.DukeStrategicTechnologies.pki.repository.UserRepository;
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
import java.util.*;

@Service
public class CreateCertificateService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private CertificateGenerator certificateGenerator;
    private KeyStoreWriter keyStoreWriter;
    private KeyStoreReader keystoreReader;
    private String issuerKeyStorePassword;
    @Autowired
    private UserRepository userRepository;

    public CreateCertificateService() {
        this.keyStoreWriter = new KeyStoreWriter();
        this.keystoreReader = new KeyStoreReader();
        this.certificateGenerator = new CertificateGenerator();
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
            keyStoreWriter.loadKeyStore("ca.jks", "456".toCharArray());
            filePath = "ca.jks";
            keyStorePass = "456";
            subjectPassword = "456" + subjectCertificateSerialNumber;
        }
        else {
            keyStoreWriter.loadKeyStore("end_entity.jks", "789".toCharArray());
            filePath = "end_entity.jks";
            keyStorePass = "789";
            subjectPassword = "789" + subjectCertificateSerialNumber;
        }

        Certificate[] certificateChain = createCertificateChain(keyStore, issuerAllias, subjectCertificate);
        keyStoreWriter.write(subjectAlias, subject.getKeyPair().getPrivate(), subjectPassword, certificateChain);
        keyStoreWriter.saveKeyStore(filePath, keyStorePass.toCharArray());

        user.setCertificateCount(user.getCertificateCount() + 1);
        userRepository.save(user);

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
        String filePath = "root.jks";
        KeyStore keystore = keystoreReader.getKeyStore(filePath, "123");
        return keystore;
    }

    private KeyStore getCAKeystore() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
        String filePath = "ca.jks";
        KeyStore keystore = keystoreReader.getKeyStore(filePath, "456");
        return keystore;
    }

    private KeyStore getEndEntityKeyStore() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
        String filePath = "end_entity.jks";
        KeyStore keystore = keystoreReader.getKeyStore(filePath, "789");
        return keystore;
    }

    private X509Certificate getCertificateByAlias(String alias, KeyStore keystore) throws KeyStoreException {
        X509Certificate certificate = (X509Certificate) keystore.getCertificate(alias);
        return certificate;
    }

    private KeyStore getIssuerKeyStoreByAlias(String alias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        KeyStore keystore = null;
        if ((keystore = getCAKeystore()).containsAlias(alias)) {
            issuerKeyStorePassword = "456";
            return keystore;
        }
        else if ((keystore = getSelfSignedKeyStore()).containsAlias(alias)) {
            issuerKeyStorePassword = "123";
            return keystore;
        }
        else if((keystore = getEndEntityKeyStore()).containsAlias(alias)) {
            issuerKeyStorePassword = "789";
            return keystore;
        }
        else {
            return keystore;
        }
    }

    private void isCertificateValid(KeyStore keyStore, String alias) throws KeyStoreException, NoSuchProviderException, CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Certificate[] certificates = keyStore.getCertificateChain(alias);
        for(int i = certificates.length - 1; i >= 0; i--) {
            if(i == 0) {
                X509Certificate certificate = (X509Certificate) certificates[i];
                certificate.checkValidity();
                break;
            }
            X509Certificate parent = (X509Certificate) certificates[i];
            X509Certificate child = (X509Certificate) certificates[i-1];
            child.verify(parent.getPublicKey());
            parent.checkValidity();

        }

    }

    private Certificate[] createCertificateChain(KeyStore keyStore, String alias, X509Certificate certificate) throws KeyStoreException {
        Certificate[] certificates = keyStore.getCertificateChain(alias);
        Certificate[] certificateChain = new Certificate[certificates.length + 1];
        certificateChain[0] = certificate;
        for(int i = 0; i < certificates.length; i ++) {
            certificateChain[i+1] = certificates[i];


        }
        return certificateChain;
    }

    public List<CertificateDTO> getAllCertificates() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificates = getRootCertificates();
        certificates.addAll(getCaCertificates());
        certificates.addAll(getEndEntityCertificates());

        return certificates;
    }

    private List<CertificateDTO> getRootCertificates() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
        List<CertificateDTO> certificates = new ArrayList<>();
        KeyStore rootKeyStore = getSelfSignedKeyStore();

        List<String> aliases = Collections.list(rootKeyStore.aliases());

        for(String alias : aliases) {
            X509Certificate certificate = getCertificateByAlias(alias, rootKeyStore);
            CertificateDTO certificateDTO = extractCertificateData(certificate);
            certificates.add(certificateDTO);
        }
        return certificates;
    }

    private List<CertificateDTO> getEndEntityCertificates() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
        List<CertificateDTO> certificates = new ArrayList<>();
        KeyStore endEntityKeyStore = getEndEntityKeyStore();

        List<String> aliases = Collections.list(endEntityKeyStore.aliases());

        for(String alias : aliases) {
            X509Certificate certificate = getCertificateByAlias(alias, endEntityKeyStore);
            CertificateDTO certificateDTO = extractCertificateData(certificate);
            certificates.add(certificateDTO);
        }
        return certificates;
    }

    private List<CertificateDTO> getCaCertificates() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
        List<CertificateDTO> certificates = new ArrayList<>();
        KeyStore caKeyStore = getCAKeystore();

        List<String> aliases = Collections.list(caKeyStore.aliases());

        for(String alias : aliases) {
            X509Certificate certificate = getCertificateByAlias(alias, caKeyStore);
            CertificateDTO certificateDTO = extractCertificateData(certificate);
            certificates.add(certificateDTO);
        }
        return certificates;
    }

    private CertificateDTO extractCertificateData(X509Certificate certificate) {
        CertificateDTO certificateDTO = new CertificateDTO();

        String serialNumber = certificate.getSerialNumber().toString();
        Date startDate = certificate.getNotBefore();
        Date endDate = certificate.getNotAfter();
        String signatureAlgorithm = certificate.getSigAlgName();
        boolean[] keyUsage = certificate.getKeyUsage();
        Collection<String> keyUsages = KeyUsagesMapper.keyUsagesBoolToKeyUsagesString(keyUsage);

        certificateDTO.setSerialNumber(serialNumber);
        certificateDTO.setStartDate(startDate);
        certificateDTO.setEndDate(endDate);
        certificateDTO.setSignatureAlgorithm(signatureAlgorithm);
        certificateDTO.setKeyUsages(keyUsages);
        return certificateDTO;
    }
}

