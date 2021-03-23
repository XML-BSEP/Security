package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.TemplateDTO;
import com.example.DukeStrategicTechnologies.pki.mapper.TemplateMapper;
import com.example.DukeStrategicTechnologies.pki.model.Template;
import com.example.DukeStrategicTechnologies.pki.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;
    private TemplateMapper templateMapper;

    public TemplateService(){
        templateMapper = new TemplateMapper();

    }

    public void createTemplate(TemplateDTO templateDTO) {

        Template template = templateMapper.dtoToTemplate(templateDTO);
        templateRepository.save(template);

    }

    public List<TemplateDTO> getAllTemplates(){

        List<Template> templates = templateRepository.findAll();
        List<TemplateDTO> dtoTemplates = new ArrayList<>();

        for (Template template : templates) {
            dtoTemplates.add(templateMapper.templateToDTO(template));
        }
        return dtoTemplates;
    }


}
