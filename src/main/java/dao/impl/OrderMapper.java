package dao.impl;

import model.Order;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;

public class OrderMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Order o = new Order();
        o.setId(rs.getInt("order_id"));
        o.setDate(rs.getTimestamp("order_date").toLocalDateTime());
        o.setCustomer_id(rs.getInt("customer_id"));
        o.setTable_id(rs.getInt("table_id"));
        return o;
    }
}
