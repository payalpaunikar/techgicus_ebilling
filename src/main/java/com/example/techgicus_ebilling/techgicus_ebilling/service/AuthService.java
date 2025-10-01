package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Role;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.User;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.forgotPasswordDto.ForgotPasswordRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.loginDto.LoginRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.loginDto.LoginResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.registrationDto.RegistrationRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.registrationDto.RegistrationResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.UserAlreadyRegisterException;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.RoleRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.UserRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.security.JwtService;
import com.example.techgicus_ebilling.techgicus_ebilling.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

        private UserRepository userRepository;
        private PasswordEncoder passwordEncoder;
        private JwtService jwtService;
        private RoleRepository roleRepository;
        private RefreshTokenService refreshTokenService;
        private AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RoleRepository roleRepository, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }


    public RegistrationResponse registerAdmin(RegistrationRequest registrationRequest){

        if (userRepository.findByEmail(registrationRequest.getEmailId()).isPresent()){
            throw new UserAlreadyRegisterException("User alredy exit with email id : "+registrationRequest.getEmailId());
        }

        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseThrow(()-> new ResourceNotFoundException("Role with admin role name is not found"));

        User newUser = new User();
        newUser.setUserName(registrationRequest.getUserName());
        newUser.setEmail(registrationRequest.getEmailId());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdateAt(LocalDateTime.now());
        newUser.setRoles(Set.of(adminRole));

        User saveUser = userRepository.save(newUser);

        //generate access & refresh token
        String accessToken = jwtService.generateAccessToken(saveUser);
        String refreshToken = jwtService.generateRefreshToken(saveUser);


        //Stored refresh token in DB
        refreshTokenService.saveRefreshToken(saveUser,refreshToken);

        //Return the token with user data to frontend
        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setUserId(saveUser.getUserId());
        registrationResponse.setUserName(saveUser.getUserName());
        registrationResponse.setEmailId(saveUser.getEmail());
        registrationResponse.setRoleName(saveUser.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));
        registrationResponse.setAccessToken(accessToken);
        registrationResponse.setRefreshToken(refreshToken);

        return registrationResponse;
    }


    public LoginResponse login(LoginRequest loginRequest){

        //Authenticated the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmailId(),loginRequest.getPassword())
        );

        if (authentication.isAuthenticated()){

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            //Generated access token and refresh token
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);


            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUserId(user.getUserId());
            loginResponse.setEmailId(user.getEmail());
            loginResponse.setUserName(user.getUserName());
            loginResponse.setAccessToken(accessToken);
            loginResponse.setRefreshToken(refreshToken);
            loginResponse.setRoleName(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));

            return loginResponse;
        }

        throw new RuntimeException("Invalid email or password");
    }

    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest){
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmailId())
                .orElseThrow(()-> new ResourceNotFoundException("User with "+forgotPasswordRequest.getEmailId()+" email is not found "));

        user.setPassword(passwordEncoder.encode(forgotPasswordRequest.getNewPassword()));

        userRepository.save(user);

        return "Password changed successfully. Please log in with your new password.";
    }

}
