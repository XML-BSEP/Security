package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.CertificateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.PossibleKeyUsagesDTO;
import com.example.DukeStrategicTechnologies.pki.mapper.ExtendedKeyUsagesMapper;
import com.example.DukeStrategicTechnologies.pki.model.enums.SignatureAlgorithm;
import com.example.DukeStrategicTechnologies.pki.repository.AccountRepository;
import com.example.DukeStrategicTechnologies.pki.util.keystores.KeyStoreReader;
import com.example.DukeStrategicTechnologies.pki.util.keystores.KeyStoreWriter;
import com.example.DukeStrategicTechnologies.pki.mapper.KeyUsagesMapper;
import com.example.DukeStrategicTechnologies.pki.model.*;
import com.example.DukeStrategicTechnologies.pki.repository.RevokedCertificateRepository;
import com.example.DukeStrategicTechnologies.pki.repository.UserRepository;
import com.example.DukeStrategicTechnologies.pki.util.properties.KeyStoreProperties;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.time.LocalDate;
import java.util.*;

@Service
public class CertificateService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    public static final String CERTIFICATE_NOT_VALID = "Certificate is not valid!";
    public static final String CERTIFICATE_REVOKED = "Certificate is revoked!";
    public static final String SUBJECT_NOT_FOUND = "Subject not found!";
    public static final String ISSUER_NOT_FOUND = "Issuer not found!";
    public static final String SELF_SIGNED_EXCEPTION = "Self-signed certificates can only be issued by self-signed certificates!";
    public static final String KEYSTORE_NOT_FOUND = "KeyStore not found!";
    public static final String END_DATE_BEFORE_START = "End date must be after start date!";
    public static final String INVALID_DATE = "Invalid date!";
    public static final String INVALID_KEY_USAGE = "Key usage can't be added because issuer has no permission!";
    public static final String NO_USER_PERMISSIONS = "Only admins can revoke certificate";

    private CertificateGenerator certificateGenerator;
    private KeyStoreWriter keyStoreWriter;
    private KeyStoreReader keystoreReader;
    private String issuerKeyStorePassword;
    private KeyStoreProperties keyStoreProperties;

    @Autowired
    private OCSPService ocspService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RevokedCertificateRepository revokedCertificateRepository;

    @Autowired
    private AccountRepository accountRepository;

    public CertificateService() {
        this.keyStoreWriter = new KeyStoreWriter();
        this.keystoreReader = new KeyStoreReader();
        this.certificateGenerator = new CertificateGenerator();
        this.keyStoreProperties = new KeyStoreProperties();
    }


    public void createRootCertificate(CreateCertificateDTO createCertificateDTO) throws Exception {
        User root = userRepository.findAll().stream().filter(user -> user.getEmail().equals("root@gmail.com")).findFirst().orElse(null);

        if(root == null) {
            throw new Exception("Znaci...");
        }

        if(createCertificateDTO.getIssuerId() != createCertificateDTO.getSubjectId()) {
            throw new Exception("Znaci...");
        }

        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, root.getCommonName());
        builder.addRDN(BCStyle.SURNAME, root.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, root.getGivenName());
        builder.addRDN(BCStyle.O, root.getOrganization());
        builder.addRDN(BCStyle.OU, root.getOrganizationUnit());
        builder.addRDN(BCStyle.C, root.getState());
        builder.addRDN(BCStyle.E, root.getEmail());

        KeyPair keyPair = generateKeyPair();
        Issuer issuer = new Issuer(builder.build(), keyPair);
        Subject subject = new Subject(issuer.getX500Name(), issuer.getKeyPair());
        BigInteger serialNumber = generateSerialNumberForCertificate();

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

        SignatureAlgorithm signatureAlgorithm = createCertificateDTO.getSignatureAlgorithm();

        ExtendedCertificateData extendedCertificateData = new ExtendedCertificateData(LocalDate.parse(createCertificateDTO.getStartDate()),
                LocalDate.parse(createCertificateDTO.getEndDate()),
                signatureAlgorithm,
                keyUsageValues,
                extendedKeyUsageArray,
                serialNumber
        );

        X509Certificate rootCertificate = certificateGenerator.generateCertificate(subject, issuer, extendedCertificateData);

        String keyStorePass = keyStoreProperties.readKeyStorePass(KeyStoreProperties.ROOT_FILE) + serialNumber;

        String rootAlias = root.getEmail() + serialNumber;

        keyStoreWriter.loadKeyStore(KeyStoreProperties.ROOT_FILE, keyStoreProperties.readKeyStorePass(KeyStoreProperties.ROOT_FILE).toCharArray());
        keyStoreWriter.write(rootAlias, subject.getKeyPair().getPrivate(), keyStorePass, new X509Certificate[] {rootCertificate});
        keyStoreWriter.saveKeyStore(KeyStoreProperties.ROOT_FILE, keyStoreProperties.readKeyStorePass(KeyStoreProperties.ROOT_FILE).toCharArray());
    }

    public void createCertificate(CreateCertificateDTO createCertificateDTO) throws Exception {
        if (LocalDate.parse(createCertificateDTO.getEndDate()).compareTo(LocalDate.parse(createCertificateDTO.getStartDate())) < 0) {
            throw new Exception(END_DATE_BEFORE_START);
        }

        Subject subject = generateSubject(createCertificateDTO);

        User issuer = userRepository.findById(createCertificateDTO.getIssuerId()).get();
        if (issuer == null) {
            throw new Exception(ISSUER_NOT_FOUND);
        }

        String issuerAlias = issuer.getEmail() + createCertificateDTO.getIssuerSerialNumber();
        KeyStore keyStore = getIssuerKeyStoreByAlias(issuerAlias);

        if(keyStore == null) {
            throw new Exception(KEYSTORE_NOT_FOUND);
        }

        User user = userRepository.findById(createCertificateDTO.getSubjectId()).get();
        if (user == null) {
            throw new Exception(SUBJECT_NOT_FOUND);
        }

        if (user.getEmail().equals(issuer.getEmail())) {
            if (!isSelfSignedCertificate(issuerAlias)) {
                throw new Exception(SELF_SIGNED_EXCEPTION);
            }
        }

        X509Certificate issuerCertificate = getCertificateByAlias(issuerAlias, keyStore);

        String issuerPassword = getKeyStorePassFromAlias(issuerAlias) + issuerCertificate.getSerialNumber();
        isCertificateValid(keyStore, issuerAlias);
        isDateValid(issuerCertificate, LocalDate.parse(createCertificateDTO.getStartDate()), LocalDate.parse(createCertificateDTO.getEndDate()));

        BigInteger subjectCertificateSerialNumber = generateSerialNumberForCertificate();
        String subjectAlias = user.getEmail() + subjectCertificateSerialNumber;

        ExtendedCertificateData extendedCertificateData = new ExtendedCertificateData(LocalDate.parse(createCertificateDTO.getStartDate()),
                LocalDate.parse(createCertificateDTO.getEndDate()),
                createCertificateDTO.getSignatureAlgorithm(),
                KeyUsagesMapper.keyUsagesDTOToKeyUsages(createCertificateDTO),
                KeyUsagesMapper.extendedKeyUsagesDTOToValues(createCertificateDTO),
                subjectCertificateSerialNumber);

        X500Name issureX509Name = new JcaX509CertificateHolder(issuerCertificate).getSubject();
        Key issuerPrivateKey =  keyStore.getKey(issuerAlias, issuerPassword.toCharArray());
        PublicKey issuerPublicKey = issuerCertificate.getPublicKey();
        KeyPair issuerKeyPair = new KeyPair(issuerPublicKey, (PrivateKey) issuerPrivateKey);
        Issuer issuerData = new Issuer(issureX509Name, issuerKeyPair);

        X509Certificate subjectCertificate = certificateGenerator.generateCertificate(subject, issuerData, extendedCertificateData);
        String subjectPassword = "";
        String keyStorePass = "";
        String filePath = "";
        if (isCA(subjectCertificate)) {
            User user1 = userRepository.findById(createCertificateDTO.getSubjectId()).get();
            Account account = accountRepository.findByEmail(user1.getEmail());

            List<Authority> authorities = new ArrayList<>();
            authorities.add(new Authority(3L, "ROLE_CA"));
            account.setAuthorities(new ArrayList<>(authorities));
            accountRepository.save(account);

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

        Certificate[] certificateChain = createCertificateChain(keyStore, issuerAlias, subjectCertificate);
        keyStoreWriter.write(subjectAlias, subject.getKeyPair().getPrivate(), subjectPassword, certificateChain);
        keyStoreWriter.saveKeyStore(filePath, keyStorePass.toCharArray());

        user.setCertificateCount(user.getCertificateCount() + 1);
        userRepository.save(user);

    }

    public void isDateValid(X509Certificate issuerCertificate, LocalDate subjectNotBefore, LocalDate subjectNotAfter) throws Exception {
        Date issuerNotBefore = issuerCertificate.getNotBefore();
        Date issuerNotAfter = issuerCertificate.getNotAfter();

        Date subjectNotBeforeDate = java.sql.Date.valueOf(subjectNotBefore);
        Date subjectNotAfterDate = java.sql.Date.valueOf(subjectNotAfter);

        if(subjectNotBeforeDate.compareTo(new Date((new Date()).getTime() + 24*60*60*1000)) >= 0|| subjectNotBeforeDate.before(issuerNotBefore)) {
            throw new Exception(INVALID_DATE);
        }

        if(subjectNotAfterDate.after(issuerNotAfter)) {
            throw new Exception(INVALID_DATE);
        }

    }

    private boolean isSelfSignedCertificate(String issuerAlias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        KeyStore keyStore = getSelfSignedKeyStore();
        return keyStore.containsAlias(issuerAlias);
    }

    public void revokeCertificate(String serialNumber, String mail) throws Exception {
        Account user = accountRepository.findByEmail(mail);
        if (!user.getRole().equals("Admin")) {
            throw new Exception(NO_USER_PERMISSIONS);
        }

        if (revokedCertificateRepository.findBySerialNumber(serialNumber) != null) {
            throw new Exception(CERTIFICATE_REVOKED);
        }

        revokeAllCertificatesInHierarchyBelow(serialNumber);
        revokedCertificateRepository.save(new RevokedCertificate(serialNumber, new Date()));
    }

    private void revokeAllCertificatesInHierarchyBelow(String serialNumber) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        KeyStore currentKeyStore = getEndEntityKeyStore();
        revokeAllByKeyStore(currentKeyStore, serialNumber);
        currentKeyStore = getCAKeystore();
        revokeAllByKeyStore(currentKeyStore, serialNumber);
        currentKeyStore = getSelfSignedKeyStore();
        revokeAllByKeyStore(currentKeyStore, serialNumber);
    }

    private void revokeAllByKeyStore(KeyStore currentKeyStore, String serialNumber) throws KeyStoreException {
        List<String> aliases = Collections.list(currentKeyStore.aliases());
        for (String alias : aliases) {
            checkCertificateChain(currentKeyStore.getCertificateChain(alias), serialNumber);
        }
    }

    private void checkCertificateChain(Certificate[] certificateChain, String serialNumber) {
        int position = -1;
        for (int i = 0; i < certificateChain.length; i++) {
            if (((X509Certificate) certificateChain[i]).getSerialNumber().equals(new BigInteger(serialNumber))) {
                position = i;
            }
        }

        if (position != -1) {
            for (int i = 0; i < position; i++) {
                X509Certificate certificate = (X509Certificate) certificateChain[i];
                revokedCertificateRepository.save(new RevokedCertificate(certificate.getSerialNumber().toString(), new Date()));
            }
        }
    }

    public String generateAliasByCertificate(X509Certificate certificate) throws CertificateEncodingException {
        X500Name subject = null;
        subject = new JcaX509CertificateHolder(certificate).getSubject();

        String subjectEmail = subject.getRDNs()[6].getFirst().getValue().toString();

        return subjectEmail + certificate.getSerialNumber();
    }

    private boolean isCA(X509Certificate certificate) {
        return certificate.getKeyUsage()[5];
    }

    private Subject generateSubject(CreateCertificateDTO dto) throws Exception {

        KeyPair keyPairSubject = generateKeyPair();

        User user = userRepository.findById(dto.getSubjectId()).get();
        if (user == null) {
            throw new Exception(SUBJECT_NOT_FOUND);
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

    public X509Certificate getCertificateByAlias(String alias, KeyStore keystore) throws KeyStoreException {
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

    public KeyStore getKeyStoreByAlias(String alias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        KeyStore keystore = null;
        if ((keystore = getCAKeystore()).containsAlias(alias)) {
            return keystore;
        }
        else if ((keystore = getSelfSignedKeyStore()).containsAlias(alias)) {
            return keystore;
        }
        else if((keystore = getEndEntityKeyStore()).containsAlias(alias)) {
            return keystore;
        }
        else {
            return keystore;
        }
    }

    private void isCertificateValid(KeyStore keyStore, String alias) throws Exception {
        Certificate[] certificates = keyStore.getCertificateChain(alias);
        try {
            PrivateKey password;
            String aliasPassword = "";
            String currentAlias = "";
            String keyStorePass = "";
            KeyStore currentKeyStore = null;
            for (int i = certificates.length - 1; i >= 0; i--) {

                currentAlias = generateAliasByCertificate((X509Certificate) certificates[i]);
                currentKeyStore = getKeyStoreByAlias(currentAlias);
                keyStorePass = getKeyStorePassFromAlias(currentAlias);
                aliasPassword = keyStorePass + ((X509Certificate) certificates[i]).getSerialNumber();

                if (ocspService.isCertificateRevoked((X509Certificate) certificates[i],
                        (PrivateKey) currentKeyStore.getKey(currentAlias, aliasPassword.toCharArray()))) {
                    throw new Exception(CERTIFICATE_REVOKED);
                }

                if (i == 0) {
                    X509Certificate certificate = (X509Certificate) certificates[i];
                    certificate.checkValidity();
                    break;
                }

                X509Certificate parent = (X509Certificate) certificates[i];
                X509Certificate child = (X509Certificate) certificates[i - 1];
                child.verify(parent.getPublicKey());
                parent.checkValidity();
            }
        } catch (Exception e) {
            throw new Exception(CERTIFICATE_NOT_VALID);
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

    public List<CertificateDTO> getRootCertificates() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
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

    public List<CertificateDTO> getEndEntityCertificates() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
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

    public List<CertificateDTO> getCaCertificates() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
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

    private CertificateDTO extractCertificateData(X509Certificate certificate) throws CertificateParsingException, CertificateEncodingException {
        CertificateDTO certificateDTO = new CertificateDTO();

        String serialNumber = certificate.getSerialNumber().toString();
        Date startDate = certificate.getNotBefore();
        Date endDate = certificate.getNotAfter();
        String signatureAlgorithm = certificate.getSigAlgName();
        boolean[] keyUsage = certificate.getKeyUsage();
        Collection<String> keyUsages = KeyUsagesMapper.keyUsagesBoolToKeyUsagesString(keyUsage);
        Collection<String> extendedKeyUsages = ExtendedKeyUsagesMapper.extendedKeyUsagesToStringCollection(certificate.getExtendedKeyUsage());
        X500Name x500Name = new JcaX509CertificateHolder(certificate).getSubject();
        RDN emailRdn = x500Name.getRDNs(BCStyle.EmailAddress)[0];
        String email = IETFUtils.valueToString(emailRdn.getFirst().getValue());
        RDN cnRdn = x500Name.getRDNs(BCStyle.CN)[0];
        String commonName = IETFUtils.valueToString(cnRdn.getFirst().getValue());
        User user = userRepository.findByEmail(email);

        certificateDTO.setSerialNumber(serialNumber);
        certificateDTO.setStartDate(startDate);
        certificateDTO.setEndDate(endDate);
        certificateDTO.setSignatureAlgorithm(signatureAlgorithm);
        certificateDTO.setKeyUsages(keyUsages);
        certificateDTO.setExtendedKeyUsages(extendedKeyUsages);
        certificateDTO.setEmail(email);
        certificateDTO.setCommonName(commonName);
        certificateDTO.setRevoked(ocspService.isRevoked(serialNumber));
        certificateDTO.setIssuerId(user.getId());

        return certificateDTO;
    }

    public String getKeyStorePassFromAlias(String alias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        if ((getCAKeystore()).containsAlias(alias)) {
            return keyStoreProperties.readKeyStorePass(KeyStoreProperties.CA_FILE);
        }
        else if ((getSelfSignedKeyStore()).containsAlias(alias)) {
            return keyStoreProperties.readKeyStorePass(KeyStoreProperties.ROOT_FILE);
        }
        else if((getEndEntityKeyStore()).containsAlias(alias)) {
            return keyStoreProperties.readKeyStorePass(KeyStoreProperties.END_ENTITY_FILE);
        }
        else {
            return "";
        }
    }


    public List<CertificateDTO> getAllCertificatesByUser(String mail) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificatesByUser = new ArrayList<>();

        certificatesByUser.addAll(checkEndEntityCertificates(mail));
        certificatesByUser.addAll(checkCaCertificates(mail));
        certificatesByUser.addAll(checkSelfSignedCertificates(mail));

        return certificatesByUser;
    }

    public List<CertificateDTO> getAllCaCertificatesByUser(String mail) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificateDTOS = new ArrayList<>();

        certificateDTOS.addAll(checkCaCertificates(mail));

        return  certificateDTOS;
    }

    public List<CertificateDTO> getAllEndEntityCertificatesByUser(String mail) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificateDTOS = new ArrayList<>();

        certificateDTOS.addAll(checkEndEntityCertificates(mail));

        return  certificateDTOS;
    }

    private Collection<? extends CertificateDTO> checkSelfSignedCertificates(String mail) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificateDTOS = new ArrayList<>();
        List<CertificateDTO> allCertificatesByKeyStore = getRootCertificates();

        for (CertificateDTO certificateDTO : allCertificatesByKeyStore) {
            if (certificateDTO.getEmail().equals(mail)) {
                certificateDTOS.add(certificateDTO);
            }
        }

        return certificateDTOS;
    }

    private Collection<? extends CertificateDTO> checkCaCertificates(String mail) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificateDTOS = new ArrayList<>();
        List<CertificateDTO> allCertificatesByKeyStore = getCaCertificates();

        for (CertificateDTO certificateDTO : allCertificatesByKeyStore) {
            if (certificateDTO.getEmail().equals(mail)) {
                certificateDTOS.add(certificateDTO);
            }
        }

        return certificateDTOS;
    }

    private Collection<? extends CertificateDTO> checkEndEntityCertificates(String mail) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificateDTOS = new ArrayList<>();
        List<CertificateDTO> allCertificatesByKeyStore = getEndEntityCertificates();

        for (CertificateDTO certificateDTO : allCertificatesByKeyStore) {
            if (certificateDTO.getEmail().equals(mail)) {
                certificateDTOS.add(certificateDTO);
            }
        }

        return certificateDTOS;
    }


    public List<CertificateDTO> getAllCertificatesForSigning() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificates = getAllCertificates();
        List<CertificateDTO> certificatesForSiging = new ArrayList<>();

        for(CertificateDTO certificate : certificates) {
            if(isCA(certificate)) {
                certificatesForSiging.add(certificate);
            }
        }

        return certificatesForSiging;
    }

    public List<CertificateDTO> getAllCertificatesForSigningByUser(String mail) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        List<CertificateDTO> certificates = getAllCertificatesByUser(mail);
        List<CertificateDTO> certificatesForSiging = new ArrayList<>();

        for(CertificateDTO certificate : certificates) {
            if(isCA(certificate)) {
                certificatesForSiging.add(certificate);
            }
        }

        return certificatesForSiging;
    }

    private boolean isCA(CertificateDTO certificateDTO) {
        Collection<String> keyUsages = certificateDTO.getKeyUsages();
//        if(keyUsages.contains("certficateSigning")) {
//            return true;
//        }
        for(String keyUsage : keyUsages){
            if(keyUsage.equalsIgnoreCase("certificateSigning")){
                return true;
            }
        }
        return false;
    }

    public PossibleKeyUsagesDTO getPossibleKeyUsages(String alias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        KeyStore keyStore = getKeyStoreByAlias(alias);
        X509Certificate certificate = getCertificateByAlias(alias, keyStore);

        PossibleKeyUsagesDTO possibleKeyUsagesDTO = new PossibleKeyUsagesDTO();

        possibleKeyUsagesDTO.setPossibleKeyUsages(KeyUsagesMapper.keyUsagesBoolToKeyUsagesString(certificate.getKeyUsage()));
        possibleKeyUsagesDTO.setPossibleExtendedKeyUsages(ExtendedKeyUsagesMapper.extendedKeyUsagesToStringCollection(certificate.getExtendedKeyUsage()));

        return possibleKeyUsagesDTO;
    }
}

