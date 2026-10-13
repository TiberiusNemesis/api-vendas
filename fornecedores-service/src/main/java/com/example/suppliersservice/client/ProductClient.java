package com.example.suppliersservice.client;

import com.example.suppliersservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name = "produtos-service")
public interface ProductClient {
    @GetMapping("/produtos")
    List<ProductDto> findAll();
}
