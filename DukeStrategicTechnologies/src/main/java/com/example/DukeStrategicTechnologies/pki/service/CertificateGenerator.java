package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.model.ExtendedCertificateData;
import com.example.DukeStrategicTechnologies.pki.model.Issuer;
import com.example.DukeStrategicTechnologies.pki.model.Subject;


import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Date;

public class CertificateGenerator {
    public CertificateGenerator() {}

    public X509Certificate generateCertificate(Subject subjectData, Issuer issuerData, ExtendedCertificateData extendedCertificateData) {
        try {

            JcaContentSignerBuilder builder = new JcaContentSignerBuilder(extendedCertificateData.getSignatureAlgorithm().toString());
            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(issuerData.getKeyPair().getPrivate());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500Name(),
                    extendedCertificateData.getSerialNumber(),
                    Date.valueOf(extendedCertificateData.getStartDate()),
                    Date.valueOf(extendedCertificateData.getEndDate()),
                    subjectData.getX500Name(),
                    subjectData.getKeyPair().getPublic());



            certGen.addExtension(Extension.keyUsage, true, new KeyUsage(generateKeyUsage(extendedCertificateData)));
            certGen.addExtension(Extension.extendedKeyUsage, true, new ExtendedKeyUsage(extendedCertificateData.getExtendedKeyUsages()));


            //extend

            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            return certConverter.getCertificate(certHolder);

        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (CertIOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int generateKeyUsage(ExtendedCertificateData extendedCertificateData) {

        int retVal = 0;
        for (int i : extendedCertificateData.getKeyUsage()) {
            retVal = retVal | i;
        }

        return retVal;
    }
}
