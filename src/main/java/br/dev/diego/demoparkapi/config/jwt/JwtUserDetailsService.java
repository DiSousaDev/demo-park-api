package br.dev.diego.demoparkapi.config.jwt;

import br.dev.diego.demoparkapi.entity.Role;
import br.dev.diego.demoparkapi.entity.Usuario;
import br.dev.diego.demoparkapi.service.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;
    private final JwtUtils jwtUtils;

    public JwtUserDetailsService(UsuarioService usuarioService, JwtUtils jwtUtils) {
        this.usuarioService = usuarioService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(username);
        return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAuthenticated(String username) {
        Role role = usuarioService.buscarRolePorUsername(username);
        return jwtUtils.generateJwtToken(username, role.name().substring("ROLE_".length()));
    }

}
