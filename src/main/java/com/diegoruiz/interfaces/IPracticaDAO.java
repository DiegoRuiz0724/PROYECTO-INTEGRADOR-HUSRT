package com.diegoruiz.interfaces;

import java.util.List;
import com.diegoruiz.entities.Practica;

public interface IPracticaDAO {
    void guardar(Practica practica);

    List<Practica> listar();
}
