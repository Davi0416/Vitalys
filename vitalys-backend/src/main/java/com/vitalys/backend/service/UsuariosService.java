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

    private void verificarDadosUnicos(UsuariosRequestDTO dto) {
        if (usuariosRepository.existsByIdProfissional(dto.idProfissional())){
            throw new ConflictException("id do profissional", String.valueOf(dto.idProfissional()));
        }
        if (usuariosRepository.existsByLogin(dto.login())){
            throw new ConflictException("login", dto.login());
        }
    }

    public List<UsuariosResponseDTO> findAll() {
        return usuariosRepository.findAll().stream()
                .map(UsuariosResponseDTO::new)
                .toList();
    }

    public UsuariosResponseDTO registrar(UsuariosRequestDTO dto) {
        Usuarios u = new Usuarios();
        verificarDadosUnicos(dto);
        u.atualizarDados(dto);
        return new UsuariosResponseDTO(usuariosRepository.save(u));
    }

    public UsuariosResponseDTO editar(Long id, UsuariosRequestDTO dto) {
        Usuarios u = usuariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
        verificarDadosUnicos(dto);
        u.atualizarDados(dto);
        return new UsuariosResponseDTO(usuariosRepository.save(u));
    }

    public void deletar(Long id){
        Usuarios u = usuariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
        usuariosRepository.delete(u);
    }
}
