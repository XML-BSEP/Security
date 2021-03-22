package com.example.DukeStrategicTechnologies.pki.model;

import org.bouncycastle.asn1.x509.KeyUsage;

import java.util.HashMap;

public class KeyUsages {

    public static HashMap<String, Integer> getKeyUsageMap() {
        HashMap<String, Integer> keyUsageMap = new HashMap<>();

        keyUsageMap.put("digitalSignature", KeyUsage.digitalSignature);
        keyUsageMap.put("nonRepudiation", KeyUsage.nonRepudiation);
        keyUsageMap.put("keyEncipherment", KeyUsage.keyEncipherment);
        keyUsageMap.put("dataEncipherment", KeyUsage.dataEncipherment);
        keyUsageMap.put("keyAgreement", KeyUsage.keyAgreement);
        keyUsageMap.put("certificateSigning", KeyUsage.keyCertSign);
        keyUsageMap.put("crlSigning", KeyUsage.cRLSign);
        keyUsageMap.put("encipherOnly", KeyUsage.encipherOnly);
        keyUsageMap.put("decipherOnly", KeyUsage.decipherOnly);

        return keyUsageMap;
    }


}
