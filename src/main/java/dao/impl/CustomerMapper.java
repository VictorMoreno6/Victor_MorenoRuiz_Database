package dao.impl;

import model.Customer;
import org.springframework.jdbc.core.RowMapper;

public class CustomerMapper implements RowMapper<Customer> {

        @Override
        public Customer mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Customer c = new Customer();
            c.setId(rs.getInt("id"));
            c.setFirst_name(rs.getString("first_name"));
            c.setLast_name(rs.getString("last_name"));
            c.setEmail(rs.getString("email"));
            c.setPhone(rs.getString("phone"));
            c.setDob(rs.getDate("date_of_birth").toLocalDate());
            return c;
        }
}
