package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("WBT Tests for ProductService.filterByPret")
@Tag("WBT")
class ProductServiceWBTTest {

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
                return new ArrayList<>(entities);
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

    private Product addToRepo(int id, double pret) {
        Product p = new Product(id, "Bautura" + id, pret, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productRepo.save(p);
        return p;
    }

    @Test
    @DisplayName("TC01_WBT: pretMinim=0, pretMaxim=0 -> returneaza toate produsele [P01, sc, dc(1=T), mcc_cond1=TT]")
    @Tag("valid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_BothZero_ReturnsAllProducts() {
        Product p = addToRepo(1, 10.0);

        List<Product> result = productService.filterByPret(0, 0);

        assertEquals(1, result.size());
        assertTrue(result.contains(p));
    }

    @Test
    @DisplayName("TC02_WBT: pretMinim=10 > pretMaxim=5 -> IllegalArgumentException [P02, dc(1=F,3=T), mcc_cond1=FF]")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_MinGreaterThanMax_ThrowsException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> productService.filterByPret(10, 5));
        assertTrue(ex.getMessage().contains("mai mare"));
    }

    @Test
    @DisplayName("TC03_WBT: pretMinim=-1, pretMaxim=5 -> IllegalArgumentException [P03, dc(3=F,5=T), mcc_cond5=TF]")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_NegativeMin_ThrowsException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> productService.filterByPret(-1, 5));
        assertTrue(ex.getMessage().contains("negativ"));
    }

    @Test
    @DisplayName("TC04_WBT: interval [5,15], repo gol -> lista vida [P04, dc(8=F), lc=0 iter]")
    @Tag("valid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_ValidRange_EmptyRepo_ReturnsEmpty() {
        List<Product> result = productService.filterByPret(5, 15);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("TC05_WBT: interval [50,100], 1 produs (pret=10) -> lista vida [P05, dc(9=F), mcc_cond9=FT, lc=1 iter]")
    @Tag("valid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_ValidRange_OneProduct_BelowMin() {
        addToRepo(1, 10.0);

        List<Product> result = productService.filterByPret(50, 100);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("TC06_WBT: interval [5,15], 1 produs (pret=10) -> [produs] [P06, dc(9=T), mcc_cond9=TT, lc=1 iter]")
    @Tag("valid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_ValidRange_OneProduct_InRange() {
        Product p = addToRepo(1, 10.0);

        List<Product> result = productService.filterByPret(5, 15);

        assertEquals(1, result.size());
        assertTrue(result.contains(p));
    }

    @Test
    @DisplayName("TC07_WBT: interval [5,20], 2 produse (pret=8, pret=15) -> ambele returnate [lc=2 iter]")
    @Tag("valid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_ValidRange_TwoProducts_BothInRange() {
        Product p1 = addToRepo(1, 8.0);
        Product p2 = addToRepo(2, 15.0);

        List<Product> result = productService.filterByPret(5, 20);

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
    }

    @Test
    @DisplayName("TC08_WBT: pretMinim=0, pretMaxim=5, produs (pret=3) -> [produs] [mcc_cond1=TF]")
    @Tag("valid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_MinZero_MaxNonZero_ProductInRange() {
        Product p = addToRepo(1, 3.0);

        List<Product> result = productService.filterByPret(0, 5);

        assertEquals(1, result.size());
        assertTrue(result.contains(p));
    }

    @Test
    @DisplayName("TC09_WBT: pretMinim=-2, pretMaxim=-1 -> IllegalArgumentException [mcc_cond5=TT]")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_BothNegative_ThrowsException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> productService.filterByPret(-2, -1));
        assertTrue(ex.getMessage().contains("negativ"));
    }

    // mcc_cond5=FT (pretMinim>=0, pretMaxim<0) -> nodul 3 intercepteaza intotdeauna inainte de nodul 5
    @Test
    @DisplayName("TC10_WBT: pretMinim=0, pretMaxim=-1 -> IllegalArgumentException min>max [mcc_cond5=FT infezabila]")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_MaxNegative_ThrowsMinGreaterException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> productService.filterByPret(0, -1));
        assertTrue(ex.getMessage().contains("mai mare"));
    }

    @Test
    @DisplayName("TC11_WBT: interval [5,15], 1 produs (pret=20) -> lista vida [mcc_cond9=TF]")
    @Tag("valid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_ValidRange_OneProduct_AboveMax() {
        addToRepo(1, 20.0);

        List<Product> result = productService.filterByPret(5, 15);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("TC12_WBT: pretMinim=5, pretMaxim=0 -> IllegalArgumentException [mcc_cond1=FT]")
    @Tag("invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void filterByPret_MinNonZero_MaxZero_ThrowsException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> productService.filterByPret(5, 0));
        assertTrue(ex.getMessage().contains("mai mare"));
    }
}
