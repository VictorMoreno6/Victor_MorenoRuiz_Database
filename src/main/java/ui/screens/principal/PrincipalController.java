package ui.screens.principal;


import common.Constants;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.log4j.Log4j2;
import model.User;
import ui.screens.common.BaseScreenController;
import ui.screens.common.Screens;

import java.io.IOException;
import java.util.Optional;

@Log4j2
public class PrincipalController {
    private final Alert alert;
    public User actualUser;
    @FXML
    public BorderPane root;
    Instance<Object> instance;
    private Stage primaryStage;
    @FXML
    private MenuBar menuPrincipal;


    @Inject
    public PrincipalController(Instance<Object> instance) {
        this.instance = instance;
        alert = new Alert(Alert.AlertType.NONE);
    }

    private void cargarPantalla(Screens pantalla) {
        cambioPantalla(cargarPantalla(pantalla.getRuta()));
    }

    private Pane cargarPantalla(String ruta) {
        Pane panePantalla = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(controller -> instance.select(controller).get());
            panePantalla = fxmlLoader.load(getClass().getResourceAsStream(ruta));
            BaseScreenController pantallaController = fxmlLoader.getController();
            pantallaController.setPrincipalController(this);
            pantallaController.principalCargado();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return panePantalla;
    }

    public boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType continueButton = new ButtonType("Continue", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(continueButton, cancelButton);

        // Muestra el diálogo y espera a que el usuario elija una opción
        return alert.showAndWait().orElse(cancelButton) == continueButton;
    }

    public void sacarAlertError(String mensaje) {
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.getDialogPane().setId("alert");
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("btn-ok");
        alert.showAndWait();
    }

    public void sacarAlertInfo(String mensaje) {
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.getDialogPane().setId("alert");
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("btn-ok");
        alert.showAndWait();
    }

    public void onLoginDone(User user) {
        actualUser = user;
        menuPrincipal.setVisible(true);
        cargarPantalla(Screens.WELCOME);
    }


    public void logout() {
        actualUser = null;
        menuPrincipal.setVisible(false);
        cargarPantalla(Screens.LOGIN);
    }

    private void cambioPantalla(Pane pantallaNueva) {
        root.setCenter(pantallaNueva);
    }


    public void initialize() {
        menuPrincipal.setVisible(false);
        cargarPantalla(Screens.WELCOME);
        menuPrincipal.setVisible(true);
    }

    //////__________________________________________________________________________________////////////////////////////////////////

    private void closeWindowEvent(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle(Constants.QUIT_APPLICATION);
        alert.setContentText(Constants.CLOSE_WITHOUT_SAVING);
        alert.initOwner(primaryStage.getOwner());
        Optional<ButtonType> res = alert.showAndWait();


        res.ifPresent(buttonType -> {
            if (buttonType == ButtonType.CANCEL) {
                event.consume();
            }
        });
    }

    public void exit(ActionEvent actionEvent) {
        primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    public void menuCustomers(ActionEvent actionEvent) {
        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case "listCustomers":
                cargarPantalla(Screens.CUSTOMERS);
                break;
            case "addCustomer":
                cargarPantalla(Screens.ADDCUSTOMERS);
                break;
            case "updateCustomer":
                cargarPantalla(Screens.UPDATECUSTOMERS);
                break;
            case "deleteCustomer":
                cargarPantalla(Screens.DELETECUSTOMERS);
                break;
        }
    }

    public void menuOrders(ActionEvent actionEvent) {
        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case "listOrders":
                cargarPantalla(Screens.ORDERS);
                break;
            case "addOrder":
                cargarPantalla(Screens.ADDORDERS);
                break;
            case "updateOrder":
                cargarPantalla(Screens.UPDATEORDERS);
                break;
            case "deleteOrder":
                cargarPantalla(Screens.DELETEORDERS);
                break;
        }
    }
}
