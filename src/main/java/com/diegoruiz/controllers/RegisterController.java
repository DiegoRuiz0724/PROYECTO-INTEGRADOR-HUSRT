package com.diegoruiz.controllers;

import com.diegoruiz.App;
import com.diegoruiz.entities.Usuario;
import com.diegoruiz.enums.Rol;
import com.diegoruiz.impl.EstudianteDAOimpl;
import com.diegoruiz.impl.UsuarioDAOimpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RegisterController {

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtDocumento;
    @FXML
    private ComboBox<Rol> cbRol;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private PasswordField txtCodigoAutorizacion;
    @FXML
    private VBox containerCodigo;
    @FXML
    private Label lblMensaje;

    private final UsuarioDAOimpl userDAO = new UsuarioDAOimpl();
    private final EstudianteDAOimpl estudianteDAO = new EstudianteDAOimpl();
    private static final String CODIGO_ADMIN = "HUSRT2026"; // Código secreto

    @FXML
    public void initialize() {
        cbRol.setItems(FXCollections.observableArrayList(Rol.values()));

        // Mostrar/Ocultar campo de código según el rol seleccionado
        cbRol.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == Rol.ADMIN || newVal == Rol.PORTERO) {
                containerCodigo.setVisible(true);
                containerCodigo.setManaged(true);
            } else {
                containerCodigo.setVisible(false);
                containerCodigo.setManaged(false);
                txtCodigoAutorizacion.clear();
            }
        });
    }

    @FXML
    public void onRegister() {
        String username = txtUsername.getText();
        String documento = txtDocumento.getText();
        Rol rol = cbRol.getValue();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String codigo = txtCodigoAutorizacion.getText();

        if (username == null || username.isEmpty() || rol == null ||
                password == null || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
            mostrarMensaje("Por favor complete los campos obligatorios", false);
            return;
        }

        // VALIDACIÓN DE SEGURIDAD: Solo estudiantes registrados por el admin pueden
        // crear cuenta
        if (rol == Rol.ESTUDIANTE) {
            if (documento == null || documento.isEmpty()) {
                mostrarMensaje("El documento es obligatorio para validar su registro", false);
                return;
            }
            // Verificar si el estudiante existe en la base de datos académica
            if (estudianteDAO.buscarPorDocumento(documento) == null) {
                mostrarMensaje("Usted no está registrado en el sistema. Contacte al administrador.", false);
                return;
            }
        }

        // Validación de código para roles administrativos
        if ((rol == Rol.ADMIN || rol == Rol.PORTERO) && !CODIGO_ADMIN.equals(codigo)) {
            mostrarMensaje("Código de autorización incorrecto", false);
            return;
        }

        if (!password.equals(confirmPassword)) {
            mostrarMensaje("Las contraseñas no coinciden", false);
            return;
        }

        if (userDAO.buscarPorUsername(username) != null) {
            mostrarMensaje("El nombre de usuario ya existe", false);
            return;
        }

        try {
            // Guardar solo el Usuario de acceso
            Usuario nuevoUsuario = new Usuario(username, password, rol, documento);
            userDAO.guardar(nuevoUsuario);

            mostrarMensaje("Cuenta creada con éxito. Ya puede iniciar sesión.", true);
            limpiarCampos();
        } catch (Exception e) {
            mostrarMensaje("Error al registrar: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    @FXML
    public void onBackToLogin() {
        try {
            App.showLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarMensaje(String msj, boolean exito) {
        lblMensaje.setText(msj);
        lblMensaje.setTextFill(exito ? Color.GREEN : Color.RED);
    }

    private void limpiarCampos() {
        txtUsername.clear();
        txtDocumento.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
        txtCodigoAutorizacion.clear();
        cbRol.getSelectionModel().clearSelection();
    }
}
