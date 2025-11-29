package com.parcial3.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.parcial3.app.entidades.Coordinador;

public interface CoordinadorRepositorio extends JpaRepository<Coordinador, Long> {
    Coordinador findByUsuario(String usuario);
}
