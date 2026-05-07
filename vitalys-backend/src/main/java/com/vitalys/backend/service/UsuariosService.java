package com.vitalys.backend.service;

import com.vitalys.backend.dto.UsuariosRequestDTO;
import com.vitalys.backend.dto.UsuariosResponseDTO;
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

    public List<UsuariosResponseDTO> findAll() {
        return usuariosRepository.findAll().stream()
                .map(UsuariosResponseDTO::new)
                .toList();
    }

    public UsuariosResponseDTO registrar(UsuariosRequestDTO dto) {
        Usuarios u = new Usuarios();
        u.atualizarDados(dto);
        return new UsuariosResponseDTO(usuariosRepository.save(u));
    }

    public UsuariosResponseDTO editar(Long id, UsuariosRequestDTO dto) {
        Usuarios existente = usuariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
        existente.atualizarDados(dto);
        return new UsuariosResponseDTO(usuariosRepository.save(existente));
    }

    public void deletar(Long id){
        Usuarios existente = usuariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
        usuariosRepository.delete(existente);
    }
}
