package com.example.suppliersservice.config;

import com.example.suppliersservice.model.Supplier;
import com.example.suppliersservice.repository.SupplierRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final SupplierRepository repository;

    public DataInitializer(SupplierRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        repository.saveAll(List.of(
                new Supplier("Northstar Supplies", "12345678000199"),
                new Supplier("Cedar Wholesale", "23456789000188"),
                new Supplier("Summit Industrial", "34567890000177"),
                new Supplier("Harbor Distribution", "45678901000166"),
                new Supplier("Orion Materials", "56789012000155")
        ));
    }
}
