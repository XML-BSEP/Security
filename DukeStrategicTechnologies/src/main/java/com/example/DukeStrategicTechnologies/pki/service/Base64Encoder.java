package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.CertificateDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class Base64Encoder {
    private static final String DOWNLOAD_PATH = System.getProperty("user.home") + "\\Downloads\\";

    @Autowired
    private CertificateService certificateService;

    public boolean downloadCertificate(CertificateDTO dto) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, CMSException, OperatorCreationException, UnrecoverableKeyException {
        String certFileName = DOWNLOAD_PATH + "DTS" + dto.getSerialNumber() + ".p7b";
        String alias = dto.getEmail() + dto.getSerialNumber();

        KeyStore keyStore = certificateService.getKeyStoreByAlias(alias);
        X509Certificate certificate = certificateService.getCertificateByAlias(alias, keyStore);
        Certificate[] certificates = keyStore.getCertificateChain(alias);

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
        List<Certificate> certificateList = new ArrayList<>(Arrays.asList(certificates));

            if(certificate != null) {
            byte[] certByte = null;
            certByte = this.encryptCertToPKCS7(certificate, key, certificateList);

            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(certFileName);

                try {
                    outputStream.write("-----BEGIN CERTIFICATE-----\n".getBytes("US-ASCII"));
                    outputStream.write(Base64.encodeBase64(certByte, true));
                    outputStream.write("-----END CERTIFICATE-----\n".getBytes("US-ASCII"));
                    outputStream.close();

                    return true;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return false;
}

    private byte[] encryptCertToPKCS7(X509Certificate certificate, Key key, List<Certificate> certificates)
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
}
