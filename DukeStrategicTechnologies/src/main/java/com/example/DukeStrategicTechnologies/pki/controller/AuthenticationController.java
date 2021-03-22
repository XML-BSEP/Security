package com.example.DukeStrategicTechnologies.pki.controller;

import com.example.DukeStrategicTechnologies.pki.dto.AuthenticatedUserDTO;
import com.example.DukeStrategicTechnologies.pki.model.Account;
import com.example.DukeStrategicTechnologies.pki.security.TokenUtils;
import com.example.DukeStrategicTechnologies.pki.security.auth.JwtAuthenticationRequest;
import com.example.DukeStrategicTechnologies.pki.security.domain.UserTokenState;
import com.example.DukeStrategicTechnologies.pki.service.CustomDetailUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomDetailUserService userDetailsService;



    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
                                                       HttpServletResponse response) {
        AuthenticatedUserDTO authenticatedUserDTO = new AuthenticatedUserDTO();

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));

            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            SecurityContextHolder.setContext(ctx);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Account account = (Account) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(account.getEmail());
            int expiresIn = tokenUtils.getExpiredIn();
            authenticatedUserDTO = new AuthenticatedUserDTO(account.getId(), account.getRole(), account.getEmail(), new UserTokenState(jwt, expiresIn));

            return new ResponseEntity<>(authenticatedUserDTO, HttpStatus.OK);

    }



}
