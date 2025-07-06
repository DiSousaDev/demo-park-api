package br.dev.diego.demoparkapi.repository;

import br.dev.diego.demoparkapi.entity.Role;
import br.dev.diego.demoparkapi.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    @Query("SELECT u.role FROM Usuario u WHERE u.username like :username")
    Role findRoleByUsername(String username);
}