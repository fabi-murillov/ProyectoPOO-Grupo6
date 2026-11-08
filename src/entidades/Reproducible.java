package entidades;

import java.util.ArrayList;
public interface Reproducible {

    String getNombre();

    // Canciones que se reproducen, en orden
    ArrayList<Cancion> obtenerCanciones();

}
