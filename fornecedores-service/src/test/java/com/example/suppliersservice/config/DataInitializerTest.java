package com.example.suppliersservice.config;

import com.example.suppliersservice.model.Supplier;
import com.example.suppliersservice.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(DataInitializer.class)
class DataInitializerTest {
    @Autowired SupplierRepository repository;

    @Test void seedsExactlyFiveDistinctSuppliers() {
        assertThat(repository.findAll()).hasSize(5).allSatisfy(supplier -> {
            assertThat(supplier.getId()).isPositive();
            assertThat(supplier.getName()).isNotBlank();
            assertThat(supplier.getTaxId()).isNotBlank();
        });
        assertThat(repository.findAll()).extracting(Supplier::getTaxId).doesNotHaveDuplicates();
        assertThat(repository.findAll()).extracting(Supplier::getName).doesNotHaveDuplicates();
    }
}
