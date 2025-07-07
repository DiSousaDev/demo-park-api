package br.dev.diego.demoparkapi.web.controller;

import br.dev.diego.demoparkapi.config.jwt.JwtToken;
import br.dev.diego.demoparkapi.config.jwt.JwtUserDetailsService;
import br.dev.diego.demoparkapi.web.dto.UsuarioLoginDto;
import br.dev.diego.demoparkapi.web.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutenticacaoController.class);

    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthenticationManager authenticationManager;

    public AutenticacaoController(JwtUserDetailsService jwtUserDetailsService, AuthenticationManager authenticationManager) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto usuarioLoginDto, HttpServletRequest request) {
        LOGGER.info("Iniciando autenticação para o usuário: " + usuarioLoginDto.username());

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    usuarioLoginDto.username(), usuarioLoginDto.password());
            authenticationManager.authenticate(authenticationToken);

            JwtToken jwtToken = jwtUserDetailsService.getTokenAuthenticated(usuarioLoginDto.username());
            LOGGER.info("Autenticação bem-sucedida para o usuário: {}", usuarioLoginDto.username());
            return ResponseEntity.ok(jwtToken);
        } catch (AuthenticationException e) {
            LOGGER.error("Falha na autenticação para o usuário: {} - {}", usuarioLoginDto.username(), e.getMessage());
        }
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Usuário ou senha inválidos"));
    }
}
