package com.diegoruiz.interfaces;

import java.util.List;

import com.diegoruiz.entities.Acceso;

public interface IAccesoDAO {
 void guardar(Acceso acceso);
 List<Acceso> listar();

    Acceso buscarUltimoAccesoAbierto(Long idEstudiante);

    List<Acceso> listarRechazosPorFecha(java.time.LocalDate fecha);
}
