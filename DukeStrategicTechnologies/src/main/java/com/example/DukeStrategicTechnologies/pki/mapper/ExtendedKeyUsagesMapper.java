package com.example.DukeStrategicTechnologies.pki.mapper;

import com.example.DukeStrategicTechnologies.pki.dto.CreateCertificateDTO;
import com.example.DukeStrategicTechnologies.pki.model.KeyUsages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ExtendedKeyUsagesMapper {
    public static Collection<String> extendedKeyUsagesToStringCollection(Collection<String> extendedKeyUsages) {
        Collection<String> retVal = new ArrayList<>();

        HashMap<String, String> map = KeyUsages.getExtendedKeyUsageMapString();
        for(String usage : extendedKeyUsages) {
            if(map.containsKey(usage)) {
                retVal.add(map.get(usage));
            }
        }

        return retVal;
    }
}
