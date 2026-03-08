package drinkshop.repository.file;

import drinkshop.domain.Product;
import drinkshop.domain.CategorieBautura;
import drinkshop.domain.TipBautura;

public class FileProductRepository
        extends FileAbstractRepository<Integer, Product> {

    public FileProductRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(Product entity) {
        return entity.getId();
    }

    @Override
    protected Product extractEntity(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Line cannot be null or empty");
        }

        String[] elems = line.split(",");
        if (elems.length != 5) {
            throw new IllegalArgumentException("Invalid line format: " + line);
        }

        String idStr = elems[0].trim();
        String name = elems[1].trim();
        String priceStr = elems[2].trim();
        String categorieStr = elems[3].trim();
        String tipStr = elems[4].trim();

        try {
            int id = Integer.parseInt(idStr);
            double price = Double.parseDouble(priceStr);
            CategorieBautura categorie = CategorieBautura.valueOf(categorieStr);
            TipBautura tip = TipBautura.valueOf(tipStr);

            return new Product(id, name, price, categorie, tip);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error parsing line: " + line, e);
        }
    }

    @Override
    protected String createEntityAsString(Product entity) {
        return entity.getId() + "," +
                entity.getNume() + "," +
                entity.getPret() + "," +
                entity.getCategorie() + "," +
                entity.getTip();
    }
}