package com.vitalys.backend.service.rules;

import com.vitalys.backend.exception.ConflictException;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@Component
public final class UniqueFieldValidator {

    public void validar(
            String campo,
            String valor,
            Long idAtual,
            Supplier<Boolean> checkExiste,
            BiFunction<String, Long, Boolean> checkExisteExcluindo
    ) {
        boolean existe = idAtual != null
                ? checkExisteExcluindo.apply(valor, idAtual)
                : checkExiste.get();

        if (existe) throw new ConflictException(campo, valor);
    }
}
