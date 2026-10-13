package com.example.suppliersservice.controller;

import com.example.suppliersservice.dto.ProductDto;
import com.example.suppliersservice.model.Supplier;
import com.example.suppliersservice.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class SupplierController {
    private final SupplierService service;

    public SupplierController(SupplierService service) {
        this.service = service;
    }

    @GetMapping
    public List<Supplier> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> findById(@PathVariable Long id) {
        return ResponseEntity.of(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Supplier> create(@Valid @RequestBody Supplier supplier) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(supplier));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDuplicateCnpj(DataIntegrityViolationException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "A supplier with this CNPJ already exists.");
    }

    @GetMapping("/produtos")
    public List<ProductDto> findProducts() { return service.findProducts(); }
}
