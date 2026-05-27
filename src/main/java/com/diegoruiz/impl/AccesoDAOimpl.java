package com.diegoruiz.impl;

import java.util.List;

import com.diegoruiz.entities.Acceso;
import com.diegoruiz.interfaces.IAccesoDAO;

import com.diegoruiz.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class AccesoDAOimpl implements IAccesoDAO {

    @Override
    public void guardar(Acceso acceso) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (acceso.getIdAcceso() == null) {
                em.persist(acceso);
            } else {
                em.merge(acceso);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Acceso> listar() {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return java.util.Collections.emptyList();
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Acceso a", Acceso.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Acceso buscarUltimoAccesoAbierto(Long idEstudiante) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return null;
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT a FROM Acceso a " +
                "WHERE a.estudiante.idEstudiante = :id " +
                "AND a.horaSalida IS NULL " +
                "ORDER BY a.horaIngreso DESC", Acceso.class)
                .setParameter("id", idEstudiante)
                .getResultStream()
                .findFirst()
                .orElse(null);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Acceso> listarRechazosPorFecha(java.time.LocalDate fecha) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return java.util.Collections.emptyList();
        EntityManager em = emf.createEntityManager();
        try {
            java.time.LocalDateTime inicio = fecha.atStartOfDay();
            java.time.LocalDateTime fin = fecha.atTime(23, 59, 59);
            return em.createQuery(
                "SELECT a FROM Acceso a WHERE a.estadoAcceso = :estado " +
                "AND a.horaIngreso BETWEEN :inicio AND :fin", Acceso.class)
                .setParameter("estado", com.diegoruiz.enums.EstadoAcceso.RECHAZADO)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .getResultList();
        } finally {
            em.close();
        }
    }
}
