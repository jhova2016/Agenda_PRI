package com.example.agenda_pri.Models;

public class Element_Peticion {
    String Correo;
    String Aceptado;
    String Admin;

    public Element_Peticion(String correo, String aceptado, String admin) {
        Correo = correo;
        Aceptado = aceptado;
        Admin = admin;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getAceptado() {
        return Aceptado;
    }

    public void setAceptado(String aceptado) {
        Aceptado = aceptado;
    }

    public String getAdmin() {
        return Admin;
    }

    public void setAdmin(String admin) {
        Admin = admin;
    }
}
