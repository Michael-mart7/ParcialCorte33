package com.parcial3.app.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "evaluaciones_saber_pro")
public class EvaluacionSaberPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    @Column(name = "primer_nombre")
    private String primerNombre;

    @Column(name = "segundo_nombre")
    private String segundoNombre;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    @Column(name = "numero_telefonico")
    private String numeroTelefonico;

    @Column(name = "numero_registro")
    private String numeroRegistro;

    private Integer puntaje_total;
    private String saber_pro_nivel;

    private Integer comunicacion_escrita;
    private String comunicacion_escrita_nivel;

    private Integer razonamiento_cuantitativo;
    private String razonamiento_cuantitativo_nivel;

    private Integer lectura_critica;
    private String lectura_critica_nivel;

    private Integer competencias_ciudadanas;
    private String competencias_ciudadanas_nivel;

    private Integer ingles;
    private String ingles_nivel;

    private Integer formulacion_proyectos_ingenieria;
    private String formulacion_proyectos_ingenieria_nivel;

    private Integer pensamiento_cientifico_matematicas;
    private String pensamiento_cientifico_matematicas_nivel;

    private Integer diseno_software;
    private String diseno_software_nivel;

    private String nivel_ingles_europeo;
    private String estado;

    @Column(columnDefinition = "TIMESTAMP")
    private java.sql.Timestamp fecha_registro;

    // ------- GETTERS & SETTERS -------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }

    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getNumeroTelefonico() { return numeroTelefonico; }
    public void setNumeroTelefonico(String numeroTelefonico) { this.numeroTelefonico = numeroTelefonico; }

    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public Integer getPuntaje_total() { return puntaje_total; }
    public void setPuntaje_total(Integer puntaje_total) { this.puntaje_total = puntaje_total; }

    public String getSaber_pro_nivel() { return saber_pro_nivel; }
    public void setSaber_pro_nivel(String saber_pro_nivel) { this.saber_pro_nivel = saber_pro_nivel; }

    public Integer getComunicacion_escrita() { return comunicacion_escrita; }
    public void setComunicacion_escrita(Integer comunicacion_escrita) { this.comunicacion_escrita = comunicacion_escrita; }

    public String getComunicacion_escrita_nivel() { return comunicacion_escrita_nivel; }
    public void setComunicacion_escrita_nivel(String comunicacion_escrita_nivel) { this.comunicacion_escrita_nivel = comunicacion_escrita_nivel; }

    public Integer getRazonamiento_cuantitativo() { return razonamiento_cuantitativo; }
    public void setRazonamiento_cuantitativo(Integer razonamiento_cuantitativo) { this.razonamiento_cuantitativo = razonamiento_cuantitativo; }

    public String getRazonamiento_cuantitativo_nivel() { return razonamiento_cuantitativo_nivel; }
    public void setRazonamiento_cuantitativo_nivel(String razonamiento_cuantitativo_nivel) { this.razonamiento_cuantitativo_nivel = razonamiento_cuantitativo_nivel; }

    public Integer getLectura_critica() { return lectura_critica; }
    public void setLectura_critica(Integer lectura_critica) { this.lectura_critica = lectura_critica; }

    public String getLectura_critica_nivel() { return lectura_critica_nivel; }
    public void setLectura_critica_nivel(String lectura_critica_nivel) { this.lectura_critica_nivel = lectura_critica_nivel; }

    public Integer getCompetencias_ciudadanas() { return competencias_ciudadanas; }
    public void setCompetencias_ciudadanas(Integer competencias_ciudadanas) { this.competencias_ciudadanas = competencias_ciudadanas; }

    public String getCompetencias_ciudadanas_nivel() { return competencias_ciudadanas_nivel; }
    public void setCompetencias_ciudadanas_nivel(String competencias_ciudadanas_nivel) { this.competencias_ciudadanas_nivel = competencias_ciudadanas_nivel; }

    public Integer getIngles() { return ingles; }
    public void setIngles(Integer ingles) { this.ingles = ingles; }

    public String getIngles_nivel() { return ingles_nivel; }
    public void setIngles_nivel(String ingles_nivel) { this.ingles_nivel = ingles_nivel; }

    public Integer getFormulacion_proyectos_ingenieria() { return formulacion_proyectos_ingenieria; }
    public void setFormulacion_proyectos_ingenieria(Integer formulacion_proyectos_ingenieria) { this.formulacion_proyectos_ingenieria = formulacion_proyectos_ingenieria; }

    public String getFormulacion_proyectos_ingenieria_nivel() { return formulacion_proyectos_ingenieria_nivel; }
    public void setFormulacion_proyectos_ingenieria_nivel(String formulacion_proyectos_ingenieria_nivel) { this.formulacion_proyectos_ingenieria_nivel = formulacion_proyectos_ingenieria_nivel; }

    public Integer getPensamiento_cientifico_matematicas() { return pensamiento_cientifico_matematicas; }
    public void setPensamiento_cientifico_matematicas(Integer pensamiento_cientifico_matematicas) { this.pensamiento_cientifico_matematicas = pensamiento_cientifico_matematicas; }

    public String getPensamiento_cientifico_matematicas_nivel() { return pensamiento_cientifico_matematicas_nivel; }
    public void setPensamiento_cientifico_matematicas_nivel(String p) { this.pensamiento_cientifico_matematicas_nivel = p; }

    public Integer getDiseno_software() { return diseno_software; }
    public void setDiseno_software(Integer diseno_software) { this.diseno_software = diseno_software; }

    public String getDiseno_software_nivel() { return diseno_software_nivel; }
    public void setDiseno_software_nivel(String diseno_software_nivel) { this.diseno_software_nivel = diseno_software_nivel; }

    public String getNivel_ingles_europeo() { return nivel_ingles_europeo; }
    public void setNivel_ingles_europeo(String nivel_ingles_europeo) { this.nivel_ingles_europeo = nivel_ingles_europeo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public java.sql.Timestamp getFecha_registro() { return fecha_registro; }
    public void setFecha_registro(java.sql.Timestamp fecha_registro) { this.fecha_registro = fecha_registro; }
}
