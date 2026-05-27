package com.diegoruiz.controllers;

import com.diegoruiz.entities.Estudiante;
import com.diegoruiz.service.ReportesService;
import com.diegoruiz.service.ReportesService.DashboardStats;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class ReportesController {

    // Dashboard
    @FXML
    private Label lblTotalRegistrados;
    @FXML
    private Label lblDentroAhora;
    @FXML
    private Label lblAlertasArl;
    @FXML
    private Label lblRechazosHoy;

    // Alertas ARL
    @FXML
    private TableView<Estudiante> tablaAlertas;
    @FXML
    private TableColumn<Estudiante, String> colAlerNombres;
    @FXML
    private TableColumn<Estudiante, String> colAlerDocumento;
    @FXML
    private TableColumn<Estudiante, LocalDate> colAlerArl;

    // Horas estudiante
    @FXML
    private TextField txtDocHoras;
    @FXML
    private TextField txtHorasReq;
    @FXML
    private Label lblResultadoHoras;

    private final ReportesService reportesService = new ReportesService();

    @FXML
    public void initialize() {
        colAlerNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colAlerDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colAlerArl.setCellValueFactory(new PropertyValueFactory<>("arlVencimiento"));

        try {
            actualizarDashboard();
        } catch (Exception e) {
            System.err.println("Error al inicializar dashboard: " + e.getMessage());
            lblTotalRegistrados.setText("Error");
            lblDentroAhora.setText("Error");
            lblAlertasArl.setText("Error");
            lblRechazosHoy.setText("Error");
        }
    }

    @FXML
    public void onActualizarDashboard() {
        try {
            actualizarDashboard();
        } catch (Exception e) {
            System.err.println("Error al actualizar dashboard: " + e.getMessage());
        }
    }

    @FXML
    public void onConsultarHoras() {
        String doc = txtDocHoras.getText() != null ? txtDocHoras.getText().trim() : "";
        if (doc.isEmpty()) {
            lblResultadoHoras.setText("Ingrese el documento.");
            return;
        }
        double req = 40;
        try {
            req = Double.parseDouble(txtHorasReq.getText().trim());
        } catch (Exception ignored) {
        }

        try {
            ReportesService.ResumenHoras r = reportesService.resumenHorasEstudiante(doc, req);
            lblResultadoHoras.setText(String.format(
                    "Horas cumplidas: %.2f / %.0f  (%.1f%%)",
                    r.horasCumplidas, r.horasRequeridas, r.porcentajeCumplimiento));
        } catch (Exception e) {
            lblResultadoHoras.setText("Error al consultar horas.");
        }
    }

    private void actualizarDashboard() {
        DashboardStats stats = reportesService.obtenerEstadisticasDashboard();
        lblTotalRegistrados.setText(String.valueOf(stats.totalEstudiantesRegistrados));
        lblDentroAhora.setText(String.valueOf(stats.estudiantesDentroAhora));
        lblAlertasArl.setText(String.valueOf(stats.alertasArlHoy));
        lblRechazosHoy.setText(String.valueOf(stats.rechazosHoy));

        List<Estudiante> alertas = reportesService.alertasArlProximaVencer();
        tablaAlertas.setItems(FXCollections.observableArrayList(alertas));
    }
}
