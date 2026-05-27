package com.diegoruiz.entities;

import jakarta.persistence.*;
import com.diegoruiz.enums.EstadoDocumento;
import com.diegoruiz.enums.TipoDocumento;
import java.time.LocalDate;

@Entity
@Table(name = "documento")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocumento;

    private String nombreDocumento;

    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    private EstadoDocumento estadoDocumento;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;

    public Documento() {
    }

    public Documento(String nombreDocumento,
            LocalDate fechaVencimiento,
            EstadoDocumento estadoDocumento,
            TipoDocumento tipoDocumento,
            Estudiante estudiante) {

        this.nombreDocumento = nombreDocumento;
        this.fechaVencimiento = fechaVencimiento;
        this.estadoDocumento = estadoDocumento;
        this.tipoDocumento = tipoDocumento;
        this.estudiante = estudiante;
    }

    public Long getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Long idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoDocumento getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(EstadoDocumento estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}
