package com.vitalys.backend.controller;


import com.vitalys.backend.dto.ProfissionalRequestDTO;
import com.vitalys.backend.dto.ProfissionalResponseDTO;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.ProfissionalRepository;
import com.vitalys.backend.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/vitalys")
public class ProfissionalController {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ProfissionalService profissionalService;

    @PostMapping(path="/profissionais")
    public ResponseEntity<ProfissionalResponseDTO> registrar(@RequestBody @Validated ProfissionalRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalService.registrar(dto));
    }

    @GetMapping(path = "/profissionais")
    public @ResponseBody Iterable<Profissional> findAll(){
        return profissionalRepository.findAll();
    }

    @DeleteMapping(path = "/profissionais/{id}")
    public void deleteProfissional(@PathVariable Long id) {
       profissionalRepository.deleteById(id);
    }

    @PutMapping("/profissionais/{id}")
    public ResponseEntity<ProfissionalResponseDTO> updateProfissional(
            @PathVariable Long id,
            @RequestBody @Valid ProfissionalRequestDTO profissional) {
        return ResponseEntity.ok(profissionalService.editar(id, profissional));
    }
}
