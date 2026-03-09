package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.repository.file.FileOrderRepository;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.repository.file.FileRetetaRepository;
import drinkshop.repository.file.FileStocRepository;
import drinkshop.reports.DailyReportService;
import drinkshop.service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DrinkShopApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ---------- Initializare Repository-uri care citesc din fisiere ----------
        Repository<Integer, Product> productRepo = new FileProductRepository("data/products.txt");
        Repository<Integer, Order> orderRepo = new FileOrderRepository("data/orders.txt", productRepo);
        Repository<Integer, Reteta> retetaRepo = new FileRetetaRepository("data/retete.txt");
        Repository<Integer, Stoc> stocRepo = new FileStocRepository("data/stocuri.txt");

        // ---------- Initializare Service ----------
        ProductService productService = new ProductServiceImpl(productRepo);
        OrderService orderService = new OrderServiceImpl(orderRepo, productRepo);
        RetetaService retetaService = new RetetaServiceImpl(retetaRepo);
        StocService stocService = new StocServiceImpl(stocRepo);
        DailyReportService reportService = new DailyReportService(orderRepo);

        // ---------- Incarcare FXML ----------

        FXMLLoader loader = new FXMLLoader(getClass().getResource("drinkshop.fxml"));
        Scene scene = new Scene(loader.load());

        // ---------- Setare Service in Controller ----------
        DrinkShopController controller = loader.getController();
        controller.setServices(productService, orderService, retetaService, stocService, reportService);

        // ---------- Afisare Fereastra ----------
        stage.setTitle("Coffee Shop Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}