package com.diegoruiz.dto;

/**
 * DTO que encapsula el resultado completo de la validación de acceso.
 * Lo devuelve PorteriaService y lo consume PorteriaController
 * para renderizar el semáforo visual (RF-04).
 */
public class ValidacionAccesoDTO {

    private boolean aprobado;
    private String motivoRechazo;   // null si aprobado

    // Datos para mostrar en pantalla cuando es APROBADO
    private String nombreEstudiante;
    private String documento;
    private String programa;
    private String semestre;
    private String servicio;
    private String franjaHoraria;   // e.g. "14:00 – 17:00"
    private String nombreDocente;
    private boolean docentePresente;

    // ─── Factory methods ─────────────────────────────────────────────────────

    public static ValidacionAccesoDTO aprobado(String nombre, String documento,
            String programa, String semestre, String servicio,
            String franja, String docente, boolean docentePresente) {
        ValidacionAccesoDTO dto = new ValidacionAccesoDTO();
        dto.aprobado = true;
        dto.nombreEstudiante = nombre;
        dto.documento = documento;
        dto.programa = programa;
        dto.semestre = semestre;
        dto.servicio = servicio;
        dto.franjaHoraria = franja;
        dto.nombreDocente = docente;
        dto.docentePresente = docentePresente;
        return dto;
    }

    public static ValidacionAccesoDTO rechazado(String motivo) {
        ValidacionAccesoDTO dto = new ValidacionAccesoDTO();
        dto.aprobado = false;
        dto.motivoRechazo = motivo;
        return dto;
    }

    // ─── Getters y Setters ────────────────────────────────────────────────────

    public boolean isAprobado() { return aprobado; }
    public void setAprobado(boolean aprobado) { this.aprobado = aprobado; }

    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }

    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public String getServicio() { return servicio; }
    public void setServicio(String servicio) { this.servicio = servicio; }

    public String getFranjaHoraria() { return franjaHoraria; }
    public void setFranjaHoraria(String franjaHoraria) { this.franjaHoraria = franjaHoraria; }

    public String getNombreDocente() { return nombreDocente; }
    public void setNombreDocente(String nombreDocente) { this.nombreDocente = nombreDocente; }

    public boolean isDocentePresente() { return docentePresente; }
    public void setDocentePresente(boolean docentePresente) { this.docentePresente = docentePresente; }
}
