package dao.impl;

import dao.OrderItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.errors.OrderError;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDaoImpl implements OrderItemDAO {

    private DBConnection db;

    @Inject
    public OrderItemDaoImpl(DBConnection db) {
        this.db = db;
    }
    @Override
    public List<OrderItem> getAll() {
        List<OrderItem> orderItems = null;

        try(Connection con = db.getConnection();
            Statement stmt= con.createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT * FROM order_items");

            orderItems = readRs(rs);

        }catch (Exception e){
            e.printStackTrace();
        }
        return orderItems;
    }

    public List<OrderItem> readRs(ResultSet rs){
        try{
            List<OrderItem> orderItems = new ArrayList<>();
            while(rs.next()){
                int id = rs.getInt("order_item_id");
                int order_id = rs.getInt("order_id");
                MenuItem menuItem = getMenuItem(rs.getInt("menu_item_id"));
                int quantity = rs.getInt("quantity");
                orderItems.add(new OrderItem(id, order_id, menuItem, quantity));
            }
            return orderItems;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }


    public MenuItem getMenuItem(int id){
        MenuItem menuItem = null;
        try(Connection con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM menu_items WHERE menu_item_id = ?")){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int menuItemId = rs.getInt("menu_item_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int price = rs.getInt("price");
                menuItem = new MenuItem(menuItemId, name, description, price);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return menuItem;
    }

    @Override
    public Either<OrderError, Integer> save(List<OrderItem> orderItems, Order order) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> delete(int id) {
        return null;
    }
}
