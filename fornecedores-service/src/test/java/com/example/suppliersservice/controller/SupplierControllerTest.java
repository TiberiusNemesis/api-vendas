package com.example.suppliersservice.controller;

import com.example.suppliersservice.dto.ProductDto;
import com.example.suppliersservice.model.Supplier;
import com.example.suppliersservice.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupplierController.class)
class SupplierControllerTest {
    @Autowired MockMvc mvc;
    @MockBean SupplierService service;

    private Supplier supplier() {
        Supplier supplier = new Supplier("Northstar Supplies", "12345678000199");
        supplier.setId(1L);
        return supplier;
    }

    @Test void listsSuppliersWithPublicFieldNames() throws Exception {
        when(service.findAll()).thenReturn(List.of(supplier()));
        mvc.perform(get("/fornecedores")).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$[0].nome").value("Northstar Supplies"))
                .andExpect(jsonPath("$[0].cnpj").value("12345678000199"))
                .andExpect(jsonPath("$[0].name").doesNotExist()).andExpect(jsonPath("$[0].taxId").doesNotExist());
    }

    @Test void looksUpSupplierById() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(supplier()));
        mvc.perform(get("/fornecedores/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
    }

    @Test void returnsNotFoundForMissingSupplier() throws Exception {
        when(service.findById(9L)).thenReturn(Optional.empty());
        mvc.perform(get("/fornecedores/9")).andExpect(status().isNotFound());
    }

    @Test void createsSupplierFromPublicPayload() throws Exception {
        when(service.create(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });
        mvc.perform(post("/fornecedores").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Northstar Supplies\",\"cnpj\":\"12345678000199\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Northstar Supplies"))
                .andExpect(jsonPath("$.cnpj").value("12345678000199"));
    }

    @Test void returnsConflictForDuplicateCnpj() throws Exception {
        when(service.create(any(Supplier.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate CNPJ"));
        mvc.perform(post("/fornecedores").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Northstar Supplies\",\"cnpj\":\"12345678000199\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("A supplier with this CNPJ already exists."));
    }

    @Test void rejectsBlankName() throws Exception {
        mvc.perform(post("/fornecedores").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\" \",\"cnpj\":\"12345678000199\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void rejectsBlankTaxId() throws Exception {
        mvc.perform(post("/fornecedores").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Northstar Supplies\",\"cnpj\":\"\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void forwardsProductsWithoutResolvingProductPathAsSupplierId() throws Exception {
        when(service.findProducts()).thenReturn(List.of(new ProductDto(2L, "Notebook", new BigDecimal("12.50"))));
        mvc.perform(get("/fornecedores/produtos")).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].nome").value("Notebook")).andExpect(jsonPath("$[0].preco").value(12.50))
                .andExpect(jsonPath("$[0].name").doesNotExist()).andExpect(jsonPath("$[0].price").doesNotExist());
    }
}
