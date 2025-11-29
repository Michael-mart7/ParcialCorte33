package com.parcial3.app.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "coordinadores")
public class Coordinador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;
    private String clave;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;


    public Coordinador() {}

    public Coordinador(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
    }

    public Long getId() { return id; }

    public String getUsuario() { return usuario; }

    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getClave() { return clave; }

    public void setClave(String clave) { this.clave = clave; }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
    
    
}
