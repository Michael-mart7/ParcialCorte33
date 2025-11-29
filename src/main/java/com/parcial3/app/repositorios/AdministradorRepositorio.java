package com.parcial3.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.parcial3.app.entidades.Administrador;

public interface AdministradorRepositorio extends JpaRepository<Administrador, Long> {
    Administrador findByUsuario(String usuario);
}
