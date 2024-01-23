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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddOrderController extends BaseScreenController {
    public TableView<OrderItem> itemsTable;
    @FXML
    public ComboBox tableIdCombo;
    @FXML
    private Button addButton;
    @FXML
    public TableColumn<OrderItem, String> nameItemColumn;
    @FXML
    public TableColumn<OrderItem, Item> quantityItemColumn;
    @FXML
    public ComboBox itemsComboBox;

    public TextField quantityItemField;

    private List<OrderItem> orderItems;

    private OrderItem selectedOrderItem;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    private final MenuItemsService menuItemsService;

    @Inject
    public AddOrderController(OrderService orderService, OrderItemService orderItemService, MenuItemsService menuItemsService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.menuItemsService = menuItemsService;
    }

    public void initialize() throws IOException {
        nameItemColumn.setCellValueFactory(cellData -> orderItemService.printMenuItemName(cellData.getValue()));
        quantityItemColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        itemsTable.setOnMouseClicked(this::handleTableClick);
        tableIdCombo.getItems().addAll(orderService.getTableIds());
        itemsComboBox.getItems().addAll(menuItemsService.getMenuItemsName());
    }

    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            selectedOrderItem = itemsTable.getSelectionModel().getSelectedItem();
        }

    }

    @Override
    public void principalCargado() throws IOException {
        setTable();
    }

    private void setTable() {
        itemsTable.getItems().clear();
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        itemsTable.getItems().addAll(orderItems);
    }

    public void addOrder(ActionEvent actionEvent) {
        if ( tableIdCombo.getValue() == null) {
            getPrincipalController().sacarAlertError(Constants.THERE_IS_AN_EMPTY_FIELD);
        } else {
            Order o = new Order(orderService.autoId(), LocalDateTime.now(), getPrincipalController().actualUser.getId(), (Integer) tableIdCombo.getValue(), orderItems);
            if (orderService.save(o).isRight()) {
                getPrincipalController().sacarAlertInfo(Constants.ORDER_ADDED_SUCCESSFULLY);
                orderItems.clear();
                tableIdCombo.setValue(null);
                quantityItemField.clear();
                itemsComboBox.setValue(null);
                setTable();
            } else {
                getPrincipalController().sacarAlertError(Constants.ERROR_ADDING_ORDER);
            }
        }
    }

    public void addItem() {
        if (quantityItemField.getText().isEmpty() || itemsComboBox.getValue() == null) {
            getPrincipalController().sacarAlertError(Constants.THERE_IS_AN_EMPTY_FIELD);
        } else {
            MenuItem mi = menuItemsService.getMenuItemByName((String) itemsComboBox.getValue());
            OrderItem oi = new OrderItem(mi, Integer.parseInt(quantityItemField.getText()));
            orderItems.add(oi);
            setTable();
        }
    }

    public void removeItem(ActionEvent actionEvent) {
        if (selectedOrderItem == null) {
            getPrincipalController().sacarAlertError("Any item is selected");
        } else {
            orderItems.remove(selectedOrderItem);
            setTable();
        }
    }

}
