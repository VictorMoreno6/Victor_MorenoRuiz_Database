package services;

import dao.MenuItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MenuItem;
import model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class MenuItemsService {

    @Inject
    private MenuItemDAO menuItemDAO;

    Either<String, List<MenuItem>> getAll() {
        return menuItemDAO.getAll();
    }

    public List<String> getMenuItemsName() {
        List<String> result = new ArrayList<>();
        for (MenuItem menuItem : menuItemDAO.getAll().get()) {
                result.add(menuItem.getName());
        }
        return result;
    }

    public MenuItem getMenuItemByName(String name) {
        for (MenuItem menuItem : menuItemDAO.getAll().get()) {
            if (menuItem.getName().equals(name)) {
                return menuItem;
            }
        }
        return null;
    }
}
