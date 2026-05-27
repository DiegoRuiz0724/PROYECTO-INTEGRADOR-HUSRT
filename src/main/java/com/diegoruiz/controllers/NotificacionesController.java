package com.diegoruiz.controllers;

import com.diegoruiz.App;
import com.diegoruiz.entities.Notificacion;
import com.diegoruiz.entities.Usuario;
import com.diegoruiz.service.NotificacionService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.time.format.DateTimeFormatter;

public class NotificacionesController {

    @FXML private ListView<Notificacion> listNotificaciones;
    private final NotificacionService notificacionService = new NotificacionService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        listNotificaciones.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Notificacion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox container = new VBox(5);
                    Label title = new Label(item.getTitulo());
                    title.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
                    
                    Label msg = new Label(item.getMensaje());
                    msg.setWrapText(true);
                    
                    Label date = new Label(item.getFecha().format(formatter));
                    date.setStyle("-fx-font-size: 11; -fx-text-fill: #94a3b8;");

                    if (!item.isLeida()) {
                        container.setStyle("-fx-background-color: #f0f9ff; -fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #bae6fd; -fx-border-width: 0 0 0 4;");
                    } else {
                        container.setStyle("-fx-padding: 10; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");
                    }

                    container.getChildren().addAll(title, msg, date);
                    setGraphic(container);
                }
            }
        });

        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        Usuario user = App.getUsuarioLogueado();
        if (user != null && user.getDocumento() != null) {
            listNotificaciones.setItems(FXCollections.observableArrayList(
                notificacionService.obtenerMisNotificaciones(user.getDocumento())
            ));
        }
    }

    @FXML
    public void onMarcarTodasLeidas() {
        Usuario user = App.getUsuarioLogueado();
        if (user != null && user.getDocumento() != null) {
            notificacionService.obtenerMisNotificaciones(user.getDocumento()).forEach(n -> {
                if (!n.isLeida()) notificacionService.marcarComoLeida(n.getIdNotificacion());
            });
            cargarNotificaciones();
        }
    }
}
