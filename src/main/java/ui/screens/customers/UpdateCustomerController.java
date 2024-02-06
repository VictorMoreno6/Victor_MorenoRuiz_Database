package ui.screens.customers;

import common.Constants;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Customer;
import services.CustomerService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.time.LocalDate;

public class UpdateCustomerController extends BaseScreenController {
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
    
    private Customer oldCustomer;

    @Inject
    public UpdateCustomerController(CustomerService servicesCustomers) {
        this.servicesCustomers = servicesCustomers;
    }

    public void initialize() throws IOException {
        idCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("_id"));
        firstnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        lastnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        emailCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        dobCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        customersTable.setOnMouseClicked(this::handleTableClick);
        idField.setEditable(false);
    }

    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            oldCustomer = customersTable.getSelectionModel().getSelectedItem();
            if (oldCustomer != null) {
                idField.setText(String.valueOf(oldCustomer.get_id()));
                fnameField.setText(oldCustomer.getFirst_name());
                lnameField.setText(oldCustomer.getLast_name());
                emailField.setText(oldCustomer.getEmail());
                phoneField.setText(oldCustomer.getPhone());
                dobField.setValue(oldCustomer.getDob());
            }
        }
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

    public void updateCustomer(ActionEvent actionEvent) {
        if(!idField.getText().isEmpty() && fnameField.getText() != null && !fnameField.getText().isEmpty() && lnameField.getText() != null && !lnameField.getText().isEmpty() && emailField.getText() != null && !emailField.getText().isEmpty() && phoneField.getText() != null && !phoneField.getText().isEmpty() && dobField.getValue() != null && !dobField.getValue().toString().isEmpty()){
            Customer newCustomer = new Customer(oldCustomer.get_id(), fnameField.getText(), lnameField.getText(), emailField.getText(), phoneField.getText(), dobField.getValue());
            if(servicesCustomers.update(newCustomer).isRight()){
                getPrincipalController().sacarAlertInfo(Constants.CUSTOMER_UPDATED_SUCCESSFULLY);
                setTable();
            } else
                getPrincipalController().sacarAlertError(Constants.FAILED_TO_UPDATE_THE_CUSTOMER);

        } else {
            getPrincipalController().sacarAlertError(Constants.THERE_IS_AN_EMPTY_FIELD);
        }
    }
}
