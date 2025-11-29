package com.parcial3.app.repositorios;
import org.springframework.data.jpa.repository.JpaRepository;
import com.parcial3.app.entidades.Estudiante;
import com.parcial3.app.entidades.EvaluacionSaberPro;

public interface EvaluacionRepositorio extends JpaRepository<EvaluacionSaberPro, Long> {

    EvaluacionSaberPro findByNumeroRegistro(String numeroRegistro);

    EvaluacionSaberPro findByNumeroDocumento(String numeroDocumento);
}
