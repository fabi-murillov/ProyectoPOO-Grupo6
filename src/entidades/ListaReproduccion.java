package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class ListaReproduccion implements Reproducible {

    // Atributos
    private ArrayList<Cancion> canciones;
    private LocalDate fechaCreacion;
    private String nombre;

    // Constructor completo
    public ListaReproduccion(ArrayList<Cancion> canciones, LocalDate fechaCreacion, String nombre) {
        this.canciones = canciones;
        this.fechaCreacion = fechaCreacion;
        this.nombre = nombre;
    }

    // Getters
    public ArrayList<Cancion> getCanciones() {
        return canciones;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    // Setters
    public void setCanciones(ArrayList<Cancion> canciones) {
        this.canciones = canciones;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Promedio de la calificacion de sus canciones
    public double getCalificacion() {
        if (canciones.isEmpty()) {
            return 0.0;
        }
        double suma = 0;
        for (Cancion cancion : canciones) {
            suma += cancion.getCalificacion();
        }
        return Math.round(suma / canciones.size() * 10) / 10.0;
    }

    public void agregarCancion(Cancion cancion) {
        canciones.add(cancion);
    }

    public boolean contiene(Cancion cancion) {
        return canciones.contains(cancion);
    }

    @Override
    public ArrayList<Cancion> obtenerCanciones() {
        return canciones;
    }

    public String resumen() {
        return nombre + " | " + canciones.size() + " canciones | "
                + String.format(Locale.US, "%.1f", getCalificacion()) + "/5.0";
    }

    // toString()
    @Override
    public String toString() {
        String detalle = "";
        for (Cancion cancion : canciones) {
            detalle += "\n  - " + cancion.getNombre() + " (" + cancion.getArtista() + ")";
        }
        if (canciones.isEmpty()) {
            detalle = "\n  (sin canciones)";
        }
        return "Lista de reproduccion: " + nombre + "\nFecha de creacion: " + fechaCreacion +
                "\nCalificacion: " + String.format(Locale.US, "%.1f", getCalificacion()) + "/5.0" +
                "\nCanciones:" + detalle;
    }

}
