package ui.screens.customers;

import common.Constants;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import services.CustomerService;
import services.OrderService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DeleteCustomerController extends BaseScreenController {
    @FXML
    public TableView<Customer> customersTable;
    @FXML
    public TableColumn<Customer, Integer> idCustomerColumn;
    @FXML
    public TableColumn<Customer, String> firstnameCustomerColumn;
    @FXML
    public TableColumn<Customer, String> lastnameCustomerColumn;
    @FXML
    public TableColumn<Customer, String> emailCustomerColumn;
    @FXML
    public TableColumn<Customer, String> phoneCustomerColumn;
    @FXML
    public TableColumn<Customer, LocalDate> dobCustomerColumn;
    @FXML
    private Button deleteButton;

    @FXML
    public TableView<Order> ordersTable;
    @FXML
    public TableColumn<Order, Integer> idOrderColumn;
    @FXML
    public TableColumn<Order, LocalDateTime> dateOrderColumn;
    @FXML
    public TableColumn<Order, Integer> customerOrderColumn;
    @FXML
    public TableColumn<Order, Integer> tableOrderColumn;
    private Customer selectedCustomer;
    private final CustomerService servicesCustomers;
    private final OrderService orderService;

    @Inject
    public DeleteCustomerController(CustomerService servicesCustomers, OrderService orderService) {
        this.servicesCustomers = servicesCustomers;
        this.orderService = orderService;
    }

    public void initialize() throws IOException {
        idCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("_id"));
        firstnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        lastnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        emailCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        dobCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        customersTable.setOnMouseClicked(this::handleTableClick);
//        idOrderColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//        customerOrderColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        tableOrderColumn.setCellValueFactory(new PropertyValueFactory<>("table_id"));
    }

    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
            ordersTable.getItems().clear();
            ordersTable.getItems().addAll(selectedCustomer.getOrders());
        }
    }

    @Override
    public void principalCargado() throws IOException {
        setTables();
    }

    private void setTables() {
        customersTable.getItems().clear();
        servicesCustomers.getAll().peek(customers -> customersTable.getItems().addAll(customers))
                .peekLeft(customerError -> getPrincipalController().sacarAlertError(customerError.getMessage()));
        if (selectedCustomer != null) {
            ordersTable.getItems().clear();
            ordersTable.getItems().addAll(selectedCustomer.getOrders());
        }
    }

    public void deleteCustomer(ActionEvent actionEvent) {
        if (selectedCustomer != null){
            servicesCustomers.delete(selectedCustomer, false).peek(result -> {
                if (result) {
                    if (getPrincipalController().showConfirmationDialog("Delete", "Are you sure you want to continue?, If you delete the customer, all orders they have will also be deleted.")) {
                        servicesCustomers.delete(selectedCustomer, true);
                    }
                } else {
                    getPrincipalController().sacarAlertInfo("The customer has been deleted successfully");
                }
                setTables();
            }).peekLeft(customerError -> getPrincipalController().sacarAlertError(customerError.getMessage()));
        }
        else
            getPrincipalController().sacarAlertError("Select a customer to delete");
    }
}
