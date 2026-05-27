package com.diegoruiz.interfaces;

import java.util.List;
import com.diegoruiz.entities.Docente;

public interface IDocenteDAO {
    void guardar(Docente docente);

    List<Docente> listarDocentes();
}
