import java.util.ArrayList;

public class Aplicacion {

    //Atributos
    private Administrador administrador;
    private ArrayList<Cancion> catalogo;
    private ArrayList<Usuario> usuarios;

    //Constructor completo
    public Aplicacion(Administrador administrador, ArrayList<Cancion> catalogo, ArrayList<Usuario> usuarios) {
        this.administrador = administrador;
        this.catalogo = catalogo;
        this.usuarios = usuarios;
    }

    //Getters
    public Administrador getAdministrador() {
        return administrador;
    }

    public ArrayList<Cancion> getCatalogo() {
        return catalogo;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    //Setters
    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public void setCatalogo(ArrayList<Cancion> catalogo) {
        this.catalogo = catalogo;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void agregarCancion(Cancion cancion) {
        catalogo.add(cancion);
    }

    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    //toString()
    @Override
    public String toString() {
        return "Aplicacion de streaming musical\nAdministrador: " + administrador.getNombreUsuario() +
                "\nCanciones en el catalogo: " + catalogo.size() +
                "\nUsuarios registrados: " + usuarios.size();
    }

}
