package dao.impl;

import dao.TableDAO;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.MenuItem;
import model.Table;
import model.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class TableDaoJdbc implements TableDAO {

    private DBConnection db;

    @Inject
    public TableDaoJdbc(DBConnection db) {
        this.db = db;
    }
    @Override
    public List<Table> getAll() {
        List<Table> tables = null;

        try(Connection con = db.getConnection();
            Statement stmt= con.createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT * FROM restaurant_tables");

            tables = readRs(rs);

        }catch (Exception e){
            log.error(e.getMessage());
        }
        return tables;
    }

    public List<Table> readRs(ResultSet rs){
        try{
            List<Table> tables = new ArrayList<>();
            while(rs.next()){
                int id = rs.getInt("table_number_id");
                int seats = rs.getInt("number_of_seats");

                tables.add(new Table(id, seats));
            }
            return tables;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
