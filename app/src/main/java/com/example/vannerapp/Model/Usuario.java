package com.example.vannerapp.Model;

public class Usuario {
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String bio;

    public Usuario() {
        // Constructor vacío requerido por Firebase
    }

    public Usuario(String nombre, String apellido, String correo, String telefono, String bio) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.bio = bio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getBio() {
        return bio;
    }
}

