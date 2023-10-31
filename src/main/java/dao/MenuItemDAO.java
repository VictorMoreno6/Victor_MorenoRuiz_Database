package dao;

import io.vavr.control.Either;
import model.MenuItem;

import java.util.List;

public interface MenuItemDAO {
    Either<String, List<MenuItem>> getAll();

    Either<String, MenuItem> get(int id);

    Either<String, Integer> save(MenuItem c);

    Either<String, Integer> update(MenuItem c);

    Either<String, Integer> delete(MenuItem c);
}
