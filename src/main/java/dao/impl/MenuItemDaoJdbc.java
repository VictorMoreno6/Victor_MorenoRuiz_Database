package dao.impl;

import common.Constants;
import dao.MenuItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDaoJdbc implements MenuItemDAO {
    private DBConnection db;

    @Inject
    public MenuItemDaoJdbc(DBConnection db) {
        this.db = db;
    }


    @Override
    public Either<String, List<MenuItem>> getAll() {
        Either<String, List<MenuItem>> result = null;
        try(Connection con = db.getConnection();
            Statement stmt = con.createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT * FROM menu_items");

            List<MenuItem> menuItems = readRs(rs);
            result = Either.right(menuItems);
        }catch (Exception e){
            e.printStackTrace();
            result = Either.left(Constants.ERROR_CONNECTING_TO_DATABASE);
        }
        return result;
    }

    public List<MenuItem> readRs(ResultSet rs){
        try{
            List<MenuItem> menuItems = new ArrayList<>();
            while(rs.next()){
                int id = rs.getInt("menu_item_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                menuItems.add(new MenuItem(id, name, description, price));
            }
            return menuItems;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Either<String, MenuItem> get(int id) {
        Either<String, MenuItem> result = null;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM menu_items WHERE menu_item_id = ?")){
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            List<MenuItem> menuItems = readRs(rs);
            if(menuItems.size() == 0){
                result = Either.left("No menu item found");
            }else{
                result = Either.right(menuItems.get(0));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = Either.left(Constants.ERROR_CONNECTING_TO_DATABASE);
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
