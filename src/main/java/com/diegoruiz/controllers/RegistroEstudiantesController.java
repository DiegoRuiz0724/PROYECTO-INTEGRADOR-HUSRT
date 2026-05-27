package com.diegoruiz.controllers;

import com.diegoruiz.entities.Estudiante;
import com.diegoruiz.entities.Programa;
import com.diegoruiz.entities.Universidad;
import com.diegoruiz.entities.Usuario;
import com.diegoruiz.enums.Rol;
import com.diegoruiz.enums.TipoDocumento;
import com.diegoruiz.enums.TipoEstudiante;
import com.diegoruiz.impl.ProgramaDAOimpl;
import com.diegoruiz.impl.UniversidadDAOimpl;
import com.diegoruiz.impl.UsuarioDAOimpl;
import com.diegoruiz.service.RegistroService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class RegistroEstudiantesController {

    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtDocumento;
    @FXML
    private TextField txtSemestre;
    @FXML
    private DatePicker dpFechaInduccion;
    @FXML
    private DatePicker dpArlVencimiento;
    @FXML
    private ComboBox<Programa> cbPrograma;
    @FXML
    private ComboBox<Universidad> cbUniversidad;
    @FXML
    private ComboBox<TipoDocumento> cbTipoDocumento;
    @FXML
    private ComboBox<TipoEstudiante> cbTipoEstudiante;
    @FXML
    private TextField txtEmail;

    @FXML
    private TableView<Estudiante> tablaEstudiantes;
    @FXML
    private TableColumn<Estudiante, String> colNombres;
    @FXML
    private TableColumn<Estudiante, String> colApellidos;
    @FXML
    private TableColumn<Estudiante, String> colDocumento;
    @FXML
    private TableColumn<Estudiante, Integer> colSemestre;
    @FXML
    private TableColumn<Estudiante, LocalDate> colArl;

    @FXML
    private Label lblMensaje;
    @FXML
    private Label lblTotalEstudiantes;

    private final RegistroService registroService = new RegistroService();
    private final ProgramaDAOimpl programaDAO = new ProgramaDAOimpl();
    private final UniversidadDAOimpl universidadDAO = new UniversidadDAOimpl();
    private final UsuarioDAOimpl usuarioDAO = new UsuarioDAOimpl();

    @FXML
    public void initialize() {
        // Limpiar configuración previa si existiera
        tablaEstudiantes.getColumns().clear();

        // Re-crear columnas programáticamente para asegurar total compatibilidad
        TableColumn<Estudiante, String> colNombre = new TableColumn<>("NOMBRES");
        colNombre.setPrefWidth(150);
        colNombre.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombres()));

        TableColumn<Estudiante, String> colApellido = new TableColumn<>("APELLIDOS");
        colApellido.setPrefWidth(150);
        colApellido
                .setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getApellidos()));

        TableColumn<Estudiante, String> colCedula = new TableColumn<>("CÉDULA");
        colCedula.setPrefWidth(120);
        colCedula.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDocumento()));

        TableColumn<Estudiante, Integer> colSem = new TableColumn<>("SEM.");
        colSem.setPrefWidth(60);
        colSem.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getSemestre()));

        TableColumn<Estudiante, String> colArlVenc = new TableColumn<>("ARL");
        colArlVenc.setPrefWidth(120);
        colArlVenc.setCellValueFactory(d -> {
            LocalDate date = d.getValue().getArlVencimiento();
            return new javafx.beans.property.SimpleStringProperty(
                    date != null ? date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            : "No definida");
        });

        tablaEstudiantes.getColumns().addAll(colNombre, colApellido, colCedula, colSem, colArlVenc);

        try {
            cargarProgramas();
            cargarUniversidades();
            cargarTiposDocumento();
            cargarTiposEstudiante();
            actualizarTabla();
        } catch (Exception e) {
            System.err.println("Error en initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarProgramas() {
        try {
            List<Programa> programas = programaDAO.listar();
            if (programas.isEmpty()) {
                mostrarMensaje("⚠ No hay programas cargados. Verifica la base de datos.", false);
            }
            cbPrograma.setItems(FXCollections.observableArrayList(programas));
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
        } catch (Exception e) {
            System.err.println("No se pudieron cargar los programas: " + e.getMessage());
        }
    }

    private void cargarUniversidades() {
        try {
            List<Universidad> unis = universidadDAO.listar();
            cbUniversidad.setItems(FXCollections.observableArrayList(unis));
            cbUniversidad.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(Universidad u) {
                    return u != null ? u.getNombre() : "";
                }

                @Override
                public Universidad fromString(String s) {
                    return null;
                }
            });
        } catch (Exception e) {
            System.err.println("No se pudieron cargar las universidades: " + e.getMessage());
        }
    }

    private void cargarTiposDocumento() {
        cbTipoDocumento.setItems(FXCollections.observableArrayList(TipoDocumento.values()));
    }

    private void cargarTiposEstudiante() {
        cbTipoEstudiante.setItems(FXCollections.observableArrayList(TipoEstudiante.values()));
    }

    @FXML
    public void onGuardar() {
        if (txtNombres.getText() == null || txtNombres.getText().trim().isEmpty() ||
                txtDocumento.getText() == null || txtDocumento.getText().trim().isEmpty()) {
            mostrarMensaje("⚠ Nombres y documento son obligatorios.", false);
            return;
        }
        try {
            Estudiante e = new Estudiante();
            e.setNombres(txtNombres.getText().trim());
            e.setApellidos(txtApellidos.getText().trim());
            e.setDocumento(txtDocumento.getText().trim());
            e.setSemestre(Integer.parseInt(txtSemestre.getText().trim()));
            e.setEmail(txtEmail.getText() != null ? txtEmail.getText().trim() : "");
            e.setFechaInduccion(dpFechaInduccion.getValue());
            e.setArlVencimiento(dpArlVencimiento.getValue());
            e.setPrograma(cbPrograma.getValue());
            e.setUniversidad(cbUniversidad.getValue());
            e.setTipoEstudiante(cbTipoEstudiante.getValue());

            registroService.registrarEstudiante(e);

            // CREAR AUTOMÁTICAMENTE EL USUARIO PARA EL LOGUEO
            // Usuario: Número de documento | Password: 1234 (por defecto)
            if (usuarioDAO.buscarPorUsername(e.getDocumento()) == null) {
                Usuario nuevoUser = new Usuario(e.getDocumento(), "1234", Rol.ESTUDIANTE, e.getDocumento());
                usuarioDAO.guardar(nuevoUser);
                System.out.println("DEBUG: Usuario creado automáticamente para " + e.getNombres());
            }

            // Verificación post-guardado
            actualizarTabla();

            mostrarMensaje("✔ Estudiante registrado correctamente.", true);
            limpiarFormulario();
        } catch (NumberFormatException ex) {
            mostrarMensaje("⚠ El semestre debe ser un número.", false);
        } catch (IllegalArgumentException ex) {
            mostrarMensaje("⚠ " + ex.getMessage(), false);
        } catch (Exception ex) {
            mostrarMensaje("⚠ Error al guardar: " + ex.getMessage(), false);
        }
    }

    @FXML
    public void onEliminar() {
        Estudiante seleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje("⚠ Seleccione un estudiante de la tabla para eliminar.", false);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro de que desea eliminar al estudiante " +
                seleccionado.getNombres() + " " + seleccionado.getApellidos() + "?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                registroService.eliminarEstudiante(seleccionado.getIdEstudiante());
                mostrarMensaje("✔ Estudiante eliminado correctamente.", true);
                actualizarTabla();
            } catch (Exception e) {
                mostrarMensaje("⚠ Error al eliminar: " + e.getMessage(), false);
            }
        }
    }

    @FXML
    public void onLimpiar() {
        limpiarFormulario();
        lblMensaje.setText("");
    }

    private void actualizarTabla() {
        try {
            List<Estudiante> estudiantes = registroService.listarEstudiantes();
            System.out.println("DEBUG: Cargando " + estudiantes.size() + " estudiantes en la tabla.");

            tablaEstudiantes.setItems(FXCollections.observableArrayList(estudiantes));

            if (lblTotalEstudiantes != null) {
                lblTotalEstudiantes.setText("Total de estudiantes registrados: " + estudiantes.size());
            }

            tablaEstudiantes.refresh();
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo actualizar la tabla - " + e.getMessage());
            if (lblTotalEstudiantes != null) {
                lblTotalEstudiantes.setText("Error al cargar datos.");
            }
        }
    }

    private void limpiarFormulario() {
        txtNombres.clear();
        txtApellidos.clear();
        txtDocumento.clear();
        txtSemestre.clear();
        txtEmail.clear();
        dpFechaInduccion.setValue(null);
        dpArlVencimiento.setValue(null);
        cbPrograma.setValue(null);
        cbUniversidad.setValue(null);
        cbTipoDocumento.setValue(null);
        cbTipoEstudiante.setValue(null);
    }

    private void mostrarMensaje(String msg, boolean exito) {
        lblMensaje.setText(msg);
        lblMensaje.setStyle(exito ? "-fx-text-fill: #166534;" : "-fx-text-fill: #991b1b;");
    }
}
