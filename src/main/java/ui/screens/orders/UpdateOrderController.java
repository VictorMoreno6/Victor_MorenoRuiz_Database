package ui.screens.orders;

import common.Constants;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Item;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import services.MenuItemsService;
import services.OrderItemService;
import services.OrderService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.security.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class UpdateOrderController extends BaseScreenController {
    private final OrderService orderService;
    private final MenuItemsService menuItemsService;
    private final OrderItemService orderItemService;
    public TableView<Order> ordersTable;
    public TableColumn<Integer, Order> idOrderColumn;
    public TableColumn<Timestamp, Order> dateOrderColumn;
    public TableColumn<Integer, Order> customerOrderColumn;
    public TableColumn<Integer, Order> tableOrderColumn;
    public TableView<OrderItem> itemsTable;
    public TableColumn<Integer, Item> idItemColumn;
    public TableColumn<String, Item> nameItemColumn;
    public TableColumn<Float, Item> priceItemColumn;
    public TableColumn<String, Item> descriptionItemColumn;
    public DatePicker dateField;
    public TextField tableOrderField;
    public TextField customerOrderField;
    public TextField menuItemField;
    public TextField quantityItemField;
    public ComboBox customerComboBox;
    public ComboBox itemsComboBox;
    @FXML
    public TableColumn<OrderItem, String> menuItemColumn;
    @FXML
    public TableColumn<OrderItem, Integer> quantityItemColumn;
    public ComboBox tableIdComboBox;
    private List<OrderItem> orderItems;
    private OrderItem selectedOrderItem;

    private int orderid;


    @Inject
    public UpdateOrderController(OrderService orderService, MenuItemsService menuItemsService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.menuItemsService = menuItemsService;
        this.orderItemService = orderItemService;
    }

    public void initialize() throws IOException {
        idOrderColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerOrderColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        tableOrderColumn.setCellValueFactory(new PropertyValueFactory<>("table_id"));

        ordersTable.setOnMouseClicked(this::handleTableClick);
        itemsTable.setOnMouseClicked(this::handleTableClick2);

        menuItemColumn.setCellValueFactory(cellData -> orderItemService.printMenuItemName(cellData.getValue()));
        quantityItemColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemsComboBox.getItems().addAll(menuItemsService.getMenuItemsName());
        customerComboBox.getItems().addAll(orderService.getIds());
        tableIdComboBox.getItems().addAll(orderService.getTableIds());

    }

    private void handleTableClick2(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            selectedOrderItem = itemsTable.getSelectionModel().getSelectedItem();
        }
    }

    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            if (orderItems != null)
                orderItems.clear();
            Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                tableIdComboBox.setValue(selectedOrder.getTable_id());
                customerComboBox.setValue(selectedOrder.getCustomer_id());
                dateField.setValue(LocalDate.from(selectedOrder.getDate()));
                orderItems = orderService.getOrderItems(selectedOrder.getId());
                orderid = selectedOrder.getId();
            }
            itemsTable.getItems().clear();
            itemsTable.getItems().addAll(orderItems);
        }
    }

    @Override
    public void principalCargado() throws IOException {
        setTables();
    }

    private void setTables() {
        ordersTable.getItems().clear();
        if (getPrincipalController().actualUser.getId() < 0) {
            orderService.getAll().peek(orders -> ordersTable.getItems().addAll(orders))
                    .peekLeft(orderError -> getPrincipalController().sacarAlertError(orderError.getMessage()));
        } else {
            ordersTable.getItems().addAll(orderService.getOrdersById(getPrincipalController().actualUser.getId()));
        }

        itemsTable.getItems().clear();
    }

    public void updateOrder(ActionEvent actionEvent) {
        Order o = new Order(orderid, dateField.getValue().atStartOfDay(), (Integer) customerComboBox.getValue(), (Integer) tableIdComboBox.getValue(), orderItems);
        if (orderService.update(o).isRight()) {
            getPrincipalController().sacarAlertInfo(Constants.ORDER_UPDATED_SUCCESSFULLY);
            orderItems.clear();
            tableIdComboBox.setValue(null);
            customerComboBox.setValue(null);
            dateField.setValue(null);
            setTables();
        } else {
            getPrincipalController().sacarAlertError("Error updating the customer");
        }
    }

    public void addItem() {
        if (quantityItemField.getText().isEmpty() || itemsComboBox.getValue() == null) {
            getPrincipalController().sacarAlertError(Constants.THERE_IS_AN_EMPTY_FIELD);
        } else {
            MenuItem mi = menuItemsService.getMenuItemByName((String) itemsComboBox.getValue());
            OrderItem oi = new OrderItem(mi, Integer.parseInt(quantityItemField.getText()));
            orderItems.add(oi);
            itemsTable.getItems().clear();
            itemsTable.getItems().addAll(orderItems);
        }
    }

    public void removeItem() {
        if (selectedOrderItem == null) {
            getPrincipalController().sacarAlertError("Any item is selected");
        } else {
            orderItems.remove(selectedOrderItem);
            itemsTable.getItems().clear();
            itemsTable.getItems().addAll(orderItems);
        }
    }
}
