package com.example.DukeStrategicTechnologies.pki.model;


import org.bouncycastle.asn1.x500.X500Name;

import java.security.KeyPair;

public class Issuer extends CertificateData{
    public Issuer(X500Name x500Name, KeyPair keyPair) {
        super(x500Name, keyPair);
    }
}
