package com.diegoruiz.controllers;

import com.diegoruiz.entities.*;
import com.diegoruiz.impl.*;
import com.diegoruiz.service.PlanPracticasService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalTime;
import java.util.List;

public class PlanPracticasController {

    @FXML
    private ComboBox<Docente> cbDocente;
    @FXML
    private ComboBox<Programa> cbPrograma;
    @FXML
    private ComboBox<ServicioHospitalario> cbServicio;
    @FXML
    private TextField txtMes;
    @FXML
    private TextField txtAnio;
    @FXML
    private ComboBox<String> cbDiaSemana;
    @FXML
    private TextField txtHoraInicio;
    @FXML
    private TextField txtHoraFin;
    @FXML
    private Label lblMensaje;

    @FXML
    private TableView<Horario> tablaHorarios;
    @FXML
    private TableColumn<Horario, String> colDia;
    @FXML
    private TableColumn<Horario, LocalTime> colInicio;
    @FXML
    private TableColumn<Horario, LocalTime> colFin;
    @FXML
    private TableColumn<Horario, String> colPrograma;
    @FXML
    private TableColumn<Horario, String> colServicio;

    private final PlanPracticasService planService = new PlanPracticasService();
    private final DocenteDAOimpl docenteDAO = new DocenteDAOimpl();
    private final ServicioHospitalarioDAOimpl servicioDAO = new ServicioHospitalarioDAOimpl();
    private final ProgramaDAOimpl programaDAO = new ProgramaDAOimpl();
    private final com.diegoruiz.service.NotificacionService notificacionService = new com.diegoruiz.service.NotificacionService();
    private final EstudianteDAOimpl estudianteDAO = new EstudianteDAOimpl();

    @FXML
    public void initialize() {
        colDia.setCellValueFactory(new PropertyValueFactory<>("diaSemana"));
        colInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("horaFin"));

        colPrograma.setCellValueFactory(cellData -> {
            Horario h = cellData.getValue();
            if (h.getPractica() != null && h.getPractica().getPrograma() != null) {
                return new javafx.beans.property.SimpleStringProperty(h.getPractica().getPrograma().getNombre());
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        colServicio.setCellValueFactory(cellData -> {
            Horario h = cellData.getValue();
            if (h.getPractica() != null && h.getPractica().getServicioHospitalario() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        h.getPractica().getServicioHospitalario().getNombre());
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        cbDiaSemana.setItems(FXCollections.observableArrayList(
                "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO"));

        try {
            cbDocente.setItems(FXCollections.observableArrayList(docenteDAO.listarDocentes()));
            cbDocente.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(Docente d) {
                    return d != null ? d.getNombre() + " " + d.getApellido() : "";
                }

                @Override
                public Docente fromString(String s) {
                    return null;
                }
            });

            cbPrograma.setItems(FXCollections.observableArrayList(programaDAO.listar()));
            cbPrograma.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(Programa p) {
                    return p != null ? p.getNombre() : "";
                }

                @Override
                public Programa fromString(String s) {
                    return null;
                }
            });

            cbServicio.setItems(FXCollections.observableArrayList(servicioDAO.listar()));
            cbServicio.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(ServicioHospitalario s) {
                    return s != null ? s.getNombre() : "";
                }

                @Override
                public ServicioHospitalario fromString(String s) {
                    return null;
                }
            });

            actualizarTabla();
        } catch (Exception e) {
            mostrarMensaje("⚠ Error de conexión con la base de datos.", false);
            e.printStackTrace();
        }
    }

    @FXML
    public void onGuardarPlan() {
        if (cbDocente.getValue() == null || cbServicio.getValue() == null || cbPrograma.getValue() == null ||
                txtMes.getText() == null || txtMes.getText().trim().isEmpty() ||
                txtAnio.getText() == null || txtAnio.getText().trim().isEmpty() ||
                cbDiaSemana.getValue() == null ||
                txtHoraInicio.getText() == null || txtHoraInicio.getText().trim().isEmpty() ||
                txtHoraFin.getText() == null || txtHoraFin.getText().trim().isEmpty()) {
            mostrarMensaje("⚠ Todos los campos son obligatorios.", false);
            return;
        }

        try {
            int mes = Integer.parseInt(txtMes.getText().trim());
            int anio = Integer.parseInt(txtAnio.getText().trim());

            Practica practica = new Practica(mes, anio, cbDocente.getValue(), cbServicio.getValue(),
                    cbPrograma.getValue());
            planService.guardarPractica(practica);

            Horario horario = new Horario(
                    cbDiaSemana.getValue(),
                    LocalTime.parse(txtHoraInicio.getText().trim()),
                    LocalTime.parse(txtHoraFin.getText().trim()),
                    practica);
            planService.agregarHorario(horario);

            // Notificar a todos los estudiantes de ese programa
            List<Estudiante> estudiantes = estudianteDAO.listarEstudiantes().stream()
                    .filter(e -> e.getPrograma() != null
                            && e.getPrograma().getIdPrograma().equals(cbPrograma.getValue().getIdPrograma()))
                    .collect(java.util.stream.Collectors.toList());

            for (Estudiante e : estudiantes) {
                notificacionService.enviarNotificacion("Nueva Práctica de Área",
                        "Se ha creado una práctica para el programa " + cbPrograma.getValue().getNombre() +
                                " en " + cbServicio.getValue().getNombre() +
                                " los días " + cbDiaSemana.getValue() + " de " + txtHoraInicio.getText() + " a "
                                + txtHoraFin.getText(),
                        e.getDocumento());
            }

            mostrarMensaje("✔ Plan y horario guardados correctamente.", true);
            actualizarTabla();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            mostrarMensaje("⚠ Mes y año deben ser números.", false);
        } catch (IllegalStateException ex) {
            mostrarMensaje("⚠ " + ex.getMessage(), false);
        } catch (Exception ex) {
            mostrarMensaje("⚠ Revisa los datos. Hora en formato HH:mm", false);
        }
    }

    private void limpiarCampos() {
        txtMes.clear();
        txtAnio.clear();
        txtHoraInicio.clear();
        txtHoraFin.clear();
        cbDiaSemana.setValue(null);
        cbDocente.setValue(null);
        cbPrograma.setValue(null);
        cbServicio.setValue(null);
    }

    private void actualizarTabla() {
        try {
            tablaHorarios.setItems(FXCollections.observableArrayList(planService.listarHorarios()));
        } catch (Exception e) {
            System.err.println("No se pudo actualizar la tabla: " + e.getMessage());
        }
    }

    private void mostrarMensaje(String msg, boolean exito) {
        lblMensaje.setText(msg);
        lblMensaje.setStyle(exito ? "-fx-text-fill: #166534;" : "-fx-text-fill: #991b1b;");
    }
}
