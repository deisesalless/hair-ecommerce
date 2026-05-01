package br.com.store.hair.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String path;
    private Map<String, String> errors;
}
