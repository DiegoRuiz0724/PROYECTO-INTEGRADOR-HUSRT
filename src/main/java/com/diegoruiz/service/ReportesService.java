package com.diegoruiz.service;

import com.diegoruiz.entities.Acceso;
import com.diegoruiz.entities.Estudiante;
import com.diegoruiz.enums.EstadoAcceso;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RF-07 — Módulo de Reportes y Trazabilidad.
 * RF-08 — Alertas automáticas.
 *
 * Genera los reportes principales del sistema.
 */
public class ReportesService {

    private final RegistroService registroService = new RegistroService();
    private final PorteriaService porteriaService = new PorteriaService();

    // ─── Reporte 1: Historial de ingresos por estudiante ─────────────────────

    public List<Acceso> historialEstudiante(String documento, LocalDate desde, LocalDate hasta) {
        return porteriaService.obtenerHistorial().stream()
            .filter(a -> a.getEstudiante() != null
                && a.getEstudiante().getDocumento().equals(documento))
            .filter(a -> {
                LocalDate fecha = a.getHoraIngreso().toLocalDate();
                return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
            })
            .collect(Collectors.toList());
    }

    // ─── Reporte 2: Horas acumuladas vs requeridas ───────────────────────────

    public ResumenHoras resumenHorasEstudiante(String documento, double horasRequeridas) {
        double cumplidas = porteriaService.obtenerHistorial().stream()
            .filter(a -> a.getEstudiante() != null
                && a.getEstudiante().getDocumento().equals(documento))
            .filter(a -> a.getEstadoAcceso() == EstadoAcceso.APROBADO
                && a.getHoraIngreso() != null && a.getHoraSalida() != null)
            .mapToDouble(a -> Duration.between(a.getHoraIngreso(), a.getHoraSalida()).toMinutes() / 60.0)
            .sum();

        return new ResumenHoras(documento, cumplidas, horasRequeridas);
    }

    // ─── Reporte 3: ARL próxima a vencer (RF-08) ─────────────────────────────

    /** Estudiantes con ARL que vence en los próximos 15 días. */
    public List<Estudiante> alertasArlProximaVencer() {
        return registroService.estudiantesConArlProximaAVencer(15);
    }

    // ─── Reporte 4: Intentos de acceso rechazados ─────────────────────────────

    public List<Acceso> rechazosHoy() {
        return porteriaService.obtenerRechazosHoy();
    }

    // ─── Reporte 5: Ocupación histórica por servicio ─────────────────────────

    public Map<String, Long> ocupacionHistoricaPorServicio() {
        return porteriaService.obtenerHistorial().stream()
            .filter(a -> a.getEstadoAcceso() == EstadoAcceso.APROBADO)
            .filter(a -> a.getEstudiante() != null)
            .collect(Collectors.groupingBy(
                a -> a.getEstudiante().getPrograma() != null
                    ? a.getEstudiante().getPrograma().getNombre()
                    : "Sin servicio",
                Collectors.counting()
            ));
    }

    // ─── Estadísticas del dashboard ──────────────────────────────────────────

    public DashboardStats obtenerEstadisticasDashboard() {
        DashboardStats stats = new DashboardStats();
        stats.totalEstudiantesRegistrados = registroService.totalRegistrados();
        stats.estudiantesDentroAhora = porteriaService.totalEstudiantesDentro();
        stats.alertasArlHoy = alertasArlProximaVencer().size();
        stats.rechazosHoy = rechazosHoy().size();
        return stats;
    }

    // ─── Clases de resultado ─────────────────────────────────────────────────

    public static class ResumenHoras {
        public final String documento;
        public final double horasCumplidas;
        public final double horasRequeridas;
        public final double porcentajeCumplimiento;

        public ResumenHoras(String documento, double cumplidas, double requeridas) {
            this.documento = documento;
            this.horasCumplidas = Math.round(cumplidas * 100.0) / 100.0;
            this.horasRequeridas = requeridas;
            this.porcentajeCumplimiento = requeridas > 0
                ? Math.min(100.0, (cumplidas / requeridas) * 100.0) : 0;
        }
    }

    public static class DashboardStats {
        public int totalEstudiantesRegistrados;
        public int estudiantesDentroAhora;
        public int alertasArlHoy;
        public int rechazosHoy;
    }
}
