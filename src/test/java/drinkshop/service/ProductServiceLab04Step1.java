package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Step 1: testare unitara S (mock V + mock R)")
@Tag("UnitTesting")
class ProductServiceLab04Step1 {

    @Mock
    private Repository<Integer, Product> productRepo;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("addProduct - produs valid: validate() si save() apelate")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_Valid_DelegatesToValidatorAndRepo() {
        Product p = new Product(1, "Limonada", 10.5, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(p);

        verify(productValidator, times(1)).validate(p);
        verify(productRepo, times(1)).save(p);
    }

    @Test
    @DisplayName("addProduct - produs invalid: validator arunca exceptie, save() nu mai e apelat")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_Invalid_ValidatorThrows_RepoNeverSaves() {
        Product p = new Product(2, "", 10.5, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        doThrow(new ValidationException("Numele nu poate fi gol!")).when(productValidator).validate(p);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));

        verify(productValidator, times(1)).validate(p);
        verify(productRepo, never()).save(any());
    }

    @Test
    @DisplayName("findById care merge")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void findById_ReturnsProductFromRepo() {
        Product p = new Product(1, "Limonada", 10.5, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        when(productRepo.findOne(1)).thenReturn(p);

        Product result = productService.findById(1);

        assertEquals(p, result);
        verify(productRepo, times(1)).findOne(1);
    }
}
