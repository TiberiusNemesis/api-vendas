package com.example.suppliersservice.service;

import com.example.suppliersservice.client.ProductClient;
import com.example.suppliersservice.dto.ProductDto;
import com.example.suppliersservice.model.Supplier;
import com.example.suppliersservice.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    private final SupplierRepository repository;
    private final ProductClient productClient;

    public SupplierService(SupplierRepository repository, ProductClient productClient) {
        this.repository = repository;
        this.productClient = productClient;
    }

    public List<Supplier> findAll() { return repository.findAll(); }
    public Optional<Supplier> findById(Long id) { return repository.findById(id); }

    public Supplier create(Supplier supplier) {
        supplier.setId(null);
        return repository.save(supplier);
    }

    public List<ProductDto> findProducts() { return productClient.findAll(); }
}
