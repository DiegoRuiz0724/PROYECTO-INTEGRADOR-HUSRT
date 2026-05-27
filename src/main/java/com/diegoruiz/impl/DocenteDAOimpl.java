package com.diegoruiz.impl;

import java.util.List;

import com.diegoruiz.entities.Docente;
import com.diegoruiz.interfaces.IDocenteDAO;

import com.diegoruiz.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class DocenteDAOimpl implements IDocenteDAO {

    @Override
    public void guardar(Docente docente) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(docente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Docente> listarDocentes() {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return java.util.Collections.emptyList();
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT d FROM Docente d", Docente.class).getResultList();
        } finally {
            em.close();
        }
    }
}
