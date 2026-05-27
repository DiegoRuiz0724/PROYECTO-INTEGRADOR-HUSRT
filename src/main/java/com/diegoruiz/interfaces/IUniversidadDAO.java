package com.diegoruiz.interfaces;

import java.util.List;
import com.diegoruiz.entities.Universidad;

public interface IUniversidadDAO {
    void guardar(Universidad universidad);

    List<Universidad> listar();
}
