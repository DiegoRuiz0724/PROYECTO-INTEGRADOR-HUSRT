package com.diegoruiz;

import com.diegoruiz.entities.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;
    private static Usuario usuarioLogueado;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        // Inicializar datos maestros al arrancar
        try {
            com.diegoruiz.util.DataInitializer.seed();
        } catch (Exception e) {
            System.err.println("Error al inicializar datos: " + e.getMessage());
        }

        primaryStage.setTitle("HUSRT — Control de Prácticas Asistenciales");
        showLogin();
    }

    public static void showLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/Login.fxml"));
        ScrollPane loginPane = loader.load();
        Scene scene = new Scene(loginPane, 1000, 680);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void showRegister() throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/Register.fxml"));
        ScrollPane registerPane = loader.load();
        Scene scene = new Scene(registerPane, 1000, 680);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void showMainLayout() throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/MainLayout.fxml"));
        BorderPane rootLayout = loader.load();
        Scene scene = new Scene(rootLayout, 1000, 680);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void setUsuarioLogueado(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
