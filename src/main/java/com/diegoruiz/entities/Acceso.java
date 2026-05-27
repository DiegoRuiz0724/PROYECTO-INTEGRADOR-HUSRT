package com.diegoruiz.entities;



import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.diegoruiz.enums.EstadoAcceso;

@Entity
@Table(name = "acceso")
public class Acceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAcceso;

    private LocalDateTime horaIngreso;
    private LocalDateTime horaSalida;

    @Enumerated(EnumType.STRING)
    private EstadoAcceso estadoAcceso;

    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;

    public Acceso() {
    }

    public Acceso(LocalDateTime horaIngreso,
                  LocalDateTime horaSalida,
                  EstadoAcceso estadoAcceso,
                  Estudiante estudiante) {

        this.horaIngreso = horaIngreso;
        this.horaSalida = horaSalida;
        this.estadoAcceso = estadoAcceso;
        this.estudiante = estudiante;
    }

    public Long getIdAcceso() {
        return idAcceso;
    }

    public void setIdAcceso(Long idAcceso) {
        this.idAcceso = idAcceso;
    }

    public LocalDateTime getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(LocalDateTime horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public EstadoAcceso getEstadoAcceso() {
        return estadoAcceso;
    }

    public void setEstadoAcceso(EstadoAcceso estadoAcceso) {
        this.estadoAcceso = estadoAcceso;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}