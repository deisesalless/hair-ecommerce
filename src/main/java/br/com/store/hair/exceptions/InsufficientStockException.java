package br.com.store.hair.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InsufficientStockException extends BusinessException {
    public InsufficientStockException(Long productId, int requested, int available) {
        super("Estoque insuficiente para o produto ID " + productId +
                        ". Solicitado: " + requested + ", Disponível: " + available,
                HttpStatus.UNPROCESSABLE_ENTITY  // 422
        );
    }
}
