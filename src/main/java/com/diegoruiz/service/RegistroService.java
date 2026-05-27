package com.diegoruiz.service;

import com.diegoruiz.entities.Docente;
import com.diegoruiz.entities.Estudiante;
import com.diegoruiz.impl.DocenteDAOimpl;
import com.diegoruiz.impl.EstudianteDAOimpl;
import com.diegoruiz.interfaces.IDocenteDAO;
import com.diegoruiz.interfaces.IEstudianteDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RF-01 — Módulo de Registro de Estudiantes
 * RF-02 — Módulo de Registro de Docentes
 *
 * Capa de servicio que encapsula la lógica de negocio sobre los DAOs JPA.
 */
public class RegistroService {

    private final IEstudianteDAO estudianteDAO = new EstudianteDAOimpl();
    private final IDocenteDAO docenteDAO = new DocenteDAOimpl();

    // ─── Registro de Estudiantes (RF-01) ─────────────────────────────────────

    /**
     * Registra un nuevo estudiante.
     * Lanza excepción si ya existe uno con el mismo documento.
     */
    public void registrarEstudiante(Estudiante estudiante) {
        Optional<Estudiante> existente = buscarPorDocumento(estudiante.getDocumento());
        if (existente.isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un estudiante con documento " + estudiante.getDocumento() + ".");
        }
        estudianteDAO.guardarEstudiante(estudiante);
    }

    /**
     * Busca un estudiante por número de documento — O(1) vía JPA.
     */
    public Optional<Estudiante> buscarPorDocumento(String documento) {
        return Optional.ofNullable(estudianteDAO.buscarPorDocumento(documento));
    }

    /**
     * Lista todos los estudiantes registrados.
     */
    public List<Estudiante> listarEstudiantes() {
        return estudianteDAO.listarEstudiantes();
    }

    /**
     * Actualiza los datos de un estudiante (ARL, inducción, etc.).
     */
    public void actualizarEstudiante(Estudiante estudiante) {
        estudianteDAO.actualizarEstudiante(estudiante);
    }

    /**
     * Retorna estudiantes con ARL que vence en los próximos {@code dias} días
     * (RF-08).
     */
    public List<Estudiante> estudiantesConArlProximaAVencer(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);
        return estudianteDAO.listarEstudiantes().stream()
                .filter(e -> e.getArlVencimiento() != null)
                .filter(e -> !e.getArlVencimiento().isBefore(hoy) && e.getArlVencimiento().isBefore(limite))
                .collect(Collectors.toList());
    }

    /** Total de estudiantes registrados. */
    public int totalRegistrados() {
        return estudianteDAO.listarEstudiantes().size();
    }

    /**
     * Elimina un estudiante por su ID.
     */
    public void eliminarEstudiante(Long id) {
        estudianteDAO.eliminarEstudiante(id);
    }

    // ─── Registro de Docentes (RF-02) ─────────────────────────────────────────

    public void registrarDocente(Docente docente) {
        boolean existe = docenteDAO.listarDocentes().stream()
                .anyMatch(d -> d.getDocumento().equals(docente.getDocumento()));
        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe un docente con documento " + docente.getDocumento() + ".");
        }
        docenteDAO.guardar(docente);
    }

    public Optional<Docente> buscarDocentePorDocumento(String documento) {
        return docenteDAO.listarDocentes().stream()
                .filter(d -> d.getDocumento().equals(documento))
                .findFirst();
    }

    public List<Docente> listarDocentes() {
        return docenteDAO.listarDocentes();
    }
}
