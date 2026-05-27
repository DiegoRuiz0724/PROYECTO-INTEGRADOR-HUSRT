package com.diegoruiz.interfaces;

import com.diegoruiz.entities.Usuario;
import java.util.List;

public interface IUsuarioDAO {
    void guardar(Usuario usuario);
    Usuario buscarPorUsername(String username);
    List<Usuario> listar();
}
