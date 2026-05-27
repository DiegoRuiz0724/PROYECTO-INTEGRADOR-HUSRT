package com.diegoruiz.impl;

import com.diegoruiz.entities.Usuario;
import com.diegoruiz.interfaces.IUsuarioDAO;
import com.diegoruiz.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class UsuarioDAOimpl implements IUsuarioDAO {

    @Override
    public void guardar(Usuario usuario) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return null;
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.username = :user", Usuario.class)
                    .setParameter("user", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> listar() {
        EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
        if (emf == null) return java.util.Collections.emptyList();
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } finally {
            em.close();
        }
    }
}
