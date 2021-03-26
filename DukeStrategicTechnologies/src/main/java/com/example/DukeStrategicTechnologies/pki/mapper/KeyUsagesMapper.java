package com.example.DukeStrategicTechnologies.pki.mapper;

import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.model.KeyUsages;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class KeyUsagesMapper {

    public static Collection<Integer> keyUsagesDTOToKeyUsages(CreateCertificateDTO dto) {

        Collection<String> usages = dto.getKeyUsage();
        Collection<Integer> retVal = new ArrayList<>();

        if (dto.getKeyUsage() == null) {
            return retVal;
        }
        HashMap<String, Integer> map = KeyUsages.getKeyUsageMap();
        for(String usage : usages) {
            if(map.containsKey(usage)) {
                retVal.add(map.get(usage));
            }
        }

        return retVal;
    }

    public static Collection<String> keyUsagesBoolToKeyUsagesString(boolean[] keyUsages) {
        Collection<String> retval = new ArrayList<>();

        HashMap<Integer, String> map = KeyUsages.getKeyUsageMapString();

        for (int i = 0; i < keyUsages.length; i++) {
            if (keyUsages[i]) {
                retval.add(map.get(i));
            }
        }
        return retval;
    }
    public static KeyPurposeId[] extendedKeyUsagesDTOToValues(CreateCertificateDTO createCertificateDTO) {
        ArrayList<String> selectedExtendedKeys = new ArrayList<>();
        ArrayList<KeyPurposeId> extendedKeyUsageValues = new ArrayList<>();
        HashMap<String, KeyPurposeId> extendedKeyMap = KeyUsages.getExtendedKeyUsageMap();

        if (createCertificateDTO.getExtendedKeyUsage() == null) {
            KeyPurposeId[] extendedKeyUsages = new KeyPurposeId[extendedKeyUsageValues.size()];
            extendedKeyUsageValues.toArray(extendedKeyUsages);
            return  extendedKeyUsages;
        }
        for (String s : createCertificateDTO.getExtendedKeyUsage()) {
            if (extendedKeyMap.containsKey(s)) {
                extendedKeyUsageValues.add(extendedKeyMap.get(s));
            }
        }

        KeyPurposeId[] extendedKeyUsages = new KeyPurposeId[extendedKeyUsageValues.size()];
        extendedKeyUsageValues.toArray(extendedKeyUsages);
        return extendedKeyUsages;
    }
}
