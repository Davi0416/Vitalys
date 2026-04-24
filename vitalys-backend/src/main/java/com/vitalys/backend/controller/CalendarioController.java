package com.vitalys.backend.controller;

import com.vitalys.backend.model.Calendario;
import com.vitalys.backend.repository.CalendarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/vitalys")
public class CalendarioController {

    @Autowired
    private CalendarioRepository calendarioRepository;

    @PostMapping(path = "/calendario")
    public Calendario addCalendario(@RequestBody Calendario calendario){
        return calendarioRepository.save(calendario);
    }

    @GetMapping(path = "/calendario")
    public @ResponseBody Iterable<Calendario> findAllCalendario()
    {
        return calendarioRepository.findAll();
    }
}
