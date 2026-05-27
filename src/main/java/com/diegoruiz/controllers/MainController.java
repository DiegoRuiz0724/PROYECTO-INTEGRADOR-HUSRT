package com.diegoruiz.controllers;

import com.diegoruiz.App;
import com.diegoruiz.entities.Usuario;
import com.diegoruiz.enums.Rol;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * Controlador del layout principal.
 * Gestiona la navegación entre las pantallas del menú lateral.
 */
public class MainController {

    @FXML
    private StackPane rootPane;
    @FXML
    private Button btnPorteria;
    @FXML
    private Button btnRegistro;
    @FXML
    private Button btnPlanPracticas;
    @FXML
    private Button btnPresencia;
    @FXML
    private Button btnReportes;
    @FXML
    private Button btnNotificaciones;

    @FXML
    public void initialize() {
        aplicarPermisos();

        // Cargar la pantalla inicial permitida según el rol
        Usuario user = App.getUsuarioLogueado();
        if (user != null) {
            switch (user.getRol()) {
                case PORTERO:
                    mostrarPorteria();
                    break;
                case ESTUDIANTE:
                    mostrarPresencia();
                    break;
                case DOCENTE:
                    mostrarPlanPracticas();
                    break;
                case ADMIN:
                default:
                    mostrarPorteria();
                    break;
            }
        }
    }

    private void aplicarPermisos() {
        Usuario user = App.getUsuarioLogueado();
        if (user == null)
            return;

        Rol rol = user.getRol();

        // Configuración de permisos por rol
        // setVisible(false) oculta el botón
        // setManaged(false) elimina el espacio que ocupaba en el layout
        switch (rol) {
            case ADMIN:
                // Admin tiene acceso a todo
                break;
            case PORTERO:
                ocultarBoton(btnRegistro);
                ocultarBoton(btnPlanPracticas);
                ocultarBoton(btnReportes);
                break;
            case ESTUDIANTE:
                ocultarBoton(btnPorteria);
                ocultarBoton(btnRegistro);
                ocultarBoton(btnPlanPracticas);
                ocultarBoton(btnReportes);
                break;
            case DOCENTE:
                ocultarBoton(btnPorteria);
                ocultarBoton(btnRegistro);
                ocultarBoton(btnReportes);
                break;
        }
    }

    private void ocultarBoton(Button btn) {
        if (btn != null) {
            btn.setVisible(false);
            btn.setManaged(false);
        }
    }

    @FXML
    public void onLogout() {
        try {
            App.setUsuarioLogueado(null);
            App.showLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void mostrarPorteria() {
        cargarVista("/fxml/Porteria.fxml");
    }

    @FXML
    public void mostrarRegistroEstudiantes() {
        cargarVista("/fxml/RegistroEstudiantes.fxml");
    }

    @FXML
    public void mostrarPresencia() {
        cargarVista("/fxml/Presencia.fxml");
    }

    @FXML
    public void mostrarReportes() {
        cargarVista("/fxml/Reportes.fxml");
    }

    @FXML
    public void mostrarPlanPracticas() {
        cargarVista("/fxml/PlanPracticas.fxml");
    }

    @FXML
    public void mostrarNotificaciones() {
        cargarVista("/fxml/Notificaciones.fxml");
    }

    private void cargarVista(String ruta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Pane vista = loader.load();
            rootPane.getChildren().setAll(vista);
        } catch (Exception e) {
            System.err.println("ERROR CARGANDO VISTA " + ruta);
            e.printStackTrace();

            Throwable cause = e.getCause();
            String detalle = e.getMessage();
            while (cause != null) {
                detalle += "\nCausa: " + cause.getMessage();
                cause = cause.getCause();
            }

            mostrarAlertaError("Error al cargar la vista",
                    "No se pudo cargar la pantalla: " + ruta + "\n\n" +
                            "Posible causa: Error en el archivo FXML o en el initialize() del controlador.\n\n" +
                            "Detalle técnico:\n" + detalle);
        }
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
