package com.example.DukeStrategicTechnologies.pki.dto;

import java.util.Collection;
import java.util.List;

public class PossibleKeyUsagesDTO {
    private Collection<String> possibleKeyUsages;
    private Collection<String> possibleExtendedKeyUsages;

    public PossibleKeyUsagesDTO() {
    }

    public PossibleKeyUsagesDTO(Collection<String> possibleKeyUsages, Collection<String> possibleExtendedKeyUsages) {
        this.possibleKeyUsages = possibleKeyUsages;
        this.possibleExtendedKeyUsages = possibleExtendedKeyUsages;
    }

    public Collection<String> getPossibleKeyUsages() {
        return possibleKeyUsages;
    }

    public void setPossibleKeyUsages(Collection<String> possibleKeyUsages) {
        this.possibleKeyUsages = possibleKeyUsages;
    }

    public Collection<String> getPossibleExtendedKeyUsages() {
        return possibleExtendedKeyUsages;
    }

    public void setPossibleExtendedKeyUsages(Collection<String> possibleExtendedKeyUsages) {
        this.possibleExtendedKeyUsages = possibleExtendedKeyUsages;
    }
}
