package com.example.suppliersservice.service;

import com.example.suppliersservice.client.ProductClient;
import com.example.suppliersservice.dto.ProductDto;
import com.example.suppliersservice.model.Supplier;
import com.example.suppliersservice.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SupplierServiceTest {
    SupplierRepository repository;
    ProductClient client;
    SupplierService service;

    @BeforeEach void setUp() {
        repository = mock(SupplierRepository.class);
        client = mock(ProductClient.class);
        service = new SupplierService(repository, client);
    }

    @Test void clearsCallerIdBeforeSavingNewSupplier() {
        Supplier supplier = new Supplier("Northstar Supplies", "12345678000199");
        supplier.setId(99L);
        when(repository.save(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier saved = invocation.getArgument(0);
            assertThat(saved.getId()).isNull();
            saved.setId(1L);
            return saved;
        });
        Supplier created = service.create(supplier);
        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getName()).isEqualTo("Northstar Supplies");
        assertThat(created.getTaxId()).isEqualTo("12345678000199");
    }

    @Test void forwardsProductListFromProductClient() {
        when(client.findAll()).thenReturn(List.of(new ProductDto(3L, "Paper", new BigDecimal("4.75"))));
        assertThat(service.findProducts()).singleElement().satisfies(product -> {
            assertThat(product.id()).isEqualTo(3L);
            assertThat(product.name()).isEqualTo("Paper");
            assertThat(product.price()).isEqualByComparingTo("4.75");
        });
        verify(client).findAll();
    }

    @Test void returnsStoredSuppliers() {
        when(repository.findAll()).thenReturn(List.of(new Supplier("Northstar Supplies", "12345678000199")));
        assertThat(service.findAll()).singleElement().extracting(Supplier::getName).isEqualTo("Northstar Supplies");
    }

    @Test void looksUpStoredSupplierById() {
        Supplier supplier = new Supplier("Northstar Supplies", "12345678000199");
        when(repository.findById(7L)).thenReturn(Optional.of(supplier));
        assertThat(service.findById(7L)).contains(supplier);
        assertThat(service.findById(8L)).isEmpty();
    }
}
