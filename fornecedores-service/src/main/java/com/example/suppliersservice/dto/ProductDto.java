package com.example.suppliersservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record ProductDto(Long id, @JsonProperty("nome") String name, @JsonProperty("preco") BigDecimal price) {
}
