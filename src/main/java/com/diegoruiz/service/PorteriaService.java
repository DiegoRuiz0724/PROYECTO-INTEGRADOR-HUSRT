package com.diegoruiz.service;

import com.diegoruiz.dto.ValidacionAccesoDTO;
import com.diegoruiz.entities.Acceso;
import com.diegoruiz.entities.Estudiante;
import com.diegoruiz.entities.Horario;
import com.diegoruiz.enums.EstadoAcceso;
import com.diegoruiz.impl.AccesoDAOimpl;
import com.diegoruiz.interfaces.IAccesoDAO;
import com.diegoruiz.util.ArbolBST;
import com.diegoruiz.util.ColaAcceso;
import com.diegoruiz.util.TablaHash;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * RF-04 — Módulo de Control de Acceso.
 * RF-05 — Registro de salida y cálculo de horas cumplidas.
 * RF-06 — Panel de presencia en tiempo real.
 *
 * Estructuras de datos personalizadas implementadas:
 * - ArbolBST<String, Estudiante> — búsqueda por documento O(log n)
 * - ColaAcceso<String> — solicitudes de ingreso FIFO
 * - TablaHash<String, PresenciaInfo> — panel en tiempo real O(1)
 */
public class PorteriaService {

    private final RegistroService registroService = new RegistroService();
    private final PlanPracticasService planService = new PlanPracticasService();
    private final IAccesoDAO accesoDAO = new AccesoDAOimpl();
    private final NotificacionService notificacionService = new NotificacionService();

    // ─── Estructuras de datos personalizadas ──────────────────────────────────

    private static final ArbolBST<String, Estudiante> arbolEstudiantes = new ArbolBST<>();
    private static final ColaAcceso<String> colaAcceso = new ColaAcceso<>();
    private static final TablaHash<String, PresenciaInfo> tablaPresencia = new TablaHash<>();

    static {
        // Cargar presencia actual desde la DB al iniciar el servicio
        new Thread(() -> {
            try {
                IAccesoDAO dao = new AccesoDAOimpl();
                dao.listar().stream()
                        .filter(a -> a.getHoraSalida() == null && a.getEstadoAcceso() == EstadoAcceso.APROBADO)
                        .forEach(a -> {
                            if (a.getEstudiante() != null) {
                                String doc = a.getEstudiante().getDocumento();
                                String nombre = a.getEstudiante().getNombres() + " " + a.getEstudiante().getApellidos();
                                tablaPresencia.insertar(doc,
                                        new PresenciaInfo(doc, nombre, "En servicio", a.getHoraIngreso()));
                            }
                        });
            } catch (Exception ignored) {
            }
        }).start();
    }

    public static class PresenciaInfo {
        private final String documento;
        private final String nombreEstudiante;
        private final String servicio;
        private final LocalDateTime horaEntrada;

        public PresenciaInfo(String documento, String nombre, String servicio, LocalDateTime entrada) {
            this.documento = documento;
            this.nombreEstudiante = nombre;
            this.servicio = servicio;
            this.horaEntrada = entrada;
        }

        public String getDocumento() {
            return documento;
        }

        public String getNombreEstudiante() {
            return nombreEstudiante;
        }

        public String getServicio() {
            return servicio;
        }

        public LocalDateTime getHoraEntrada() {
            return horaEntrada;
        }
    }

    // ─── RF-04: Validación de acceso ─────────────────────────────────────────

