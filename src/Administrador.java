public class Administrador {

    //Atributos
    private String correoElectronico;
    private String nombreUsuario;
    private String contrasenia;

    //Constructor completo
    public Administrador(String correoElectronico, String nombreUsuario, String contrasenia) {
        this.correoElectronico = correoElectronico;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
    }

    //Getters
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    //Setters
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    //toString()
    @Override
    public String toString() {
        return "Administrador: " + nombreUsuario + "\nCorreo electronico: " + correoElectronico;
    }

}
