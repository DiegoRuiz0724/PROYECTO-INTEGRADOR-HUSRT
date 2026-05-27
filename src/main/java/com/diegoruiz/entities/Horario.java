package com.diegoruiz.entities;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "horario")
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHorario;

    private String diaSemana;

    private LocalTime horaInicio;
    private LocalTime horaFin;

    @ManyToOne
    @JoinColumn(name = "id_practica")
    private Practica practica;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private ServicioHospitalario servicioHospitalario;

    public Horario() {
    }

    public Horario(String diaSemana,
            LocalTime horaInicio,
            LocalTime horaFin,
            Practica practica) {

        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.practica = practica;
    }

    public Long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Practica getPractica() {
        return practica;
    }

    public void setPractica(Practica practica) {
        this.practica = practica;
    }

    public ServicioHospitalario getServicioHospitalario() {
        return servicioHospitalario;
    }

    public void setServicioHospitalario(ServicioHospitalario servicioHospitalario) {
        this.servicioHospitalario = servicioHospitalario;
    }
}