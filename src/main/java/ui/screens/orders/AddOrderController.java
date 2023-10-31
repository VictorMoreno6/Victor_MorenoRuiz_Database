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
    public ComboBox customerIdCombo;
    @FXML
    private Button addButton;

    @FXML
    private TableColumn<OrderItem, Integer> idItemColumn;
    @FXML
    public TableColumn<OrderItem, String> nameItemColumn;
    @FXML
    public TableColumn<OrderItem, Item> quantityItemColumn;
    @FXML
    public ComboBox itemsComboBox;
    public TextField tableOrderField;

    public TextField quantityItemField;

    private List<OrderItem> orderItems;

    private OrderItem selectedOrderItem;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Inject
    public AddOrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    public void initialize() throws IOException {
        idItemColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameItemColumn.setCellValueFactory(cellData -> orderItemService.printMenuItemName(cellData.getValue()));
        quantityItemColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        itemsTable.setOnMouseClicked(this::handleTableClick);
        customerIdCombo.getItems().addAll(orderService.getIds());
        //itemsComboBox.getItems().addAll(orderItemService.getMenuItemsName(orderService.getIds()));
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
        if ( tableOrderField.getText().isEmpty() || customerIdCombo.getValue() == null) {
            getPrincipalController().sacarAlertError(Constants.THERE_IS_AN_EMPTY_FIELD);
        } else {
            Order o = new Order(orderService.autoId(), LocalDateTime.now(), (Integer) customerIdCombo.getValue(), Integer.parseInt(tableOrderField.getText()));
            if (orderService.save(o).isRight()) {
                getPrincipalController().sacarAlertInfo(Constants.ORDER_ADDED_SUCCESSFULLY);
                if (orderItemService.save(orderItems, o).isRight()){
                    getPrincipalController().sacarAlertInfo(Constants.ORDER_ITEMS_ADDED_SUCCESSFULLY);
                } else
                    getPrincipalController().sacarAlertError(Constants.ERROR_ADDING_ORDER_ITEMS);
                orderItems.clear();
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
            MenuItem mi = new MenuItem(0, (String) itemsComboBox.getValue(), "", 0);
            OrderItem oi = new OrderItem(0, 0, mi, Integer.parseInt(quantityItemField.getText()));
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
