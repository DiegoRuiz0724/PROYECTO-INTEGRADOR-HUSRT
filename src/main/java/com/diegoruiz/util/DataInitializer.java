package com.diegoruiz.util;

import com.diegoruiz.entities.*;
import com.diegoruiz.impl.*;
import com.diegoruiz.enums.Rol;
import java.util.List;

/**
 * Utilidad para insertar datos iniciales (maestros) en la base de datos
 * si estas se encuentran vacías. Esto asegura que los ComboBox tengan opciones.
 */
public class DataInitializer {

    public static void seed() {
        System.out.println("DEBUG: Iniciando DataInitializer.seed()...");
        if (HibernateUtil.getEntityManagerFactory() == null) {
            System.err.println("Omitiendo inicialización de datos: No hay conexión a la DB.");
            return;
        }
        try {
            seedUsuarios();
            seedUniversidadesYProgramas();
            seedServicios();
            seedDocentes();
            System.out.println("DEBUG: Finalización exitosa de DataInitializer.seed().");
        } catch (Exception e) {
            System.err.println("DEBUG: Error durante la inicialización de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void seedUsuarios() {
        UsuarioDAOimpl userDAO = new UsuarioDAOimpl();
        if (userDAO.listar().isEmpty()) {
            userDAO.guardar(new Usuario("admin", "1234", Rol.ADMIN));
            userDAO.guardar(new Usuario("portero", "1234", Rol.PORTERO));
            userDAO.guardar(new Usuario("estudiante", "1234", Rol.ESTUDIANTE, "1010")); // Usuario con documento
            userDAO.guardar(new Usuario("docente", "1234", Rol.DOCENTE));
        }
    }

    private static void seedUniversidadesYProgramas() {
        UniversidadDAOimpl uniDAO = new UniversidadDAOimpl();
        ProgramaDAOimpl progDAO = new ProgramaDAOimpl();
        EstudianteDAOimpl estDAO = new EstudianteDAOimpl();

        if (uniDAO.listar().isEmpty()) {
            Universidad u1 = new Universidad("Universidad Nacional");
            Universidad u2 = new Universidad("Universidad de Boyacá");
            Universidad u3 = new Universidad("UPTC");

            uniDAO.guardar(u1);
            uniDAO.guardar(u2);
            uniDAO.guardar(u3);

            Programa p1 = new Programa("Medicina", u1);
            progDAO.guardar(p1);
            progDAO.guardar(new Programa("Enfermería", u1));
            progDAO.guardar(new Programa("Fisioterapia", u2));
            progDAO.guardar(new Programa("Instrumentación Quirúrgica", u3));

            // Crear estudiante de prueba vinculado al usuario "estudiante"
            estDAO.guardarEstudiante(new Estudiante("Juan", "Prueba", "1010", 7, "juan@test.com",
                    java.time.LocalDate.now(), java.time.LocalDate.now().plusMonths(6), p1, u1));
        }
    }

    private static void seedServicios() {
        ServicioHospitalarioDAOimpl servicioDAO = new ServicioHospitalarioDAOimpl();
        if (servicioDAO.listar().isEmpty()) {
            servicioDAO.guardar(new ServicioHospitalario("Urgencias", "URGENCIAS", "10"));
            servicioDAO.guardar(new ServicioHospitalario("UCI Adultos", "UCI", "5"));
            servicioDAO.guardar(new ServicioHospitalario("Pediatría", "PEDIATRIA", "8"));
            servicioDAO.guardar(new ServicioHospitalario("Cirugía", "CIRUGIA", "6"));
            servicioDAO.guardar(new ServicioHospitalario("Hospitalización", "HOSPITALIZACION", "15"));
        }
    }

    private static void seedDocentes() {
        DocenteDAOimpl docenteDAO = new DocenteDAOimpl();
        if (docenteDAO.listarDocentes().isEmpty()) {
            docenteDAO.guardar(new Docente("Juan", "Perez", "12345678", "Medicina Interna"));
            docenteDAO.guardar(new Docente("Maria", "Lopez", "87654321", "Pediatría"));
            docenteDAO.guardar(new Docente("Carlos", "Gomez", "11223344", "Cirugía General"));
        }
    }
}
