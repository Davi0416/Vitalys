package com.vitalys.backend.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String tipoInfo, String info) {
        super("O " + tipoInfo + " " + info + " já está cadastrado no sistema");
    }
}
