package com.example.agenda_pri.Models;

public class Elemento_Evento {
    String Id;
    String TipoDeEvento;
    String Localidad;
    String Lugar;
    String Informacion;
    String FechaDeEvento;
    String FechaDeEventoLarga;
    String HoraInicio;
    String HoraFinal;
    String Responsable;
    String Vestimenta;
    String EstadoDeEvento;
    String CoordenadasActuales;
    String CoordenadasEvento;
    String FechaDeCalendarizacion;
    String QuienCalendarizo;


    public Elemento_Evento(String id, String tipoDeEvento, String localidad, String lugar, String informacion, String fechaDeEvento, String fechaDeEventoLarga, String horaInicio, String horaFinal, String responsable, String vestimenta, String estadoDeEvento, String coordenadasActuales, String coordenadasEvento, String fechaDeCalendarizacion, String quienCalendarizo) {
        Id = id;
        TipoDeEvento = tipoDeEvento;
        Localidad = localidad;
        Lugar = lugar;
        Informacion = informacion;
        FechaDeEvento = fechaDeEvento;
        FechaDeEventoLarga = fechaDeEventoLarga;
        HoraInicio = horaInicio;
        HoraFinal = horaFinal;
        Responsable = responsable;
        Vestimenta = vestimenta;
        EstadoDeEvento = estadoDeEvento;
        CoordenadasActuales = coordenadasActuales;
        CoordenadasEvento = coordenadasEvento;
        FechaDeCalendarizacion = fechaDeCalendarizacion;
        QuienCalendarizo = quienCalendarizo;
    }

    public String getFechaDeEventoLarga() {
        return FechaDeEventoLarga;
    }

    public void setFechaDeEventoLarga(String fechaDeEventoLarga) {
        FechaDeEventoLarga = fechaDeEventoLarga;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTipoDeEvento() {
        return TipoDeEvento;
    }

    public void setTipoDeEvento(String tipoDeEvento) {
        TipoDeEvento = tipoDeEvento;
    }

    public String getLocalidad() {
        return Localidad;
    }

    public void setLocalidad(String localidad) {
        Localidad = localidad;
    }

    public String getLugar() {
        return Lugar;
    }

    public void setLugar(String lugar) {
        Lugar = lugar;
    }

    public String getInformacion() {
        return Informacion;
    }

    public void setInformacion(String informacion) {
        Informacion = informacion;
    }

    public String getFechaDeEvento() {
        return FechaDeEvento;
    }

    public void setFechaDeEvento(String fechaDeEvento) {
        FechaDeEvento = fechaDeEvento;
    }

    public String getHoraInicio() {
        return HoraInicio;
    }

    public void setHoraInicio(String horaInicio) {
        HoraInicio = horaInicio;
    }

    public String getHoraFinal() {
        return HoraFinal;
    }

    public void setHoraFinal(String horaFinal) {
        HoraFinal = horaFinal;
    }

    public String getResponsable() {
        return Responsable;
    }

    public void setResponsable(String responsable) {
        Responsable = responsable;
    }

    public String getVestimenta() {
        return Vestimenta;
    }

    public void setVestimenta(String vestimenta) {
        Vestimenta = vestimenta;
    }

    public String getEstadoDeEvento() {
        return EstadoDeEvento;
    }

    public void setEstadoDeEvento(String estadoDeEvento) {
        EstadoDeEvento = estadoDeEvento;
    }

    public String getCoordenadasActuales() {
        return CoordenadasActuales;
    }

    public void setCoordenadasActuales(String coordenadasActuales) {
        CoordenadasActuales = coordenadasActuales;
    }

    public String getCoordenadasEvento() {
        return CoordenadasEvento;
    }

    public void setCoordenadasEvento(String coordenadasEvento) {
        CoordenadasEvento = coordenadasEvento;
    }

    public String getFechaDeCalendarizacion() {
        return FechaDeCalendarizacion;
    }

    public void setFechaDeCalendarizacion(String fechaDeCalendarizacion) {
        FechaDeCalendarizacion = fechaDeCalendarizacion;
    }

    public String getQuienCalendarizo() {
        return QuienCalendarizo;
    }

    public void setQuienCalendarizo(String quienCalendarizo) {
        QuienCalendarizo = quienCalendarizo;
    }
}
