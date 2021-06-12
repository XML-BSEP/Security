package com.example.DukeStrategicTechnologies.pki.helpers;

import com.example.DukeStrategicTechnologies.pki.dto.*;
import com.example.DukeStrategicTechnologies.pki.helpers.validatorErrors.StringInputException;
import com.example.DukeStrategicTechnologies.pki.model.enums.SignatureAlgorithm;

import java.util.Collection;

public class Validator {
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z0-9\\d$@$!%*?&].{7,}$";
    public static final String EMAIL = "^(.+)@(.+)$";

    public static void validateStringInput(String input, String fieldName) throws StringInputException {
        if(input.equals("")){
            throw new StringInputException("Empty " + fieldName + " field");
        }
    }
    public static void validateLongInput(Long input, String fieldName) throws StringInputException {
        if(input==null){
            throw new StringInputException("Empty " + fieldName + " field");
        }
    }
    public static void validatePassword(String password) throws StringInputException{
        if (!password.matches(PASSWORD))
            throw new StringInputException("Password in wrong format");
    }

    public static void validateEmail(String email) throws StringInputException{
        if (!email.matches(EMAIL))
            throw new StringInputException("Email in wrong format");
    }

    public static void validatePasswordResetForm(PasswordResetDTO dto) throws StringInputException{
        validateStringInput(dto.getCode(), "code");
        validatePassword(dto.getPassword());
        validatePassword(dto.getConfirmedpassword());
        validateEmail(dto.getEmail());
    }

    public static void validateRegistrationForm(NewUserDTO dto) throws StringInputException{
        validateStringInput(dto.getName(), "name");
        validateStringInput(dto.getSurname(), "surname");
        validateStringInput(dto.getCommonName(), "common name");
        validateStringInput(dto.getOrganization(), "organization");
        validateStringInput(dto.getOrganizationUnit(), "organization unit");
        validateStringInput(dto.getState(), "state");
        validateStringInput(dto.getCity(), "city");
        validatePassword(dto.getPassword());
        validatePassword(dto.getConfirmPassword());
        validateEmail(dto.getEmail());
    }

    public static void validateSaveUserForm(UserDTO dto) throws StringInputException{
        validateStringInput(dto.getGivenName(), "name");
        validateStringInput(dto.getSurname(), "surname");
        validateStringInput(dto.getCommonName(), "common name");
        validateStringInput(dto.getOrganization(), "organization");
        validateStringInput(dto.getOrganizationUnit(), "organization unit");
        validateStringInput(dto.getState(), "state");
        validateStringInput(dto.getCity(), "city");
        validatePassword(dto.getPassword());
        validateEmail(dto.getEmail());
    }

    public static void validateResendCodeForm(ResendCodeDTO dto) throws StringInputException{
        validateEmail(dto.getEmail());
    }
    public static void validateActivationForm(ActivationDTO dto) throws StringInputException{
        validateStringInput(dto.getCode(), "code");
        validateEmail(dto.getEmail());
    }
    public static void validateCreateCertificateForm(CreateCertificateDTO dto) throws StringInputException{
        validateLongInput(dto.getSubjectId(), "subjectid");
        validateLongInput(dto.getIssuerId(), "issuedId");
        validateStringInput(dto.getStartDate(), "start date");
        validateStringInput(dto.getEndDate(), "end date");
        for (String keyUsage : dto.getKeyUsage()){
            validateStringInput(keyUsage,"keyUsage");
        }
        for (String extendedKeyUsage : dto.getExtendedKeyUsage()){
            validateStringInput(extendedKeyUsage,"extendedKeyUsage");
        }
        validateStringInput(dto.getIssuerSerialNumber(), "issuer serial number");

    }
}

