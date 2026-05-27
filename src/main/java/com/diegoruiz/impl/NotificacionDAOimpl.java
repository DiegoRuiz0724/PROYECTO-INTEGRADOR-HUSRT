package com.diegoruiz.impl;

import com.diegoruiz.entities.Notificacion;
import com.diegoruiz.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class NotificacionDAOimpl {

    public void guardar(Notificacion n) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(n);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Notificacion> listarPorDestinatario(String documento) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return java.util.Collections.emptyList();
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notificacion n WHERE n.documentoDestinatario = :doc ORDER BY n.fecha DESC", Notificacion.class)
                    .setParameter("doc", documento)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void marcarComoLeida(Long id) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Notificacion n = em.find(Notificacion.class, id);
            if (n != null) n.setLeida(true);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
