import java.util.ArrayList;

public class Usuario {

    // Atributos
    private String nombreCompleto;
    private String fechaNacimiento;
    private String nacionalidad;
    private String cedula;
    private String avatar;
    private String correoElectronico;
    private String nombreUsuario;
    private String contrasenia;
    private double saldo;
    private ArrayList<Cancion> coleccion;
    private ArrayList<ListaReproduccion> listasReproduccion;

    // Constructor completo (la colección y las listas inician vacías)
    public Usuario(String nombreCompleto, String fechaNacimiento, String nacionalidad, String cedula, String avatar,
            String correoElectronico, String nombreUsuario, String contrasenia, double saldo) {
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.cedula = cedula;
        this.avatar = avatar;
        this.correoElectronico = correoElectronico;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.saldo = saldo;
        this.coleccion = new ArrayList<>();
        this.listasReproduccion = new ArrayList<>();
    }

    // Getters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getFechaNacimiento() {
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

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
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

    public void setFechaNacimiento(String fechaNacimiento) {
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

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
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

    // Métodos originados por las relaciones con Cancion y ListaReproduccion
    public void agregarAColeccion(Cancion cancion) {
        coleccion.add(cancion);
    }

    public void agregarListaReproduccion(ListaReproduccion lista) {
        listasReproduccion.add(lista);
    }

    // toString()
    @Override
    public String toString() {
        return "Usuario: " + nombreCompleto + "\nFecha de nacimiento: " + fechaNacimiento +
                "\nNacionalidad: " + nacionalidad + "\nCedula: " + cedula +
                "\nAvatar: " + avatar + "\nCorreo electronico: " + correoElectronico +
                "\nNombre de usuario: " + nombreUsuario + "\nSaldo: $" + saldo +
                "\nCanciones en coleccion: " + coleccion.size() +
                "\nListas de reproduccion: " + listasReproduccion.size();
    }

}
