package com.example.DukeStrategicTechnologies.pki.model;

import org.bouncycastle.asn1.x509.KeyPurposeId;
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


    public static HashMap<String, KeyPurposeId> getExtendedKeyUsageMap() {
        HashMap<String, KeyPurposeId> extendedKeyUsageMap = new HashMap<>();

        extendedKeyUsageMap.put("serverAuth", KeyPurposeId.id_kp_serverAuth);
        extendedKeyUsageMap.put("clientAuth", KeyPurposeId.id_kp_clientAuth);
        extendedKeyUsageMap.put("signExecCode", KeyPurposeId.id_kp_codeSigning);
        extendedKeyUsageMap.put("emailProtection", KeyPurposeId.id_kp_emailProtection);
        extendedKeyUsageMap.put("ipsecEndSystem", KeyPurposeId.id_kp_ipsecEndSystem);
        extendedKeyUsageMap.put("ipsecTunnel", KeyPurposeId.id_kp_ipsecTunnel);
        extendedKeyUsageMap.put("ipsecUser", KeyPurposeId.id_kp_ipsecUser);
        extendedKeyUsageMap.put("timeStamping", KeyPurposeId.id_kp_timeStamping);
        extendedKeyUsageMap.put("ocspSigning", KeyPurposeId.id_kp_OCSPSigning);

        return extendedKeyUsageMap;
    }
}
