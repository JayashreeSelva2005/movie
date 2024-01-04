package com.springbootproject.movie.auth;
import com.springbootproject.movie.Model.User;
import com.springbootproject.movie.Model.Role;
import com.springbootproject.movie.config.JwtService;
import com.springbootproject.movie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

import static com.springbootproject.movie.auth.AuthenticationResponse.*;
@Service

public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

//    public AuthenticationResponse register(RegisterRequest request) {
//        try {
//            var user = User.builder()
//                    .firstname(request.getFirstname())
//                    .lastname(request.getLastname())
//                    .email(request.getEmail())
//                    .password(passwordEncoder.encode(request.getPassword()))
//                    .build();
//            repository.save(user);
//            var jwtToken = jwtService.generateToken(user);
//            return AuthenticationResponse.builder()
//                    .token(jwtToken)
//                    .build();
//        } catch (DataIntegrityViolationException ex) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    "Email address is already registered. Please use a different email address.",
//                    ex);
//        }
//    }



    public AuthenticationResponse register(RegisterRequest request) {
        try {
            var user = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            Set<Role> roles = new HashSet<>();


            if (isAdminUser(request)) {
                roles.add(Role.ADMIN);
            }else{
                roles.add(Role.USER);
            }

            user.setRoles(roles);
            repository.save(user);

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Email address is already registered. Please use a different email address.",
                    ex);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private boolean isAdminUser(RegisterRequest request) {
        return request.getEmail().equals("123@admin-domain.com");
    }
}

