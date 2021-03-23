package com.example.DukeStrategicTechnologies.pki.mapper;

import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.model.KeyUsages;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class KeyUsagesMapper {

    public static Collection<Integer> keyUsagesDTOToKeyUsages(CreateCertificateDTO dto) {
        Collection<String> usages = dto.getKeyUsage();
        Collection<Integer> retVal = new ArrayList<>();

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
            if(keyUsages[i]) {
                retval.add(map.get(i));
            }
        }
        return retval;

    }
}
