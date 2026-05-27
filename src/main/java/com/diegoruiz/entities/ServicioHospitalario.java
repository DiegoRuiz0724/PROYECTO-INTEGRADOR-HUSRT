package com.diegoruiz.entities;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "servicio")
public class ServicioHospitalario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;
    private String nombre;
    private String areaMedica;
    private String capacidadMaxima;

    @OneToMany(mappedBy = "servicioHospitalario")
    private List<Horario> horarios;

    public ServicioHospitalario() {
    }

    public ServicioHospitalario(String nombre, String areaMedica, String capacidadMaxima) {
        this.nombre = nombre;
        this.areaMedica = areaMedica;
        this.capacidadMaxima = capacidadMaxima;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAreaMedica() {
        return areaMedica;
    }

    public void setAreaMedica(String areaMedica) {
        this.areaMedica = areaMedica;
    }

    public String getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(String capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }
}
