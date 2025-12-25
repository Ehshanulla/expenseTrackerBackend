package com.controller;


import com.service.JWTService;
import com.service.RefreshTokenService;
import com.entities.RefreshToken;
import com.requests.AuthRequestDTO;
import com.requests.RefreshTokenRequestDTO;
import com.responses.JwtResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TokenController
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JWTService jwtService;

    @PostMapping("auth/v1/login")
    public ResponseEntity<?> AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        try {
            // This is the line that throws the exception upon failure
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword())
            );

            // If execution reaches here, authentication was successful
            if(authentication.isAuthenticated()){
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());

                // 200 OK Response with tokens
                return new ResponseEntity<>(JwtResponseDTO.builder()
                        .accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
                        .token(refreshToken.getToken())
                        .build(), HttpStatus.OK);

            } else {
                // This 'else' block is rarely reached if an exception isn't thrown first,
                // but is fine to keep as a fallback for internal issues.
                return new ResponseEntity<>("Authentication failed internally.", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (AuthenticationException e) {
            // 🚨 CATCH BLOCK: Spring Security throws an exception on failure

            // Log the specific failure for server-side debugging
            System.err.println("Authentication failed for user: " + authRequestDTO.getUsername());
            e.printStackTrace();

            // 401 Unauthorized Response for the client
            // Provide a general, non-specific error message for security
            return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("auth/v1/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser )
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUsername());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }

}
