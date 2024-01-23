package ui.screens.orders;

import common.Constants;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.*;
import services.CustomerService;
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
    Label priceLabel;
    @FXML
    Button filterButton;
    @FXML
    DatePicker dateField;
    public Label customerNameLabel;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    private final CustomerService customerService;

    @Inject
    public ShowOrderController(OrderService orderService, OrderItemService orderItemService, CustomerService customerService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.customerService = customerService;
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
            itemsTable.getItems().clear();
            priceLabel.setText("");
            Order order = ordersTable.getSelectionModel().getSelectedItem();
            if (!orderService.getOrderItems(order.getId()).isEmpty()){
                itemsTable.getItems().addAll(orderService.getOrderItems(order.getId()));
                priceLabel.setText(orderService.getTotalPrice(order.getId()) + "€");
            }
            customerNameLabel.setText(customerService.get(order.getCustomer_id()).get().getFirst_name()
                    + " " + customerService.get(order.getCustomer_id()).get().getLast_name());
        }
    }

    @Override
    public void principalCargado() throws IOException {
        setTables();
        if (getPrincipalController().actualUser.getId() > 0)
            comboBoxId.setVisible(false);
        else
            comboBoxId.setVisible(true);
    }

    private void setTables() {
        ordersTable.getItems().clear();
        int i = getPrincipalController().actualUser.getId();
        if (i < 0)
            orderService.getAll().peek(orders -> ordersTable.getItems().addAll(orders))
                .peekLeft(orderError -> getPrincipalController().sacarAlertError(orderError.getMessage()));
        else {
            ordersTable.getItems().addAll(orderService.getOrdersById(i));
        }
    }

    public void addFilter() {
        List<Order> aux;
        ordersTable.getItems().clear();
        if (dateField.getValue() == null) {
            if (comboBoxId.getValue() == null) {
                getPrincipalController().sacarAlertInfo("All filters fields are empty");
            } else {
                aux = orderService.getOrdersById((Integer) comboBoxId.getValue());
                ordersTable.getItems().addAll(aux);
            }

        } else {
            aux = orderService.getOrdersByDate(dateField.getValue());
            ordersTable.getItems().addAll(aux);
        }
    }

    public void restartOrders() {
        dateField.setValue(null);
        comboBoxId.setValue(null);
        setTables();
        itemsTable.getItems().clear();
        priceLabel.setText("");
    }
}
