package com.diegoruiz.impl;

import java.util.List;

import com.diegoruiz.entities.Practica;
import com.diegoruiz.interfaces.IPracticaDAO;

import com.diegoruiz.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class PracticaDAOimpl implements IPracticaDAO {

    @Override
    public void guardar(Practica practica) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(practica);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Practica> listar() {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return java.util.Collections.emptyList();
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Practica p", Practica.class).getResultList();
        } finally {
            em.close();
        }
    }
}
