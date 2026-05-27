package com.diegoruiz.impl;

import java.util.List;

import com.diegoruiz.entities.Universidad;
import com.diegoruiz.interfaces.IUniversidadDAO;

import com.diegoruiz.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class UniversidadDAOimpl implements IUniversidadDAO {

    @Override
    public void guardar(Universidad universidad) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null)
            return;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(universidad);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Universidad> listar() {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null)
            return java.util.Collections.emptyList();
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Universidad u", Universidad.class).getResultList();
        } finally {
            em.close();
        }
    }
}
