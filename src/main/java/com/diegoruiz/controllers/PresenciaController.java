package com.diegoruiz.controllers;

import com.diegoruiz.App;
import com.diegoruiz.entities.Estudiante;
import com.diegoruiz.entities.Horario;
import com.diegoruiz.entities.Usuario;
import com.diegoruiz.enums.Rol;
import com.diegoruiz.service.PlanPracticasService;
import com.diegoruiz.service.PorteriaService;
import com.diegoruiz.service.PorteriaService.PresenciaInfo;
import com.diegoruiz.service.RegistroService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PresenciaController {

    @FXML
    private Label lblTotal;
    @FXML
    private Label lblEstadoEstudiante;
    @FXML
    private TableView<PresenciaInfo> tablaPresencia;
    @FXML
    private TableColumn<PresenciaInfo, String> colDocumento;
    @FXML
    private TableColumn<PresenciaInfo, String> colNombre;
    @FXML
    private TableColumn<PresenciaInfo, String> colServicio;
    @FXML
    private TableColumn<PresenciaInfo, LocalDateTime> colEntrada;

    private final PorteriaService porteriaService = new PorteriaService();
    private final RegistroService registroService = new RegistroService();
    private final PlanPracticasService planService = new PlanPracticasService();

    @FXML
    public void initialize() {
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
        colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));

        // Formatear la hora de entrada para que se vea mejor
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss (dd/MM)");
        colEntrada.setCellFactory(column -> new TableCell<PresenciaInfo, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        actualizarPanel();

        // Refrescar automáticamente cada 30 segundos (RNF-03)
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), e -> actualizarPanel()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    public void onRefrescar() {
        actualizarPanel();
    }

    private void actualizarPanel() {
        Usuario usuario = App.getUsuarioLogueado();
        List<PresenciaInfo> lista = new ArrayList<>(porteriaService.obtenerPresenciaActual());

        if (usuario != null && usuario.getRol() == Rol.ESTUDIANTE) {
            String doc = usuario.getDocumento();
            if (doc != null && !doc.isEmpty()) {
                // Filtrar tabla solo para el estudiante
                lista = lista.stream()
                        .filter(p -> p.getDocumento().equals(doc))
                        .collect(Collectors.toList());

                // Mostrar estado de horario
                verificarEstadoHorario(doc);
            }
            lblTotal.setText("Mi estado actual en el sistema");
        } else {
            lblTotal.setText("Estudiantes dentro ahora: " + porteriaService.totalEstudiantesDentro());
            if (lblEstadoEstudiante != null)
                lblEstadoEstudiante.setText("");
        }

        tablaPresencia.setItems(FXCollections.observableArrayList(lista));
    }

    private void verificarEstadoHorario(String documento) {
        if (lblEstadoEstudiante == null)
            return;

        Optional<Estudiante> optEst = registroService.buscarPorDocumento(documento);
        if (optEst.isPresent()) {
            Optional<Horario> optHorario = planService
                    .buscarHorarioVigenteParaEstudiante(optEst.get().getIdEstudiante());
            if (optHorario.isPresent()) {
                Horario h = optHorario.get();
                lblEstadoEstudiante.setText("✅ EN HORARIO: " + h.getHoraInicio() + " - " + h.getHoraFin() +
                        " ("
                        + (h.getPractica() != null && h.getPractica().getServicioHospitalario() != null
                                ? h.getPractica().getServicioHospitalario().getNombre()
                                : "N/A")
                        + ")");
                lblEstadoEstudiante.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;");
            } else {
                lblEstadoEstudiante.setText("❌ FUERA DE HORARIO: No tienes prácticas asignadas en este momento.");
                lblEstadoEstudiante.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
            }
        }
    }
}
