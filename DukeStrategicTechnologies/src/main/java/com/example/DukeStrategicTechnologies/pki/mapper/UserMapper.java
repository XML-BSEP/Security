package com.example.DukeStrategicTechnologies.pki.mapper;

import com.example.DukeStrategicTechnologies.pki.dto.TemplateDTO;
import com.example.DukeStrategicTechnologies.pki.dto.UserDTO;
import com.example.DukeStrategicTechnologies.pki.model.Template;
import com.example.DukeStrategicTechnologies.pki.model.User;

public class UserMapper {
    /*
    public User dtoToUser(UserDTO dto) {

        return new User(dto.getId(), dto.getGivenName(), dto.getSurname(),
                dto.getCommonName(), dto.getOrganization(), dto.getOrganizationUnit(), dto.getState(), dto.getCity(), dto.getEmail(), dto.isCA(), dto.getCertificateCount());
    }
    */
    public static UserDTO userToDTO(User user) {

        return new UserDTO(user.getId(), user.getGivenName(), user.getSurname(),
                user.getCommonName(), user.getOrganization(), user.getOrganizationUnit(), user.getState(), user.getCity(), user.getEmail(), "", user.isCA(), user.getCertificateCount());
    }
}
