package com.vitalys.backend.controller;

import com.vitalys.backend.dto.LoginRequestDTO;
import com.vitalys.backend.infra.security.TokenService;
import com.vitalys.backend.model.Usuarios;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/vitalys")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO dto) throws AuthenticationException {
        var credenciais = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
        var auth = authenticationManager.authenticate(credenciais);
        var tokenJWT = tokenService.gerarToken((Usuarios) auth.getPrincipal());

        return ResponseEntity.ok(tokenJWT);
    }
}
