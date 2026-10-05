import java.util.ArrayList;

public class ListaReproduccion {

    //Atributos
    private ArrayList<Cancion> canciones;
    private String fechaCreacion;
    private String nombre;
    private double calificacion;

    //Constructor completo
    public ListaReproduccion(ArrayList<Cancion> canciones, String fechaCreacion, String nombre, double calificacion) {
        this.canciones = canciones;
        this.fechaCreacion = fechaCreacion;
        this.nombre = nombre;
        this.calificacion = calificacion;
    }

    //Getters
    public ArrayList<Cancion> getCanciones() {
        return canciones;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public String getNombre() {
        return nombre;
    }

    public double getCalificacion() {
        return calificacion;
    }

    //Setters
    public void setCanciones(ArrayList<Cancion> canciones) {
        this.canciones = canciones;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public void agregarCancion(Cancion cancion) {
        canciones.add(cancion);
    }

    //toString()
    @Override
    public String toString() {
        String detalle = "";
        for (Cancion cancion : canciones) {
            detalle += "\n  - " + cancion.getNombre();
        }
        return "Lista de reproduccion: " + nombre + "\nFecha de creacion: " + fechaCreacion +
                "\nCalificacion: " + calificacion + "\nCanciones:" + detalle;
    }

}
