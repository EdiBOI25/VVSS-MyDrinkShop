package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ECP Tests for ProductService.addProduct")
@Tag("ECP")
class ProductServiceECPTest {

    private ProductService productService;
    private Repository<Integer, Product> productRepo;

    @BeforeEach
    void setUp() {
        productRepo = new Repository<>() {
            private final List<Product> entities = new ArrayList<>();

            @Override
            public Product findOne(Integer id) {
                return entities.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
            }

            @Override
            public List<Product> findAll() {
                return entities;
            }

            @Override
            public Product save(Product entity) {
                entities.add(entity);
                return entity;
            }

            @Override
            public Product delete(Integer id) {
                Product p = findOne(id);
                if (p != null) entities.remove(p);
                return p;
            }

            @Override
            public Product update(Product entity) {
                delete(entity.getId());
                save(entity);
                return entity;
            }
        };

        productService = new ProductServiceImpl(productRepo);
    }

    @Test
    @DisplayName("TC1_ECP: Valid product - name='Limonada', price=10.5")
    @Tag("valid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_ValidNameAndPrice() {
        Product p = new Product(1, "Limonada", 10.5, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(p);

        assertEquals(p, productService.findById(1));
    }

    @Test
    @DisplayName("TC2_ECP: Invalid product - empty name, price=10.5")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_InvalidEmptyName() {
        Product p = new Product(2, "", 10.5, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    @DisplayName("TC3_ECP: Invalid product - null name, price=10.5")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_InvalidNullName() {
        Product p = new Product(3, null, 10.5, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    @DisplayName("TC4_ECP: Invalid product - name='Limonada', price=0")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_InvalidZeroPrice() {
        Product p = new Product(4, "Limonada", 0.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    @DisplayName("TC5_ECP: Invalid product - name='Limonada', price=-13.2")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_InvalidNegativePrice() {
        Product p = new Product(5, "Limonada", -13.2, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    @DisplayName("TC6_ECP: Invalid product - name='Limonada', price=NaN")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_InvalidNaNPrice() {
        Product p = new Product(6, "Limonada", Double.NaN, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }
}

