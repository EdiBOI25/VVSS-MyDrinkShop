package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.receipt.ReceiptGenerator;
import drinkshop.service.*;
import drinkshop.reports.DailyReportService;
import drinkshop.export.CsvExporter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DrinkShopController {

    private ProductService productService;
    private OrderService orderService;
    private RetetaService retetaService;
    private StocService stocService;
    private DailyReportService reportService;

    // ---------- PRODUCT ----------
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colProdId;
    @FXML private TableColumn<Product, String> colProdName;
    @FXML private TableColumn<Product, Double> colProdPrice;
    @FXML private TableColumn<Product, CategorieBautura> colProdCategorie;
    @FXML private TableColumn<Product, TipBautura> colProdTip;
    @FXML private TextField txtProdName, txtProdPrice;
    @FXML private ComboBox<CategorieBautura> comboProdCategorie;
    @FXML private ComboBox<TipBautura> comboProdTip;

    // ---------- RETETE ----------
    @FXML private TableView<Reteta> retetaTable;
    @FXML private TableColumn<Reteta, Integer> colRetetaId;
    @FXML private TableColumn<Reteta, String> colRetetaDesc;

    @FXML private TableView<IngredientReteta> newRetetaTable;
    @FXML private TableColumn<IngredientReteta, String> colNewIngredName;
    @FXML private TableColumn<IngredientReteta, Double> colNewIngredCant;
    @FXML private TextField txtNewIngredName, txtNewIngredCant;

    // ---------- ORDER (CURRENT) ----------
    @FXML private TableView<OrderItem> currentOrderTable;
    @FXML private TableColumn<OrderItem, String> colOrderProdName;
    @FXML private TableColumn<OrderItem, Integer> colOrderQty;

    @FXML private ComboBox<Integer> comboQty;
    @FXML private Label lblOrderTotal;
    @FXML private TextArea txtReceipt;

    @FXML private Label lblTotalRevenue;

    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ObservableList<Reteta> retetaList = FXCollections.observableArrayList();
    private ObservableList<IngredientReteta> newRetetaList = FXCollections.observableArrayList();
    private ObservableList<OrderItem> currentOrderItems = FXCollections.observableArrayList();

    private Order currentOrder = new Order(1);

    public void setServices(
            ProductService productService,
            OrderService orderService,
            RetetaService retetaService,
            StocService stocService,
            DailyReportService reportService
    ) {
        this.productService = productService;
        this.orderService = orderService;
        this.retetaService = retetaService;
        this.stocService = stocService;
        this.reportService = reportService;
        initData();
    }

    @FXML
    private void initialize() {

        // PRODUCTS
        colProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProdName.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colProdPrice.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colProdCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colProdTip.setCellValueFactory(new PropertyValueFactory<>("tip"));
        productTable.setItems(productList);

        comboProdCategorie.getItems().setAll(CategorieBautura.values());
        comboProdTip.getItems().setAll(TipBautura.values());

        // RETETE
        colRetetaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRetetaDesc.setCellValueFactory(data -> {
            Reteta r = data.getValue();
            String desc = r.getIngrediente().stream()
                    .map(i -> i.getDenumire() + " (" + i.getCantitate() + ")")
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(desc);
        });
        retetaTable.setItems(retetaList);

        colNewIngredName.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        colNewIngredCant.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        newRetetaTable.setItems(newRetetaList);

        // CURRENT ORDER TABLE
        colOrderProdName.setCellValueFactory(data -> {
            int prodId = data.getValue().getProduct().getId();
            Product p = productList.stream().filter(pr -> pr.getId() == prodId).findFirst().orElse(null);
            return new SimpleStringProperty(p != null ? p.getNume() : "N/A");
        });
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        currentOrderTable.setItems(currentOrderItems);

        comboQty.setItems(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10));
    }

    private void initData() {
        productList.setAll(productService.getAllProducts());
        retetaList.setAll(retetaService.getAll());
        lblTotalRevenue.setText("Daily Revenue: " + reportService.getTotalRevenue());
        updateOrderTotal();
    }

    // ---------- PRODUCT ----------
    @FXML
    private void onAddProduct() {
        Reteta r=retetaTable.getSelectionModel().getSelectedItem();

        if (r == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Selectati o reteta pentru care adugati un produs");
            alert.showAndWait();
            return;
        }else
        if (productService.getAllProducts().stream().filter(p->p.getId()==r.getId()).toList().size()>0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Exista un produs cu reteta adaugata.");
            alert.showAndWait();
            return;
        }
        Product p = new Product(r.getId(),
                txtProdName.getText(),
                Double.parseDouble(txtProdPrice.getText()),
                comboProdCategorie.getValue(),
                comboProdTip.getValue());
        productService.addProduct(p);
        initData();
    }

    @FXML
    private void onUpdateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        productService.updateProduct(selected.getId(), txtProdName.getText(),
                Double.parseDouble(txtProdPrice.getText()),
                comboProdCategorie.getValue(), comboProdTip.getValue());
        initData();
    }

    @FXML
    private void onDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        productService.deleteProduct(selected.getId());
        initData();
    }

    @FXML
    private void onFilterCategorie() {
        productList.setAll(productService.filterByCategorie(comboProdCategorie.getValue()));
    }

    @FXML
    private void onFilterTip() {
        productList.setAll(productService.filterByTip(comboProdTip.getValue()));
    }

    // ---------- RETETA NOUA ----------
    @FXML
    private void onAddNewIngred() {
        newRetetaList.add(new IngredientReteta(txtNewIngredName.getText(),
                Double.parseDouble(txtNewIngredCant.getText())));
    }

    @FXML
    private void onDeleteNewIngred() {
        IngredientReteta sel = newRetetaTable.getSelectionModel().getSelectedItem();
        if (sel != null) newRetetaList.remove(sel);
    }

    @FXML
    private void onAddNewReteta() {
        Reteta r = new Reteta(retetaService.getAll().size()+1, new ArrayList<>(newRetetaList));
        retetaService.addReteta(r);
        newRetetaList.clear();
        initData();
    }

    @FXML
    private void onClearNewRetetaIngredients() {
        newRetetaTable.getItems().clear();
        txtNewIngredName.clear();
        txtNewIngredCant.clear();
    }

    // ---------- CURRENT ORDER ----------
    @FXML
    private void onAddOrderItem() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        Integer qty = comboQty.getValue();

        if (selected == null) {
            showError("Selectează un produs din listă.");
            return;
        }
        if (qty == null) {
            showError("Selectează cantitatea.");
            return;
        }

        currentOrderItems.add(new OrderItem(selected, qty));
        updateOrderTotal();
    }

    @FXML
    private void onDeleteOrderItem() {
        OrderItem sel = currentOrderTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            currentOrderItems.remove(sel);
            updateOrderTotal();
        }
    }

    @FXML
    private void onFinalizeOrder() {
        currentOrder.getItems().clear();
        currentOrder.getItems().addAll(currentOrderItems);
        currentOrder.computeTotalPrice();

        orderService.addOrder(currentOrder);
        txtReceipt.setText(ReceiptGenerator.generate(currentOrder, productService.getAllProducts()));

        currentOrderItems.clear();
        currentOrder = new Order(currentOrder.getId() + 1);
        updateOrderTotal();
    }

    private void updateOrderTotal() {
        currentOrder.getItems().clear();
        currentOrder.getItems().addAll(currentOrderItems);
        double total = orderService.computeTotal(currentOrder);
        lblOrderTotal.setText("Total: " + total);
    }

    private void comandaProdus(Product produs) {
        Reteta reteta = retetaService.findById(produs.getId());

        if (!stocService.areSuficient(reteta)) {
            throw new IllegalStateException("Stoc insuficient pentru produsul: " + produs.getNume());
        }
        stocService.consuma(reteta);
    }

    // ---------- EXPORT + REVENUE ----------
    @FXML
    private void onExportOrdersCsv() {
        CsvExporter.exportOrders(productService.getAllProducts(), orderService.getAllOrders(), "orders.csv");
    }

    @FXML
    private void onDailyRevenue() {
        lblTotalRevenue.setText("Daily Revenue: " + reportService.getTotalRevenue());
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}