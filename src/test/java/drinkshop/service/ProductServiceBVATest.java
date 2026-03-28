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

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BVA Tests for ProductService.addProduct")
@Tag("BVA")
class ProductServiceBVATest {

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
    @DisplayName("TC1_BVA: Produs valid - Boundary: name length 1")
    @Tag("valid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_ValidNameLength1() {
        Product p = new Product(1, "A", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(p);

        assertEquals(p, productService.findById(1));
    }

    @Test
    @DisplayName("TC2_BVA: Produs invalid - Boundary: name length 0")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_InvalidNameLength0() {
        Product p = new Product(2, "", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    @DisplayName("TC3_BVA: Produs valid - Boundary: price very small negative")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_ValidPriceSmallNegative() {
        Product p = new Product(3, "Limonada", -0.01, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    @DisplayName("TC4_BVA: Produs valid - Boundary: price very small positive")
    @Tag("valid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_ValidPriceSmallPositive() {
        Product p = new Product(4, "Limonada", 0.01, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(p);

        assertEquals(p, productService.findById(4));
    }

    @Test
    @DisplayName("TC5_BVA: Produs invalid - Boundary: price 0")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_InvalidPrice0() {
        Product p = new Product(5, "Limonada", 0.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }
}
