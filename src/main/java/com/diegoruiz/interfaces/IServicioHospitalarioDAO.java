package com.diegoruiz.interfaces;

import java.util.List;
import com.diegoruiz.entities.ServicioHospitalario;

public interface IServicioHospitalarioDAO {
    void guardar(ServicioHospitalario servicioHospitalario);

    List<ServicioHospitalario> listar();
}
