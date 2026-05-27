package com.diegoruiz.service;

import com.diegoruiz.entities.Notificacion;
import com.diegoruiz.impl.NotificacionDAOimpl;
import java.util.List;

public class NotificacionService {
    private final NotificacionDAOimpl dao = new NotificacionDAOimpl();

    public void enviarNotificacion(String titulo, String mensaje, String documento) {
        Notificacion n = new Notificacion(titulo, mensaje, documento);
        dao.guardar(n);
    }

    public List<Notificacion> obtenerMisNotificaciones(String documento) {
        return dao.listarPorDestinatario(documento);
    }

    public void marcarComoLeida(Long id) {
        dao.marcarComoLeida(id);
    }

    public long contarNoLeidas(String documento) {
        return dao.listarPorDestinatario(documento).stream().filter(n -> !n.isLeida()).count();
    }
}
