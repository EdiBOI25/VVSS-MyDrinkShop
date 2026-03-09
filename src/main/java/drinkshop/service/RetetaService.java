package drinkshop.service;

import drinkshop.domain.Reteta;
import java.util.List;

public interface RetetaService {
    void addReteta(Reteta r);
    void updateReteta(Reteta r);
    void deleteReteta(int id);
    Reteta findById(int id);
    List<Reteta> getAll();
}
