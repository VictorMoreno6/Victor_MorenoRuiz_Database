package ui.screens.orders;

import common.Constants;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.*;
import org.bson.types.ObjectId;
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
//        idOrderColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//        customerOrderColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
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
            if (!order.getOrderItems().isEmpty()) {
                itemsTable.getItems().addAll(order.getOrderItems());
                priceLabel.setText(orderService.getTotalPrice(order.getOrderItems()) + "€");
            }
//            customerNameLabel.setText(customerService.getCustomerByOrder(order).getFirst_name()
//                    + " " + customerService.getCustomerByOrder(order).getLast_name());
        }
    }

    @Override
    public void principalCargado() throws IOException {
        setTables();
        if (getPrincipalController().actualUser.getUsername().equals("root"))
            comboBoxId.setVisible(true);
        else
            comboBoxId.setVisible(false);
    }

    private void setTables() {
        ordersTable.getItems().clear();
        ObjectId i = getPrincipalController().actualUser.get_id();
        if (getPrincipalController().actualUser.getUsername().equals("root"))
            orderService.getAll().peek(orders -> ordersTable.getItems().addAll(orders))
                .peekLeft(orderError -> getPrincipalController().sacarAlertError(orderError.getMessage()));
        else {
            ordersTable.getItems().addAll(orderService.getOrdersById(i));
        }
    }

    //TODO
    //ver si el filtro este va bn
    public void addFilter() {
        List<Order> aux;
        ordersTable.getItems().clear();
        if (dateField.getValue() == null) {
            if (comboBoxId.getValue() == null) {
                getPrincipalController().sacarAlertInfo("All filters fields are empty");
            } else {
                aux = orderService.getOrdersById((ObjectId) comboBoxId.getValue());
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
