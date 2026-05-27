package com.diegoruiz.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotificacion;

    private String titulo;
    private String mensaje;
    private LocalDateTime fecha;
    private boolean leida;
    private String documentoDestinatario; // Para filtrar por el estudiante/docente

    public Notificacion() {
    }

    public Notificacion(String titulo, String mensaje, String documentoDestinatario) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.documentoDestinatario = documentoDestinatario;
        this.fecha = LocalDateTime.now();
        this.leida = false;
    }

    // Getters y Setters
    public Long getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(Long id) { this.idNotificacion = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }
    public String getDocumentoDestinatario() { return documentoDestinatario; }
    public void setDocumentoDestinatario(String documento) { this.documentoDestinatario = documento; }
}
