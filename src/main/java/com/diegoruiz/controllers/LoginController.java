package com.diegoruiz.controllers;

import com.diegoruiz.App;
import com.diegoruiz.entities.Usuario;
import com.diegoruiz.impl.UsuarioDAOimpl;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblError;

    private final UsuarioDAOimpl userDAO = new UsuarioDAOimpl();

    @FXML
    public void onLogin() {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        if (user == null || user.isEmpty() || pass == null || pass.isEmpty()) {
            lblError.setText("Por favor complete todos los campos");
            return;
        }

        Usuario usuario = userDAO.buscarPorUsername(user);

        if (usuario != null && usuario.getPassword().equals(pass)) {
            
            App.setUsuarioLogueado(usuario);
            try {
                App.showMainLayout();
            } catch (Exception e) {
                lblError.setText("Error al cargar la aplicación principal");
                e.printStackTrace();
            }
        } else {
            lblError.setText("Usuario o contraseña incorrectos");
        }
    }

    @FXML
    public void onGoToRegister() {
        try {
            App.showRegister();
        } catch (Exception e) {
            lblError.setText("Error al cargar la pantalla de registro");
            e.printStackTrace();
        }
    }
}
