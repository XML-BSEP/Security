package com.example.DukeStrategicTechnologies.pki.model;

import org.bouncycastle.asn1.x500.X500Name;

import java.security.KeyPair;

public class Subject extends CertificateData {
    public Subject(X500Name x500Name, KeyPair keyPair) {
        super(x500Name, keyPair);
    }
}
