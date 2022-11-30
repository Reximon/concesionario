package com.mycompany.myapp.service.dto;

import java.io.Serializable;

public class TrabajadorDTO implements Serializable {
    private String nombre;
    private String apellidos;
    private Long contador;


    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Long getContador() {
        return contador;
    }
    public void setContador(Long contador) {
        this.contador = contador;
    }
}

