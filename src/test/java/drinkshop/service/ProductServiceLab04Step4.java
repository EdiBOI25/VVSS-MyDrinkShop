package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Step 4: integration testing S + V + E + R (real repository)")
@Tag("IntegrationTesting")
class ProductServiceLab04Step4 {

    @TempDir
    Path tempDir;

    private Repository<Integer, Product> productRepo;
    private ProductService productService;

    @BeforeEach
    void setUp() throws Exception {
        Path productsFile = tempDir.resolve("products-step4.txt");
        Files.createFile(productsFile);
        productRepo = new FileProductRepository(productsFile.toString());
        productService = new ProductServiceImpl(productRepo);
    }

    @Test
    @DisplayName("addProduct - produs real valid, repo real salveaza si poate fi gasit")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_Valid_RepoPersistsAndFinds() throws Exception {
        Product product = new Product(1, "Limonada", 10.5, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(product);

        Product saved = productService.findById(1);
        assertNotNull(saved);
        assertEquals("Limonada", saved.getNume());

        List<String> lines = Files.readAllLines(tempDir.resolve("products-step4.txt"));
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).startsWith("1,Limonada,10.5"));
    }

    @Test
    @DisplayName("addProduct - produs real invalid cu nume gol, validator arunca exceptie, repo ramane gol")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_BlankName_ValidationStopsSave() {
        Product product = new Product(1, "", 10.5, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(product));

        assertNull(productService.findById(1));
    }

    @Test
    @DisplayName("findById - dupa salvare, repo real returneaza produsul")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void findById_AfterSave_ReturnsProduct() {
        Product product = new Product(1, "Limonada", 10.5, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(product);

        Product result = productService.findById(1);

        assertNotNull(result);
        assertEquals("Limonada", result.getNume());
        assertEquals(10.5, result.getPret());
    }
}
