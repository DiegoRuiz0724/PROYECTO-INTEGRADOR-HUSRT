package com.diegoruiz.impl;

import java.util.List;

import com.diegoruiz.entities.Horario;
import com.diegoruiz.interfaces.IHorarioDAO;

import com.diegoruiz.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class HorarioDAOimpl implements IHorarioDAO{
 
    @Override
    public void guardar(Horario horario) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(horario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
 
    @Override
    public List<Horario> listar() {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return java.util.Collections.emptyList();
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT h FROM Horario h", Horario.class).getResultList();
        } finally {
            em.close();
        }
    }
}
