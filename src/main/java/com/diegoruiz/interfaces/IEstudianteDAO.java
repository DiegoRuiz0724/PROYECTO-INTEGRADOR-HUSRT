package com.diegoruiz.interfaces;

import java.util.List;
import com.diegoruiz.entities.Estudiante;

public interface IEstudianteDAO {
    void guardarEstudiante(Estudiante estudiante);

    void actualizarEstudiante(Estudiante estudiante);

    void eliminarEstudiante(Long id);

    Estudiante BuscarPorId(Long id);

    List<Estudiante> listarEstudiantes();

    Estudiante buscarPorDocumento(String documento);
}
