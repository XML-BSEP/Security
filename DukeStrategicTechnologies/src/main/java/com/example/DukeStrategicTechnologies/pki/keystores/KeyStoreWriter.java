package com.example.DukeStrategicTechnologies.pki.keystores;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class KeyStoreWriter {

    private KeyStore keyStore;

    public KeyStoreWriter() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void loadKeyStore(String fileName, char[] password) {
        try {
            if(fileName != null) {
                keyStore.load(new FileInputStream(fileName), password);
            } else {
                keyStore.load(null, password); //kreiranje novog keyStore-a
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeyStore(String fileName, char[] password) {
        try {
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void write(String alias, PrivateKey privateKey, String password, Certificate[] chain) {
        try {
            keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), chain);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }


}
