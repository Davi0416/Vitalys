package com.vitalys.backend.service;

import com.vitalys.backend.dto.UsuariosRequestDTO;
import com.vitalys.backend.dto.UsuariosResponseDTO;
import com.vitalys.backend.exception.ConflictException;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Usuarios;
import com.vitalys.backend.repository.UsuariosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<UsuariosResponseDTO> findAll() {
        return usuariosRepository.findAll().stream()
                .map(UsuariosResponseDTO::new)
                .toList();
    }

    @Transactional
    public UsuariosResponseDTO registrar(UsuariosRequestDTO dto) {
        verificarDadosUnicos(dto, null);
        Usuarios u = Usuarios.builder()
                .login(dto.login())
                .senha(dto.senha())
                .cargo(dto.cargo())
                .idProfissional(dto.idProfissional())
                .ativo(dto.ativo())
                .build();
        return new UsuariosResponseDTO(usuariosRepository.save(u));
    }

    @Transactional
    public UsuariosResponseDTO editar(Long id, UsuariosRequestDTO dto) {
        Usuarios u = usuariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        verificarDadosUnicos(dto, id);
        u.atualizar(dto);
        return new UsuariosResponseDTO(usuariosRepository.save(u));
    }

    @Transactional
    public void deletar(Long id) {
        Usuarios u = usuariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        usuariosRepository.delete(u);
    }
}
