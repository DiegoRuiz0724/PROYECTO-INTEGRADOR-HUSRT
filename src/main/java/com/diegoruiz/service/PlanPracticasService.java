package com.diegoruiz.service;

import com.diegoruiz.entities.Horario;
import com.diegoruiz.entities.Practica;
import com.diegoruiz.impl.HorarioDAOimpl;
import com.diegoruiz.impl.PracticaDAOimpl;
import com.diegoruiz.interfaces.IHorarioDAO;
import com.diegoruiz.interfaces.IPracticaDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RF-03 — Módulo de Plan de Prácticas (Cronograma mensual).
 *
 * Gestiona las Practicas (cabecera mensual) y sus Horarios (franjas),
 * con validación de capacidad instalada por servicio.
 */
public class PlanPracticasService {

    private final IPracticaDAO practicaDAO = new PracticaDAOimpl();
    private final IHorarioDAO horarioDAO = new HorarioDAOimpl();

    // ─── RF-03: Guardar plan mensual ─────────────────────────────────────────

    public void guardarPractica(Practica practica) {
        practicaDAO.guardar(practica);
    }

    /**
     * Agrega un horario (franja) al plan.
     * Valida que no se supere la capacidad máxima del servicio.
     */
    public void agregarHorario(Horario horario) {
        if (superaCapacidad(horario)) {
            String cap = horario.getPractica() != null
                    && horario.getPractica().getServicioHospitalario() != null
                            ? horario.getPractica().getServicioHospitalario().getCapacidadMaxima()
                            : "máxima";
            throw new IllegalStateException(
                    "El servicio alcanzó su capacidad (" + cap + " estudiantes) "
                            + "en la franja " + horario.getHoraInicio() + " – " + horario.getHoraFin()
                            + " del " + horario.getDiaSemana() + ".");
        }
        horarioDAO.guardar(horario);
    }

    /**
     * Retorna el Horario vigente para el estudiante en el momento actual,
     * buscando en las prácticas cuyo servicio coincida y la franja esté activa.
     * Usado por PorteriaService para validar el acceso (RF-04, check #4).
     */
    public Optional<Horario> buscarHorarioVigenteParaEstudiante(Long idEstudiante) {
        LocalTime ahora = LocalTime.now();
        String diaHoy = LocalDate.now()
                .getDayOfWeek()
                .getDisplayName(java.time.format.TextStyle.FULL, new Locale("es", "CO"));

        String diaHoyNormalizado = normalizar(diaHoy);

        // Obtener el programa del estudiante
        com.diegoruiz.impl.EstudianteDAOimpl estDAO = new com.diegoruiz.impl.EstudianteDAOimpl();
        com.diegoruiz.entities.Estudiante estudiante = estDAO.BuscarPorId(idEstudiante);
        if (estudiante == null || estudiante.getPrograma() == null)
            return Optional.empty();

        Long idProgramaEstudiante = estudiante.getPrograma().getIdPrograma();

        return horarioDAO.listar().stream()
                .filter(h -> h.getPractica() != null && h.getPractica().getPrograma() != null &&
                        h.getPractica().getPrograma().getIdPrograma().equals(idProgramaEstudiante))
                .filter(h -> h.getDiaSemana() != null
                        && normalizar(h.getDiaSemana()).equals(diaHoyNormalizado))
                .filter(h -> !ahora.isBefore(h.getHoraInicio()) && !ahora.isAfter(h.getHoraFin()))
                .findFirst();
    }

    private String normalizar(String texto) {
        if (texto == null)
            return "";
        return java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase()
                .trim();
    }

    public List<Practica> listarPracticasPorMes(int mes, int anio) {
        return practicaDAO.listar().stream()
                .filter(p -> p.getMes() == mes && p.getAnio() == anio)
                .collect(Collectors.toList());
    }

    public List<Horario> listarHorarios() {
        return horarioDAO.listar();
    }

    // ─── Helper: control de capacidad ────────────────────────────────────────

    private boolean superaCapacidad(Horario nuevo) {
        if (nuevo.getPractica() == null
                || nuevo.getPractica().getServicioHospitalario() == null)
            return false;

        Long idServicio = nuevo.getPractica().getServicioHospitalario().getIdServicio();
        String capacidadStr = nuevo.getPractica().getServicioHospitalario().getCapacidadMaxima();
        int capacidad;
        try {
            capacidad = Integer.parseInt(capacidadStr);
        } catch (NumberFormatException e) {
            return false; // sin límite definido
        }

        long asignados = horarioDAO.listar().stream()
                .filter(h -> h.getPractica() != null
                        && h.getPractica().getServicioHospitalario() != null
                        && h.getPractica().getServicioHospitalario().getIdServicio().equals(idServicio))
                .filter(h -> h.getDiaSemana() != null
                        && h.getDiaSemana().equalsIgnoreCase(nuevo.getDiaSemana()))
                .filter(h -> franjasSeSolapan(h, nuevo))
                .count();

        return asignados >= capacidad;
    }

    private boolean franjasSeSolapan(Horario a, Horario b) {
        return !a.getHoraFin().isBefore(b.getHoraInicio())
                && !b.getHoraFin().isBefore(a.getHoraInicio());
    }
}
