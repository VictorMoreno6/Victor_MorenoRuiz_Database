package ui.screens.orders;

import common.Constants;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Item;
import model.Order;
import services.OrderService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.security.Timestamp;
import java.time.LocalDateTime;

public class DeleteOrderController extends BaseScreenController {
    public TableView<Order> ordersTable;
    public TableColumn<Order, Integer> idOrderColumn;
    public TableColumn<Order, LocalDateTime> dateOrderColumn;
    public TableColumn<Order, Integer> customerOrderColumn;
    public TableColumn<Order, Integer> tableOrderColumn;
    public TableView<Item> itemsTable;
    public TableColumn<Integer, Item> idItemColumn;
    public TableColumn<String, Item> nameItemColumn;
    public TableColumn<Float, Item> priceItemColumn;
    public TableColumn<String, Item> descriptionItemColumn;
    private Order selectedOrder;

    private final OrderService orderService;

    @Inject
    public DeleteOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void initialize() throws IOException {
        idOrderColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerOrderColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        tableOrderColumn.setCellValueFactory(new PropertyValueFactory<>("table_id"));
        idItemColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameItemColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceItemColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionItemColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        ordersTable.setOnMouseClicked(this::handleTableClick);
    }

    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        }
    }

    @Override
    public void principalCargado() throws IOException {
        setTables();
    }

    private void setTables() {
        ordersTable.getItems().clear();
        orderService.getAll().peek(orders -> ordersTable.getItems().addAll(orders))
                .peekLeft(orderError -> getPrincipalController().sacarAlertError(orderError.getMessage()));
    }

    public void deleteOrder(ActionEvent actionEvent) {
        if (selectedOrder == null) {
            getPrincipalController().sacarAlertError("No order selected");
        } else
            getPrincipalController().sacarAlertError("Select a customer to delete");

    }

   /* public void deleteCustomer(ActionEvent actionEvent) {
        if (selectedCustomer != null){
            servicesCustomers.delete(selectedCustomer, false).peek(result -> {
                if (result) {
                    if (getPrincipalController().showConfirmationDialog("Delete", "Are you sure you want to continue?, If you delete the customer, all orders they have will also be deleted.")) {
                        servicesCustomers.delete(selectedCustomer, true);
                    }
                    setTables();
                }
            }).peekLeft(customerError -> getPrincipalController().sacarAlertError(customerError.getMessage()));
        }
        else
            getPrincipalController().sacarAlertError("Select a customer to delete");
    }*/
}
