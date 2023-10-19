package ui.screens.customers;

import common.Constants;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import services.CustomerService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.time.LocalDate;

public class AddCustomerController extends BaseScreenController {
    private final CustomerService servicesCustomers;
    public TableView<Customer> customersTable;
    @FXML
    public TableColumn<Integer, Customer> idCustomerColumn;
    @FXML
    public TableColumn<String, Customer> firstnameCustomerColumn;
    @FXML
    public TableColumn<String, Customer> lastnameCustomerColumn;
    @FXML
    public TableColumn<String, Customer> emailCustomerColumn;
    @FXML
    public TableColumn<String, Customer> phoneCustomerColumn;
    @FXML
    public TableColumn<LocalDate, Customer> dobCustomerColumn;
    public TextField idField;
    public TextField fnameField;
    public TextField lnameField;
    public TextField emailField;
    public TextField phoneField;
    public DatePicker dobField;

    @Inject
    public AddCustomerController(CustomerService servicesCustomers) {
        this.servicesCustomers = servicesCustomers;
    }

    public void initialize() throws IOException {
        idCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        lastnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        emailCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        dobCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
    }

    @Override
    public void principalCargado() throws IOException {
        setTable();
    }

    private void setTable() {
        customersTable.getItems().clear();
        servicesCustomers.getAll().peek(customers -> customersTable.getItems().addAll(customers))
                .peekLeft(customerError -> getPrincipalController().sacarAlertError(customerError.getMessage()));
    }

    public void addCustomer(ActionEvent actionEvent) {
        if (fnameField.getText().isEmpty() || lnameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty() || dobField.getValue() == null) {
            getPrincipalController().sacarAlertError(Constants.THERE_IS_AN_EMPTY_FIELD);
        } else {
            if (servicesCustomers.save(new Customer(servicesCustomers.autoId(), fnameField.getText(), lnameField.getText(), emailField.getText(), phoneField.getText(), dobField.getValue())).isRight()){
                getPrincipalController().sacarAlertInfo(Constants.CUSTOMER_ADDED);
            } else {
                getPrincipalController().sacarAlertError(Constants.CUSTOMER_NOT_ADDED);
                setTable();
            }
        }
    }
}
