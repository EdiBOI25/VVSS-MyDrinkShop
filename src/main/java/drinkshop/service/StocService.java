package drinkshop.service;

import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import java.util.List;

public interface StocService {
    List<Stoc> getAll();
    void add(Stoc s);
    void update(Stoc s);
    void delete(int id);
    boolean areSuficient(Reteta reteta);
    void consuma(Reteta reteta);
}
