package drinkshop.service;

import drinkshop.domain.Reteta;
import drinkshop.repository.Repository;

import drinkshop.service.validator.RetetaValidator;
import java.util.List;

public class RetetaService {

    private final Repository<Integer, Reteta> retetaRepo;
    private final RetetaValidator retetaValidator;

    public RetetaService(Repository<Integer, Reteta> retetaRepo) {
        this.retetaRepo = retetaRepo;
        this.retetaValidator = new RetetaValidator();
    }

    public void addReteta(Reteta r) {
        retetaValidator.validate(r);
        retetaRepo.save(r);
    }

    public void updateReteta(Reteta r) {
        retetaValidator.validate(r);
        retetaRepo.update(r);
    }

    public void deleteReteta(int id) {
        retetaRepo.delete(id);
    }

    public Reteta findById(int id) {
        return retetaRepo.findOne(id);
    }

    public List<Reteta> getAll() {
        return retetaRepo.findAll();
    }
}