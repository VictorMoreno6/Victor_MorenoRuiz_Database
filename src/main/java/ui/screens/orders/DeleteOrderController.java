package ui.screens.orders;

import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Item;
import model.Order;
import model.OrderItem;
import services.OrderItemService;
import services.OrderService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class DeleteOrderController extends BaseScreenController {
    public TableView<Order> ordersTable;
    public TableColumn<Order, Integer> idOrderColumn;
    public TableColumn<Order, LocalDateTime> dateOrderColumn;
    public TableColumn<Order, Integer> customerOrderColumn;
    public TableColumn<Order, Integer> tableOrderColumn;
    public TableView<OrderItem> itemsTable;
    public TableColumn<Integer, Item> idItemColumn;
    public TableColumn<String, Item> nameItemColumn;
    public TableColumn<Float, Item> priceItemColumn;
    public TableColumn<String, Item> descriptionItemColumn;
    public TableColumn<OrderItem, String>  menuItemColumn;
    public TableColumn<OrderItem, Integer> quantityItemColumn;
    private Order selectedOrder;
    private List<OrderItem> orderItems;

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Inject
    public DeleteOrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    public void initialize() throws IOException {
        idOrderColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerOrderColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        tableOrderColumn.setCellValueFactory(new PropertyValueFactory<>("table_id"));

        menuItemColumn.setCellValueFactory(cellData -> orderItemService.printMenuItemName(cellData.getValue()));
        quantityItemColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        ordersTable.setOnMouseClicked(this::handleTableClick);
    }

    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (orderItems != null)
                orderItems.clear();
            orderItems = orderService.getOrderItems(selectedOrder.getId());
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
        itemsTable.getItems().clear();
        orderService.getAll().peek(orders -> ordersTable.getItems().addAll(orders))
                .peekLeft(orderError -> getPrincipalController().sacarAlertError(orderError.getMessage()));
    }

    public void deleteOrder(ActionEvent actionEvent) {
        if (selectedOrder == null) {
            getPrincipalController().sacarAlertError("No order selected");
        } else{
            Order o = new Order(selectedOrder.getId(), selectedOrder.getDate(), selectedOrder.getCustomer_id(), selectedOrder.getTable_id(), orderItems);
            if (orderService.delete(o).isRight()){
                getPrincipalController().sacarAlertInfo("Order deleted");
                setTables();
            } else
                getPrincipalController().sacarAlertError("Error deleting order");
        }
    }

}
