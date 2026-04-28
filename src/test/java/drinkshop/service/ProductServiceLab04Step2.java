package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Step 2: integration testing S + V (validator real, mock E + R)")
@Tag("IntegrationTesting")
class ProductServiceLab04Step2 {

    @Mock
    private Repository<Integer, Product> productRepo;

    @Mock
    private Product productMock;

    private ProductService productService;

    // aici nu mai merge injectmocks ca avem validator real
    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepo, new ProductValidator());
    }

    @Test
    @DisplayName("addProduct - produs mocked valid, validatorul real merge, save() e apelat")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_ValidMockedProduct_PassesRealValidator() {
        when(productMock.getId()).thenReturn(1);
        when(productMock.getNume()).thenReturn("Limonada");
        when(productMock.getPret()).thenReturn(10.5);

        productService.addProduct(productMock);

        verify(productRepo, times(1)).save(productMock);
    }

    @Test
    @DisplayName("addProduct - produs mocked invalid cu nume gol, validatorul real arunca exceptie, save() nu mai e apelat")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_BlankName_RealValidatorThrows() {
        when(productMock.getId()).thenReturn(1);
        when(productMock.getNume()).thenReturn("");
        when(productMock.getPret()).thenReturn(10.5);

        assertThrows(ValidationException.class, () -> productService.addProduct(productMock));

        verify(productRepo, never()).save(any());
    }

    @Test
    @DisplayName("addProduct - produs mocked invalid cu pret negativ, validatorul real arunca exceptie, save() nu mai e apelat")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_NegativePrice_RealValidatorThrows() {
        when(productMock.getId()).thenReturn(1);
        when(productMock.getNume()).thenReturn("Limonada");
        when(productMock.getPret()).thenReturn(-5.0);

        assertThrows(ValidationException.class, () -> productService.addProduct(productMock));

        verify(productRepo, never()).save(any());
    }
}
