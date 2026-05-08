package com.vitalys.backend.service;

import com.vitalys.backend.dto.UsuariosRequestDTO;
import com.vitalys.backend.dto.UsuariosResponseDTO;
import com.vitalys.backend.exception.ConflictException;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Usuarios;
import com.vitalys.backend.repository.UsuariosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    private void verificarDadosUnicos(UsuariosRequestDTO dto, Long idAtual) {
        boolean loginExiste = idAtual != null
                ? usuariosRepository.existsByLoginAndIdNot(dto.login(), idAtual)
                : usuariosRepository.existsByLogin(dto.login());
        if (loginExiste) throw new ConflictException("login", dto.login());

        boolean profissionalExiste = idAtual != null
                ? usuariosRepository.existsByIdProfissionalAndIdNot(dto.idProfissional(), idAtual)
                : usuariosRepository.existsByIdProfissional(dto.idProfissional());
        if (profissionalExiste) throw new ConflictException("id do profissional", String.valueOf(dto.idProfissional()));
    }

    public List<UsuariosResponseDTO> findAll() {
        return usuariosRepository.findAll().stream()
                .map(UsuariosResponseDTO::new)
                .toList();
    }

    public UsuariosResponseDTO registrar(UsuariosRequestDTO dto) {
        verificarDadosUnicos(dto, null);
        Usuarios u = new Usuarios();
        u.atualizarDados(dto);
        return new UsuariosResponseDTO(usuariosRepository.save(u));
    }

    public UsuariosResponseDTO editar(Long id, UsuariosRequestDTO dto) {
        Usuarios u = usuariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
        verificarDadosUnicos(dto, id);
        u.atualizarDados(dto);
        return new UsuariosResponseDTO(usuariosRepository.save(u));
    }

    public void deletar(Long id) {
        Usuarios u = usuariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
        usuariosRepository.delete(u);
    }
}
