package com.vitalys.backend.service;

import com.vitalys.backend.dto.UsuariosRequestDTO;
import com.vitalys.backend.dto.UsuariosResponseDTO;
import com.vitalys.backend.model.Usuarios;
import com.vitalys.backend.repository.UsuariosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    public List<UsuariosResponseDTO> findAll() {
        return usuariosRepository.findAll().stream()
                .map(UsuariosResponseDTO::new)
                .collect(Collectors.toList());
    }

    public UsuariosResponseDTO registrar(UsuariosRequestDTO dto) {
        Usuarios u = new Usuarios();
        u.setLogin(dto.login());
        u.setSenha(dto.senha());
        u.setIdCargo(dto.idCargo());
        u.setIdProfissional(dto.idProfissional());
        u.setAtivo(dto.ativo());
        return new UsuariosResponseDTO(usuariosRepository.save(u));
    }

    public UsuariosResponseDTO editar(Long id, UsuariosRequestDTO dto) {
        Usuarios existente = usuariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        existente.setLogin(dto.login());
        existente.setSenha(dto.senha());
        existente.setIdCargo(dto.idCargo());
        existente.setIdProfissional(dto.idProfissional());
        existente.setAtivo(dto.ativo());
        return new UsuariosResponseDTO(usuariosRepository.save(existente));
    }
}
