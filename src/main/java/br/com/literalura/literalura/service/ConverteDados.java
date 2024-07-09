package br.com.literalura.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ConverteDados implements IConverteDados{
    private final ObjectMapper objectMapper = new ObjectMapper();
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return objectMapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            System.out.println("Deu ruim");
            throw new RuntimeException(e);
        }
    }

}
