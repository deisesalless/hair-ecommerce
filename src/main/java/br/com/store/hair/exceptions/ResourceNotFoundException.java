package br.com.store.hair.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " não encontrado(a) com ID: " + id, HttpStatus.NOT_FOUND);
    }
}
