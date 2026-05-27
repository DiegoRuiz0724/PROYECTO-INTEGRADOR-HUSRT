package com.diegoruiz.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {
    private static EntityManagerFactory emf;

    static {
        try {
            // Intentar inicializar de forma asíncrona para no bloquear el hilo de la UI
            // o el inicio de la app si la DB tarda en responder
            emf = Persistence.createEntityManagerFactory("proyecto_hurst");
        } catch (Exception ex) {
            System.err.println("ALERTA: No se pudo conectar a la base de datos MySQL.");
            System.err
                    .println("Asegúrese de que el servicio MySQL esté activo y el usuario 'user_java' tenga permisos.");
            System.err.println("Error: " + ex.getMessage());
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("proyecto_hurst");
                System.out.println("DEBUG: Conexión exitosa a la base de datos.");
            } catch (Exception ex) {
                System.err.println("ALERTA: No se pudo conectar a la base de datos MySQL.");
                System.err.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return emf;
    }

    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
