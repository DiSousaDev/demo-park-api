package br.dev.diego.demoparkapi.web.dto;

public record UsuarioResponseDto(
        Long id,
        String username,
        String role
) {

    public void setRole(String role) {
    }

}
