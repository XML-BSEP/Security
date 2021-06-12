package com.example.DukeStrategicTechnologies.pki.mapper;

import com.example.DukeStrategicTechnologies.pki.dto.TemplateDTO;
import com.example.DukeStrategicTechnologies.pki.model.Account;
import com.example.DukeStrategicTechnologies.pki.model.Template;
import com.example.DukeStrategicTechnologies.pki.model.User;

import java.time.LocalDateTime;

public class TemplateMapper {

    public Template dtoToTemplate(TemplateDTO dto) {
        Account user = new Account();
        user.setId(dto.getUser());
        return new Template(dto.getId(), dto.getSignatureAlgorithm(), dto.getKeyAlgorithm(),
                dto.getKeyUsage(), dto.getExtendedKeyUsage(), dto.getName(), dto.getTimestamp(),user);
    }

    public TemplateDTO templateToDTO(Template template) {

        return new TemplateDTO(template.getId(), template.getSignatureAlgorithm(), template.getKeyAlgorithm(),
                template.getName(), template.getTimestamp(), template.getKeyUsage(), template.getExtendedKeyUsage(),template.getAccount().getId());
    }
}
