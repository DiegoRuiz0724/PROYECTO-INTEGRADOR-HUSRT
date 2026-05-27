package com.diegoruiz.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "docente")
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocente;
    private String nombre;
    private String apellido;
    private String documento;
    private String especialidad;

    @ManyToOne
    @JoinColumn(name = "id_programa")
    private Programa programa;

    public Docente() {
    }

    public Docente(String nombre, String apellido, String documento, String especialidad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.especialidad = especialidad;

    }

    public Long getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Long idDocente) {
        this.idDocente = idDocente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }
}
