package com.example.DukeStrategicTechnologies.pki.mapper;

import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.model.KeyUsages;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class KeyUsagesMapper {

    public static Collection<Integer> eyUsagesDTOToKeyUsages(CreateCertificateDTO dto) {
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
}
