package com.diegoruiz.controllers;

import com.diegoruiz.dto.ValidacionAccesoDTO;
import com.diegoruiz.entities.Estudiante;
import com.diegoruiz.service.PorteriaService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import java.util.Optional;

/**
 * RF-04 — Control de Acceso (portería).
 * RF-05 — Registro de salida.
 * RF-06 — Panel de presencia en tiempo real.
 */
public class PorteriaController {

    @FXML
    private TextField txtDocumento;
    @FXML
    private Label lblEstadoAcceso;
    @FXML
    private Label lblMotivoRechazo;
    @FXML
    private Label lblNombreEstudiante;
    @FXML
    private Label lblPrograma;
    @FXML
    private Label lblSemestre;
    @FXML
    private Label lblServicio;
    @FXML
    private Label lblDocente;
    @FXML
    private Label lblFranja;
    @FXML
    private Circle statusCircle;
    @FXML
    private VBox panelResultado;
    @FXML
    private HBox statusBanner;

    private final PorteriaService porteriaService = new PorteriaService();

    @FXML
    public void initialize() {
        panelResultado.setVisible(false);
        txtDocumento.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() >= 5) {
                mostrarPrevisualizacion(newVal);
            } else {
                panelResultado.setVisible(false);
            }
        });
    }

    @FXML
    public void onRegistrarIngreso() {
        String doc = txtDocumento.getText();
        if (doc == null || doc.isEmpty())
            return;

        try {
            ValidacionAccesoDTO result = porteriaService.validarIngreso(doc);
            actualizarUI(result);
        } catch (Exception e) {
            lblEstadoAcceso.setText("ERROR SISTEMA");
            lblMotivoRechazo.setText(e.getMessage());
            statusCircle.setFill(Color.web("#ef4444"));
        }
    }

    @FXML
    public void onRegistrarSalida() {
        String doc = txtDocumento.getText();
        if (doc == null || doc.isEmpty())
            return;

        try {
            double horas = porteriaService.registrarSalida(doc);
            if (horas >= 0) {
                lblEstadoAcceso.setText("SALIDA REGISTRADA");
                lblMotivoRechazo.setText("Tiempo cumplido: " + String.format("%.1f", horas) + " horas");
                statusCircle.setFill(Color.web("#3b82f6")); // Azul Salida
                statusBanner.setStyle(
                        "-fx-background-color: #dbeafe; -fx-padding: 25; -fx-alignment: center-left; -fx-background-radius: 15 15 0 0;");
                panelResultado.setVisible(true);
            } else {
                lblEstadoAcceso.setText("SIN ENTRADA ACTIVA");
                lblMotivoRechazo.setText("No se encontró un ingreso previo para este documento.");
                statusCircle.setFill(Color.web("#f59e0b"));
                statusBanner.setStyle(
                        "-fx-background-color: #fef3c7; -fx-padding: 25; -fx-alignment: center-left; -fx-background-radius: 15 15 0 0;");
                panelResultado.setVisible(true);
            }
        } catch (Exception e) {
            lblEstadoAcceso.setText("ERROR SISTEMA");
            lblMotivoRechazo.setText(e.getMessage());
            statusCircle.setFill(Color.web("#ef4444"));
        }
    }

    private void actualizarUI(ValidacionAccesoDTO result) {
        panelResultado.setVisible(true);
        lblNombreEstudiante.setText(result.isAprobado() ? result.getNombreEstudiante() : "---");
        lblEstadoAcceso.setText(result.isAprobado() ? "ACCESO APROBADO" : "ACCESO DENEGADO");
        lblMotivoRechazo.setText(result.isAprobado() ? "" : result.getMotivoRechazo());

        if (result.isAprobado()) {
            statusCircle.setFill(Color.web("#10b981")); // Verde Tailwind
            statusBanner.setStyle(
                    "-fx-background-color: #dcfce7; -fx-padding: 25; -fx-alignment: center-left; -fx-background-radius: 15 15 0 0;");
            lblPrograma.setText(result.getPrograma());
            lblServicio.setText(result.getServicio());
            lblSemestre.setText(result.getSemestre());
            lblDocente.setText(result.getNombreDocente());
            lblFranja.setText(result.getFranjaHoraria());
        } else {
            statusCircle.setFill(Color.web("#ef4444")); // Rojo Tailwind
            statusBanner.setStyle(
                    "-fx-background-color: #fee2e2; -fx-padding: 25; -fx-alignment: center-left; -fx-background-radius: 15 15 0 0;");
            lblPrograma.setText("---");
            lblServicio.setText("---");
            lblSemestre.setText("---");
            lblDocente.setText("---");
            lblFranja.setText("---");
        }
    }

    private void mostrarPrevisualizacion(String documento) {
        try {
            Optional<PorteriaService.PresenciaInfo> opt = porteriaService.consultarPresencia(documento);
            if (opt.isPresent()) {
                PorteriaService.PresenciaInfo p = opt.get();
                panelResultado.setVisible(true);
                lblNombreEstudiante.setText(p.getNombreEstudiante());
                lblServicio.setText(p.getServicio());
                lblEstadoAcceso.setText("DENTRO DEL HOSPITAL");
                lblMotivoRechazo.setText("Ingreso: " + p.getHoraEntrada().toLocalTime().toString().substring(0, 5));
                statusCircle.setFill(Color.web("#f59e0b")); // Ambar
                statusBanner.setStyle(
                        "-fx-background-color: #fef3c7; -fx-padding: 25; -fx-alignment: center-left; -fx-background-radius: 15 15 0 0;");
            }
        } catch (Exception e) {
            System.err.println("Error en previsualización: " + e.getMessage());
        }
    }
}
