package com.example.suppliersservice.repository;

import com.example.suppliersservice.model.Supplier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class SupplierRepositoryTest {
    @Autowired SupplierRepository repository;

    @Test void generatesSupplierIds() {
        Supplier saved = repository.saveAndFlush(new Supplier("Northstar Supplies", "12345678000199"));
        assertThat(saved.getId()).isPositive();
        assertThat(repository.findById(saved.getId())).get().extracting(Supplier::getName).isEqualTo("Northstar Supplies");
    }

    @Test void rejectsDuplicateTaxIds() {
        repository.saveAndFlush(new Supplier("Northstar Supplies", "12345678000199"));
        assertThatThrownBy(() -> repository.saveAndFlush(new Supplier("Another Supplier", "12345678000199")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
