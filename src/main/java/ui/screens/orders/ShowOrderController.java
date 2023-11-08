package ui.screens.orders;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.*;
import model.MenuItem;
import services.OrderItemService;
import services.OrderService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShowOrderController extends BaseScreenController {
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
    @FXML
    public ComboBox comboBoxId;
    @FXML
    public TableView itemsTable;
    @FXML
    public TableColumn<OrderItem, String> menuItemColumn;
    @FXML
    public TableColumn<OrderItem, Integer> quantityItemColumn;
    @FXML
    Button filterButton;
    @FXML
    DatePicker dateField;
    @FXML
    TextField customerOrderField;
    public Label customerNameLabel;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Inject
    public ShowOrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    public void initialize() throws IOException {
        idOrderColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerOrderColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        tableOrderColumn.setCellValueFactory(new PropertyValueFactory<>("table_id"));
        comboBoxId.getItems().addAll(orderService.getIds());
       // filterComboBox.getItems().addAll("Date", "Customer");
        ordersTable.setOnMouseClicked(this::handleTableClick);

        menuItemColumn.setCellValueFactory(cellData -> orderItemService.printMenuItemName(cellData.getValue()));
    //menuItemColumn.setCellValueFactory(new PropertyValueFactory<>("menuItem"));
        quantityItemColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            int a = ordersTable.getSelectionModel().getSelectedItem().getCustomer_id();
            itemsTable.getItems().clear();
            if (!orderService.getOrderItems(a).isEmpty())
                itemsTable.getItems().addAll(orderService.getOrderItems(a));
            customerNameLabel.setText(orderService.getCustomerName(a));
        }
    }

    @Override
    public void principalCargado() throws IOException {
        setTables();
    }

    //AQUI HAGO LO DEL ID
    private void setTables() {
        ordersTable.getItems().clear();
        int i = getPrincipalController().actualUser.getId();
        if (i < 0)
            orderService.getAll().peek(orders -> ordersTable.getItems().addAll(orders))
                .peekLeft(orderError -> getPrincipalController().sacarAlertError(orderError.getMessage()));
        else {
            orderService.get(i).peek(order -> ordersTable.getItems().addAll(order))
                    .peekLeft(orderError -> getPrincipalController().sacarAlertError(orderError.getMessage()));
        }
    }

    //TODO
    public void addFilter(ActionEvent actionEvent) {
        List<Order> aux = new ArrayList<>();
        ordersTable.getItems().clear();
        if (dateField.getValue() == null) {
            if (comboBoxId.getValue() == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle(Constants.ERROR);
                a.setContentText("All filters fields are empty");
                a.setHeaderText(null);
                a.show();
                ordersTable.getItems().clear();
                orderService.getAll().peek(orders -> ordersTable.getItems().addAll(orders))
                        .peekLeft(orderError -> getPrincipalController().sacarAlertError(orderError.getMessage()));
            } else {
                aux = orderService.getOrdersById((Integer) comboBoxId.getValue());
                ordersTable.getItems().addAll(aux);
            }

            /*if (customerOrderField.getText().equals("")) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle(Constants.ERROR);
                a.setContentText("All filters fields are empty");
                a.setHeaderText(null);
                a.show();
                ordersTable.getItems().clear();
                orderService.getAll().peek(orders -> ordersTable.getItems().addAll(orders))
                        .peekLeft(orderError -> getPrincipalController().sacarAlertError(orderError.getMessage()));
            } else {
                try {
                    aux = orderService.getOrdersById(Integer.parseInt(customerOrderField.getText()));
                    ordersTable.getItems().addAll(aux);
                } catch (Exception e) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle(Constants.ERROR);
                    a.setContentText("Customer id must be a number");
                    a.setHeaderText(null);
                    a.show();
                }
            }*/
        } else {
            aux = orderService.getOrdersByDate(dateField.getValue());
            ordersTable.getItems().addAll(aux);
        }
    }
}
