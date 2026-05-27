package com.diegoruiz.interfaces;

import java.util.List;

import com.diegoruiz.entities.Horario;

public interface IHorarioDAO {
 void guardar(Horario Horario);
List<Horario> listar();
  

}
