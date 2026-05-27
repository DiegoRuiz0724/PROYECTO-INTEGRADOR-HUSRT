package com.diegoruiz.impl;

import java.util.List;

import com.diegoruiz.entities.Documento;
import com.diegoruiz.interfaces.IDocumentoDAO;

import com.diegoruiz.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class DocumentoDAOimpl implements IDocumentoDAO {

    private EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
 
    @Override
    public void guardar(Documento documento) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(documento);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
 
    @Override
    public List<Documento> listar() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT d FROM Documento d", Documento.class).getResultList();
        } finally {
            em.close();
        }
    }
    
}
