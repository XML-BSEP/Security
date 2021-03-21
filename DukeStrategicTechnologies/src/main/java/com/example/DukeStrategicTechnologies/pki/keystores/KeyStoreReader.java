package com.example.DukeStrategicTechnologies.pki.keystores;

import com.example.DukeStrategicTechnologies.pki.model.Issuer;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class KeyStoreReader {

    private KeyStore keyStore;

    public KeyStoreReader() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
        try {

            KeyStore ks = getKeyStore(keyStoreFile, keyStorePass);

            if(ks.isKeyEntry(alias)) {
                Certificate cert = ks.getCertificate(alias);
                return cert;
            }

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyStore getKeyStore(String keyStoreFile, String keyStorePass) throws KeyStoreException, NoSuchProviderException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore ks = KeyStore.getInstance("JKS", "SUN");

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
        ks.load(in, keyStorePass.toCharArray());
        return ks;
    }


}
