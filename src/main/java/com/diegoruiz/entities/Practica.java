package com.diegoruiz.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "practica")
public class Practica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPractica;

    private int mes;
    private int anio;

    @ManyToOne
    @JoinColumn(name = "id_docente")
    private Docente docente;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private ServicioHospitalario servicioHospitalario;

    @ManyToOne
    @JoinColumn(name = "id_programa")
    private Programa programa;

    public Practica() {
    }

    public Practica(int mes,
            int anio,
            Docente docente,
            ServicioHospitalario servicioHospitalario) {
        this.mes = mes;
        this.anio = anio;
        this.docente = docente;
        this.servicioHospitalario = servicioHospitalario;
    }

    public Practica(int mes,
            int anio,
            Docente docente,
            ServicioHospitalario servicioHospitalario,
            Programa programa) {
        this.mes = mes;
        this.anio = anio;
        this.docente = docente;
        this.servicioHospitalario = servicioHospitalario;
        this.programa = programa;
    }

    public Long getIdPractica() {
        return idPractica;
    }

    public void setIdPractica(Long idPractica) {
        this.idPractica = idPractica;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public ServicioHospitalario getServicioHospitalario() {
        return servicioHospitalario;
    }

    public void setServicioHospitalario(ServicioHospitalario servicioHospitalario) {
        this.servicioHospitalario = servicioHospitalario;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }
}