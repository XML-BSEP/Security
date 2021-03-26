package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.DownloadCertificateDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.exception.ExtCertificateEncodingException;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

@Service
public class Base64Encoder {
    private static final String DOWNLOAD_PATH = System.getProperty("user.home") + "\\Downloads\\";
    private String filepath;
    @Autowired
    private CertificateService certificateService;

    @Autowired
    private OCSPService ocspService;
    public void downloadCertificate(DownloadCertificateDTO dto) throws Exception {

        if(ocspService.isRevoked(dto.getSerialNumber())) {
            throw new Exception("Can not download revoked certificate!");
        }
        String alias = dto.getSubjectEmail() + dto.getSerialNumber();

        KeyStore keyStore = certificateService.getKeyStoreByAlias(alias);
        X509Certificate certificate = certificateService.getCertificateByAlias(alias, keyStore);
        Certificate[] certificates = keyStore.getCertificateChain(alias);

        filepath = DOWNLOAD_PATH + "DukeStrategicTechnologies-SN-" + dto.getSerialNumber() + ".p7b";

        Certificate issuerCertificate = null;

        if(certificates.length == 1) {
            issuerCertificate = certificates[0];
        } else {
            issuerCertificate= certificates[1];
        }

        String issuerAlias = certificateService.generateAliasByCertificate((X509Certificate) issuerCertificate);
        KeyStore issuerKeyStore = certificateService.getKeyStoreByAlias(issuerAlias);
        String issuerPassword = certificateService.getKeyStorePassFromAlias(issuerAlias);
        Key key = issuerKeyStore.getKey(issuerAlias, (issuerPassword + ((X509Certificate) issuerCertificate).getSerialNumber()).toCharArray());

        if(certificate != null) {
            encodeCertificate(certificate, key, certificates);
        }
    }

    private byte[] signPKCS7(X509Certificate certificate, Key key, List<Certificate> certificates)
            throws CertificateEncodingException, CMSException, IOException, OperatorCreationException {

        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

        ContentSigner sha256Signer = new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build((PrivateKey) key);

        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder()
                .setProvider("BC").build())
                .build(sha256Signer, certificate));

        generator.addCertificates(new JcaCertStore(certificates));

        CMSTypedData content = new CMSProcessableByteArray(certificate.getEncoded());
        CMSSignedData signedData = generator.generate(content, true);

        return signedData.getEncoded();
    }

    private void encodeCertificate(X509Certificate certificate, Key key, Certificate[] certificates) throws OperatorCreationException, CertificateEncodingException, CMSException, IOException {
        byte[] certificateBytes;

        certificateBytes = signPKCS7(certificate, key, Arrays.asList(certificates));

        FileOutputStream outputStream = null;

        outputStream = new FileOutputStream(filepath);

        outputStream.write("-----BEGIN CERTIFICATE-----\n".getBytes("US-ASCII"));
        outputStream.write(Base64.encodeBase64(certificateBytes, true));
        outputStream.write("-----END CERTIFICATE-----\n".getBytes("US-ASCII"));
        outputStream.close();

    }
}


