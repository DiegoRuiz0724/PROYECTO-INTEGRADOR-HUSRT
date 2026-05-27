package com.diegoruiz.interfaces;

import java.util.List;
import com.diegoruiz.entities.Documento;

public interface IDocumentoDAO {
    void guardar(Documento documento);

    List<Documento> listar();
}