    public ValidacionAccesoDTO validarIngreso(String documento) {
        // Encolar y desencolar para cumplir con el requisito FIFO
        colaAcceso.encolar(documento);
        String docProcesar = colaAcceso.desencolar();

        // 1. ¿Está registrado? (Consultar caché BST o DB)
        Estudiante estudiante = arbolEstudiantes.buscar(docProcesar);
        if (estudiante == null) {
            Optional<Estudiante> optEstudiante = registroService.buscarPorDocumento(docProcesar);
            if (optEstudiante.isEmpty()) {
                return rechazar(docProcesar, "Estudiante no registrado en el sistema HUSRT.");
            }
            estudiante = optEstudiante.get();
            arbolEstudiantes.insertar(docProcesar, estudiante);
        }

        // 2. ¿Inducción?
        if (estudiante.getFechaInduccion() == null) {
            return rechazar(docProcesar, "Inducción hospitalaria no completada.");
        }

        // 3. ¿ARL?
        LocalDate hoy = LocalDate.now();
        if (estudiante.getArlVencimiento() == null || estudiante.getArlVencimiento().isBefore(hoy)) {
            return rechazar(docProcesar, "ARL vencida o no registrada.");
        }

        // 4. ¿Horario?
        Optional<Horario> optHorario = planService.buscarHorarioVigenteParaEstudiante(estudiante.getIdEstudiante());
        if (optHorario.isEmpty()) {
            return rechazar(docProcesar, "No tiene horario asignado para este momento.");
        }
        Horario horario = optHorario.get();

        // 5. ¿Docente? (Simulado)
        boolean docentePresente = true;

        // ✔ APROBADO
        LocalDateTime ahora = LocalDateTime.now();
        String nombreServicio = (horario.getPractica() != null
                && horario.getPractica().getServicioHospitalario() != null)
                        ? horario.getPractica().getServicioHospitalario().getNombre()
                        : "Servicio no asignado";

        accesoDAO.guardar(new Acceso(ahora, null, EstadoAcceso.APROBADO, estudiante));
        tablaPresencia.insertar(docProcesar, new PresenciaInfo(docProcesar,
                estudiante.getNombres() + " " + estudiante.getApellidos(), nombreServicio, ahora));

        // Enviar notificación de ingreso
        notificacionService.enviarNotificacion("Ingreso Registrado",
                "Has ingresado al servicio de " + nombreServicio + " a las "
                        + ahora.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
                docProcesar);

        return ValidacionAccesoDTO.aprobado(
                estudiante.getNombres() + " " + estudiante.getApellidos(),
                docProcesar,
                (estudiante.getPrograma() != null ? estudiante.getPrograma().getNombre() : "N/A"),
                "Semestre " + estudiante.getSemestre(),
                nombreServicio,
                horario.getHoraInicio() + " - " + horario.getHoraFin(),
                (horario.getPractica() != null && horario.getPractica().getDocente() != null
                        ? horario.getPractica().getDocente().getNombre()
                        : "N/A"),
                docentePresente);
    }

    // ─── RF-05: Salida ───────────────────────────────────────────────────────

    public double registrarSalida(String documento) {
        LocalDateTime ahora = LocalDateTime.now();
        PresenciaInfo presencia = tablaPresencia.eliminar(documento);

        Optional<Estudiante> optEstudiante = registroService.buscarPorDocumento(documento);
        if (optEstudiante.isPresent()) {
            Acceso ultimo = accesoDAO.buscarUltimoAccesoAbierto(optEstudiante.get().getIdEstudiante());
            if (ultimo != null) {
                ultimo.setHoraSalida(ahora);
                accesoDAO.guardar(ultimo);
                LocalDateTime inicio = (presencia != null) ? presencia.getHoraEntrada() : ultimo.getHoraIngreso();
                double horas = Duration.between(inicio, ahora).toMinutes() / 60.0;

                // Enviar notificación de salida
                notificacionService.enviarNotificacion("Salida Registrada",
                        "Has salido del hospital. Tiempo cumplido: " + String.format("%.1f", horas) + " horas.",
                        documento);

                return horas;
            }
        }
        return -1;
    }

    // ─── RF-06: Presencia ────────────────────────────────────────────────────

    public Collection<PresenciaInfo> obtenerPresenciaActual() {
        return tablaPresencia.valores();
    }

    public Optional<PresenciaInfo> consultarPresencia(String documento) {
        return Optional.ofNullable(tablaPresencia.obtener(documento));
    }

    public int totalEstudiantesDentro() {
        return tablaPresencia.size();
    }

    // ─── Otros ───────────────────────────────────────────────────────────────

    public List<Acceso> obtenerHistorial() {
        return accesoDAO.listar();
    }

    public List<Acceso> obtenerRechazosHoy() {
        return accesoDAO.listarRechazosPorFecha(LocalDate.now());
    }

    private ValidacionAccesoDTO rechazar(String documento, String motivo) {
        registroService.buscarPorDocumento(documento).ifPresent(e -> {
            accesoDAO.guardar(new Acceso(LocalDateTime.now(), null, EstadoAcceso.RECHAZADO, e));
        });
        return ValidacionAccesoDTO.rechazado(motivo);
    }
}
