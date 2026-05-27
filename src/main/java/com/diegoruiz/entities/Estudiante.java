package com.diegoruiz.entities;

import com.diegoruiz.enums.TipoEstudiante;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "estudiante")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstudiante;

    private String nombres;
    private String apellidos;
    private String documento;
    private int semestre;
    private String email;

    @Enumerated(EnumType.STRING)
    private TipoEstudiante tipoEstudiante;

    private LocalDate fechaInduccion;
    private LocalDate arlVencimiento;

    @ManyToOne
    @JoinColumn(name = "id_programa")
    private Programa programa;

    @ManyToOne
    @JoinColumn(name = "id_universidad")
    private Universidad universidad;

    public Estudiante() {
    }

    public Estudiante(String nombres,
            String apellidos,
            String documento,
            int semestre,
            String email,
            LocalDate fechaInduccion,
            LocalDate arlVencimiento,
            Programa programa,
            Universidad universidad) {

        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documento = documento;
        this.semestre = semestre;
        this.email = email;
        this.fechaInduccion = fechaInduccion;
        this.arlVencimiento = arlVencimiento;
        this.programa = programa;
        this.universidad = universidad;
    }

    public Long getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Long idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoEstudiante getTipoEstudiante() {
        return tipoEstudiante;
    }

    public void setTipoEstudiante(TipoEstudiante tipoEstudiante) {
        this.tipoEstudiante = tipoEstudiante;
    }

    public LocalDate getFechaInduccion() {
        return fechaInduccion;
    }

    public void setFechaInduccion(LocalDate fechaInduccion) {
        this.fechaInduccion = fechaInduccion;
    }

    public LocalDate getArlVencimiento() {
        return arlVencimiento;
    }

    public void setArlVencimiento(LocalDate arlVencimiento) {
        this.arlVencimiento = arlVencimiento;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public Universidad getUniversidad() {
        return universidad;
    }

    public void setUniversidad(Universidad universidad) {
        this.universidad = universidad;
    }
}