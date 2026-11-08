package entidades;

import java.util.ArrayList;

public abstract class Persona {

    // Atributos
    private String correoElectronico;
    private String nombreUsuario;
    private String contrasenia;
    private ArrayList<Reproducible> cola;

    // Constructor completo
    public Persona(String correoElectronico, String nombreUsuario, String contrasenia) {
        this.correoElectronico = correoElectronico;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.cola = new ArrayList<>();
    }

    // Getters
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public ArrayList<Reproducible> getCola() {
        return cola;
    }

    // Setters
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setCola(ArrayList<Reproducible> cola) {
        this.cola = cola;
    }

    public void encolar(Reproducible elemento) {
        cola.add(elemento);
    }

    public Reproducible desencolar() {
        return cola.isEmpty() ? null : cola.remove(0);
    }

    public abstract String getRol();

    public abstract boolean puedeEscucharCompleta(Cancion cancion);

    public abstract ArrayList<ListaReproduccion> getListasVisibles(Aplicacion aplicacion);

    // toString()
    @Override
    public String toString() {
        return getRol() + ": " + nombreUsuario + "\nCorreo electronico: " + correoElectronico;
    }

}
