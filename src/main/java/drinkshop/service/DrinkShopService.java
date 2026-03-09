package drinkshop.service;

import drinkshop.domain.*;
import java.util.List;

public interface DrinkShopService {
    // ---------- PRODUCT ----------
    void addProduct(Product p);
    void updateProduct(int id, String name, double price, CategorieBautura categorie, TipBautura tip);
    void deleteProduct(int id);
    List<Product> getAllProducts();
    List<Product> filtreazaDupaCategorie(CategorieBautura categorie);
    List<Product> filtreazaDupaTip(TipBautura tip);

    // ---------- ORDER ----------
    void addOrder(Order o);
    List<Order> getAllOrders();
    double computeTotal(Order o);
    String generateReceipt(Order o);
    double getDailyRevenue();
    void exportCsv(String path);

    // ---------- STOCK + RECIPE ----------
    void comandaProdus(Product produs);
    List<Reteta> getAllRetete();
    void addReteta(Reteta r);
    void updateReteta(Reteta r);
    void deleteReteta(int id);
}
