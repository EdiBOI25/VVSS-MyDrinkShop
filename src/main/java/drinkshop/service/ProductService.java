package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import java.util.List;

public interface ProductService {
    void addProduct(Product p);
    void updateProduct(int id, String name, double price, CategorieBautura categorie, TipBautura tip);
    void deleteProduct(int id);
    List<Product> getAllProducts();
    Product findById(int id);
    List<Product> filterByCategorie(CategorieBautura categorie);
    List<Product> filterByPret(double pretMinim, double pretMaxim);
    List<Product> filterByTip(TipBautura tip);
}
