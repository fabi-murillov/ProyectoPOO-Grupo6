package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Cancion del catalogo. La calificacion es un atributo derivado: promedio
 * de las calificaciones de los usuarios (una por usuario).
 */
public class Cancion implements Reproducible {

    public static final String CARATULA_PREDETERMINADA = "caratula_predeterminada.png";

    //Atributos
    private String nombre;
    private String genero;
    private String artista;
    private String compositor;
    private LocalDate fechaLanzamiento;
    private String album; // vacio si no pertenece a ningun album
    private String caratula;
    private double precio;
    private String rutaAudio;
    private int duracionSegundos;
    private HashMap<String, Double> calificaciones; // nombreUsuario -> calificacion

    //Constructor completo (las calificaciones inician vacias)
    public Cancion(String nombre, String genero, String artista, String compositor, LocalDate fechaLanzamiento,
            String album, String caratula, double precio, String rutaAudio, int duracionSegundos) {
        this.nombre = nombre;
        this.genero = genero;
        this.artista = artista;
        this.compositor = compositor;
        this.fechaLanzamiento = fechaLanzamiento;
        this.album = (album == null) ? "" : album;
        this.caratula = (this.album.isBlank() || caratula == null || caratula.isBlank())
                ? CARATULA_PREDETERMINADA : caratula;
        this.precio = precio;
        this.rutaAudio = rutaAudio;
        this.duracionSegundos = duracionSegundos;
        this.calificaciones = new HashMap<>();
    }

    //Getters
    @Override
    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public String getArtista() {
        return artista;
    }

    public String getCompositor() {
        return compositor;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public String getAlbum() {
        return album;
    }

    public String getCaratula() {
        return caratula;
    }

    public double getPrecio() {
        return precio;
    }

    public String getRutaAudio() {
        return rutaAudio;
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public HashMap<String, Double> getCalificaciones() {
        return calificaciones;
    }

    //Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public void setCompositor(String compositor) {
        this.compositor = compositor;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setRutaAudio(String rutaAudio) {
        this.rutaAudio = rutaAudio;
    }

    public void setDuracionSegundos(int duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    public void setCalificaciones(HashMap<String, Double> calificaciones) {
        this.calificaciones = calificaciones;
    }

    // Promedio de las calificaciones (0.0 si nadie la ha calificado)
    public double getCalificacion() {
        if (calificaciones.isEmpty()) {
            return 0.0;
        }
        double suma = 0;
        for (double valor : calificaciones.values()) {
            suma += valor;
        }
        return Math.round(suma / calificaciones.size() * 10) / 10.0;
    }

    // Registra (o reemplaza) la calificacion de un usuario
    public void calificar(String nombreUsuario, double valor) {
        calificaciones.put(nombreUsuario, valor);
    }

    @Override
    public ArrayList<Cancion> obtenerCanciones() {
        ArrayList<Cancion> unica = new ArrayList<>();
        unica.add(this);
        return unica;
    }

    // Linea corta para listados
    public String resumen() {
        return nombre + " - " + artista + " | " + genero + " | $" + String.format(Locale.US, "%.2f", precio)
                + " | " + String.format(Locale.US, "%.1f", getCalificacion()) + "/5.0";
    }

    // Misma cancion si coinciden nombre y artista
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cancion)) {
            return false;
        }
        Cancion otra = (Cancion) obj;
        return nombre.equalsIgnoreCase(otra.nombre) && artista.equalsIgnoreCase(otra.artista);
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode() * 31 + artista.toLowerCase().hashCode();
    }

    //toString()
    @Override
    public String toString() {
        return "Cancion: " + nombre + "\nGenero: " + genero + "\nArtista: " + artista +
                "\nCompositor: " + compositor + "\nFecha de lanzamiento: " + fechaLanzamiento +
                "\nAlbum: " + (album.isBlank() ? "Sin album" : album) + "\nCaratula: " + caratula +
                "\nDuracion: " + duracionSegundos / 60 + ":" + String.format("%02d", duracionSegundos % 60) +
                "\nCalificacion: " + String.format(Locale.US, "%.1f", getCalificacion()) + "/5.0 (" +
                calificaciones.size() + " voto(s))" +
                "\nPrecio: $" + String.format(Locale.US, "%.2f", precio);
    }
}
