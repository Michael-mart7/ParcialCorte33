package com.parcial3.app.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.parcial3.app.entidades.Estudiante;

public interface EstudianteRepositorio extends JpaRepository<Estudiante, Long> {
    Estudiante findByUsuario(String usuario);
    Estudiante findByCodigo(String codigo);
    List<Estudiante> findByPrograma(String programa);

    
    boolean existsByUsuario(String usuario);
    boolean existsByCodigo(String codigo);
    boolean existsByNumeroRegistro(String numeroRegistro);
}
