package br.com.store.hair.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateBrandNameException extends BusinessException {
    public DuplicateBrandNameException(String name) {
        super("Já existe uma marca com o nome '" + name + "'", HttpStatus.CONFLICT);
    }
}
