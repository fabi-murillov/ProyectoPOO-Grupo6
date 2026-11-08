package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class Usuario extends Persona {

    public static final String AVATAR_PREDETERMINADO = "avatar_predeterminado.png";
    public static final double BONO_BIENVENIDA = 4.99;

    // Atributos
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private String cedula;
    private String avatar;
    private double saldo;
    private ArrayList<Cancion> coleccion;
    private ArrayList<ListaReproduccion> listasReproduccion;

    // Constructor completo
    public Usuario(String nombreCompleto, LocalDate fechaNacimiento, String nacionalidad, String cedula,
            String avatar, String correoElectronico, String nombreUsuario, String contrasenia, double saldo) {
        super(correoElectronico, nombreUsuario, contrasenia);
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.cedula = cedula;
        this.avatar = (avatar == null || avatar.isBlank()) ? AVATAR_PREDETERMINADO : avatar;
        this.saldo = saldo;
        this.coleccion = new ArrayList<>();
        this.listasReproduccion = new ArrayList<>();
    }

    // Getters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public String getCedula() {
        return cedula;
    }

    public String getAvatar() {
        return avatar;
    }

    public double getSaldo() {
        return saldo;
    }

    public ArrayList<Cancion> getColeccion() {
        return coleccion;
    }

    public ArrayList<ListaReproduccion> getListasReproduccion() {
        return listasReproduccion;
    }

    // Setters
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setColeccion(ArrayList<Cancion> coleccion) {
        this.coleccion = coleccion;
    }

    public void setListasReproduccion(ArrayList<ListaReproduccion> listasReproduccion) {
        this.listasReproduccion = listasReproduccion;
    }

    public void agregarAColeccion(Cancion cancion) {
        coleccion.add(cancion);
    }

    public boolean poseeCancion(Cancion cancion) {
        return coleccion.contains(cancion);
    }

    public void agregarListaReproduccion(ListaReproduccion lista) {
        listasReproduccion.add(lista);
    }

    @Override
    public String getRol() {
        return "Usuario";
    }

    // Solo escucha completas las canciones compradas
    @Override
    public boolean puedeEscucharCompleta(Cancion cancion) {
        return poseeCancion(cancion);
    }

    // Solo ve sus propias listas
    @Override
    public ArrayList<ListaReproduccion> getListasVisibles(Aplicacion aplicacion) {
        return listasReproduccion;
    }

    // toString()
    @Override
    public String toString() {
        return "Usuario: " + nombreCompleto + "\nFecha de nacimiento: " + fechaNacimiento +
                "\nNacionalidad: " + nacionalidad + "\nCedula: " + cedula +
                "\nAvatar: " + avatar + "\nCorreo electronico: " + getCorreoElectronico() +
                "\nNombre de usuario: " + getNombreUsuario() +
                "\nSaldo: $" + String.format(Locale.US, "%.2f", saldo) +
                "\nCanciones en coleccion: " + coleccion.size() +
                "\nListas de reproduccion: " + listasReproduccion.size();
    }

}
