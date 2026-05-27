package com.diegoruiz.interfaces;

import java.util.List;
import com.diegoruiz.entities.Programa;

public interface IProgramaDAO {
    void guardar(Programa programa);

    List<Programa> listar();
}
