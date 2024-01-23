package dao.impl;

import common.Constants;
import dao.MenuItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.MenuItem;
import model.hibernate.MenuItemsEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuItemDaoJdbc implements MenuItemDAO {
    private final JPAUtil jpautil;

    private EntityManager entityManager;

    @Inject
    public MenuItemDaoJdbc(JPAUtil jpautil) {
        this.jpautil = jpautil;
    }




    @Override
    public Either<String, List<MenuItem>> getAll() {
        Either<String, List<MenuItem>> result = null;

        try {
            entityManager = jpautil.getEntityManager();

            TypedQuery<MenuItemsEntity> query = entityManager.createQuery("SELECT mi FROM MenuItemsEntity mi", MenuItemsEntity.class);
            List<MenuItemsEntity> menuItemsEntities = query.getResultList();

            List<MenuItem> menuItems = menuItemsEntities.stream()
                    .map(menuItemsEntity -> new MenuItem(
                            menuItemsEntity.getId(),
                            menuItemsEntity.getName(),
                            menuItemsEntity.getDescription(),
                            menuItemsEntity.getPrice()
                    ))
                    .collect(Collectors.toList());

            result = Either.right(menuItems);

        } catch (Exception e) {
            e.printStackTrace();
            result = Either.left(Constants.ERROR_CONNECTING_TO_DATABASE);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return result;
    }

    @Override
    public Either<String, MenuItem> get(int id) {
        Either<String, MenuItem> result = null;

        try {
            entityManager = jpautil.getEntityManager();

            MenuItemsEntity menuItemsEntity = entityManager.find(MenuItemsEntity.class, id);

            if (menuItemsEntity == null) {
                result = Either.left("No menu item found");
            } else {
                MenuItem menuItem = new MenuItem(
                        menuItemsEntity.getId(),
                        menuItemsEntity.getName(),
                        menuItemsEntity.getDescription(),
                        menuItemsEntity.getPrice()
                );
                result = Either.right(menuItem);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = Either.left(Constants.ERROR_CONNECTING_TO_DATABASE);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return result;
    }


    @Override
    public Either<String, Integer> save(MenuItem c) {
        return null;
    }

    @Override
    public Either<String, Integer> update(MenuItem c) {
        return null;
    }

    @Override
    public Either<String, Integer> delete(MenuItem c) {
        return null;
    }
}
