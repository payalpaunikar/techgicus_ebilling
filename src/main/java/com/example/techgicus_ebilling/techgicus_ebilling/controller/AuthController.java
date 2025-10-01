package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.User;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.errorDto.ErrorResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.forgotPasswordDto.ForgotPasswordRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.loginDto.LoginRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.loginDto.LoginResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.refreshTokenDto.RefreshTokenRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.refreshTokenDto.RefreshTokenResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.registrationDto.RegistrationRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.registrationDto.RegistrationResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.InvalidTokenException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.UserAlreadyRegisterException;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.UserRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.security.JwtService;
import com.example.techgicus_ebilling.techgicus_ebilling.security.MyUserDetails;
import com.example.techgicus_ebilling.techgicus_ebilling.security.MyUserDetailsService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authetication API", description = "All Api Which need for login and registration")
public class AuthController {

    private AuthService authService;

    private JwtService jwtService;

    private MyUserDetailsService myUserDetailsService;


    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService,JwtService jwtService,MyUserDetailsService myUserDetailsService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
    }


    @Operation(summary = "Public endpoint", description = "This endpoint is accessible without authentication",operationId = "just test")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "This is just a testing api for check the setup of application",
                    content = @Content(
                            mediaType = "application/json"
                           // schema = @Schema(implementation = dto.class)
                    )
            )
    })
    @GetMapping("/test")
    public String test(){
        return "Application is running";
    }



    @Operation(summary = "Admin Registration Api",description = "This api is used for admin registration")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Admin register succefully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegistrationResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Admin is already register,Please login",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)

                    )
            )
    })
    @PostMapping("/auth/register/admin")
    public ResponseEntity<RegistrationResponse> registerAdmin(@RequestBody RegistrationRequest registrationRequest){
        RegistrationResponse response = authService.registerAdmin(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @Operation(summary = "Login Api",description = "This is a login api for every user in the application")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            description = "Login succeufully.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoginResponse.class)
            )
    ),
            @ApiResponse(responseCode = "401", description = "Unauthorized user first register yourself",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }



    @Operation(summary = "forgot password request",description = "When we forget our password this api is help")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset request successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Password changed successfully. Please log in with your new password.")
                    )

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Invalid request (e.g., email not registered)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )

    })
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        return ResponseEntity.ok(authService.forgotPassword(forgotPasswordRequest));
    }



//    // This is just for the checking the token
//    @PreAuthorize("hasAuthority('USER_CREATE')")
//    @GetMapping("/just/test")
//    public String userCreatedFroTest(){
//        return "User created succefully";
//    }



    @Operation(summary = "get new access token",
            description = "This api is used when we need new access token from refresh token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get new access token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenResponse.class)
                    )
            ),
            @ApiResponse(
                 responseCode = "401",
                    description = "Refresh token is invalid (Unauthorized user)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request (missing or empty token)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/get/access-token/by/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest, Authentication authentication){
        String refreshToken = refreshTokenRequest.getRefreshToken();
        String emailId;

        //exctract email id from the refresh token
        try {
            emailId = jwtService.extractEmailId(refreshToken);
        }
        catch (ExpiredJwtException exception){
            throw new InvalidTokenException("Refresh token has expired. Please login again.",HttpStatus.UNAUTHORIZED);
        }catch (MalformedJwtException exception){
            throw new InvalidTokenException("Invalid refresh token format.",HttpStatus.BAD_REQUEST);
        }
        catch (SignatureException exception){
            throw new InvalidTokenException("Invalid refresh token signature.",HttpStatus.UNAUTHORIZED);
        }
        catch (IllegalArgumentException e) {
            throw new InvalidTokenException("Refresh token is empty or null.",HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            throw new InvalidTokenException("Could not validate refresh token.",HttpStatus.BAD_REQUEST);
        }

        MyUserDetails userDetails = (MyUserDetails) myUserDetailsService.loadUserByUsername(emailId);
        // if refresh token is valid
        Boolean isRefreshTokenValid =  userDetails.getUsername().equals(emailId) && !jwtService.isTokenExpired(refreshToken);

        logger.info("Is refersh token is valid : "+isRefreshTokenValid);

       // Boolean isTokenTypeIsRefresh = "refresh".equalsIgnoreCase(jwtService.extractTokenType(refreshToken));

        Boolean isTokenTypeIsRefresh = jwtService.isRefreshToken(refreshToken);

        logger.info("Is token type is refresh : "+isTokenTypeIsRefresh);

        // Validate token type and expiration
        if (!isRefreshTokenValid ||
                !isTokenTypeIsRefresh){
            logger.info("token is invalid or token type is not refresh");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //Generated new access token
        String newAccessToken = jwtService.generateAccessToken(userDetails.getUser());

        return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken));
    }

}
