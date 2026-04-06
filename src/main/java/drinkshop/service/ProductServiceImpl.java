package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.repository.Repository;

import drinkshop.service.validator.ProductValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {

    private final Repository<Integer, Product> productRepo;
    private final ProductValidator productValidator;

    public ProductServiceImpl(Repository<Integer, Product> productRepo) {
        this.productRepo = productRepo;
        this.productValidator = new ProductValidator();
    }

    public void addProduct(Product p) {
        productValidator.validate(p);
        productRepo.save(p);
    }

    public void updateProduct(int id, String name, double price, CategorieBautura categorie, TipBautura tip) {
        Product updated = new Product(id, name, price, categorie, tip);
        productValidator.validate(updated);
        productRepo.update(updated);
    }

    public void deleteProduct(int id) {
        productRepo.delete(id);
    }

    public List<Product> getAllProducts() {
//        Iterable<Product> it=productRepo.findAll();
//        ArrayList<Product> products=new ArrayList<>();
//        it.forEach(products::add);
//        return products;

//        return StreamSupport.stream(productRepo.findAll().spliterator(), false)
//                    .collect(Collectors.toList());
        return productRepo.findAll();
    }

    public Product findById(int id) {
        return productRepo.findOne(id);
    }

    public List<Product> filterByCategorie(CategorieBautura categorie) {
        if (categorie == CategorieBautura.ALL) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> p.getCategorie() == categorie)
                .collect(Collectors.toList());
    }

    public List<Product> filterByPret(double pretMinim, double pretMaxim) {
        if (pretMinim == 0 && pretMaxim == 0) return getAllProducts();

        if (pretMinim > pretMaxim) {
            throw new IllegalArgumentException("Pret minim nu poate fi mai mare decat pret maxim!");
        }

        if (pretMinim < 0 || pretMaxim < 0) {
            throw new IllegalArgumentException("Pretul nu poate fi negativ!");
        }

        List<Product> products = new ArrayList<>();
        for (Product p : getAllProducts()) {
            if (p.getPret() >= pretMinim && p.getPret() <= pretMaxim) {
                products.add(p);
            }
        }

        return products;
    }

    public List<Product> filterByTip(TipBautura tip) {
        if (tip == TipBautura.ALL) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> p.getTip() == tip)
                .collect(Collectors.toList());
    }
}