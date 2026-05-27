package com.diegoruiz.impl;

import java.util.List;

import com.diegoruiz.entities.ServicioHospitalario;
import com.diegoruiz.interfaces.IServicioHospitalarioDAO;

import com.diegoruiz.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class ServicioHospitalarioDAOimpl implements IServicioHospitalarioDAO{

    @Override
    public void guardar(ServicioHospitalario servicioHospitalario) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(servicioHospitalario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<ServicioHospitalario> listar() {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return java.util.Collections.emptyList();
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM ServicioHospitalario s", ServicioHospitalario.class).getResultList();
        } finally {
            em.close();
        }
    }
}
