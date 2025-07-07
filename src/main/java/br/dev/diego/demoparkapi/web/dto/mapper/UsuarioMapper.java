package br.dev.diego.demoparkapi.web.dto.mapper;

import br.dev.diego.demoparkapi.entity.Usuario;
import br.dev.diego.demoparkapi.web.dto.UsuarioCreateDto;
import br.dev.diego.demoparkapi.web.dto.UsuarioResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toUsuario(Usuario usuario);

    UsuarioResponseDto toDto(Usuario usuario);

    Usuario toUsuario(UsuarioCreateDto usuarioCreateDto);

    List<UsuarioResponseDto> toListDto(List<Usuario> usuarios);
}
