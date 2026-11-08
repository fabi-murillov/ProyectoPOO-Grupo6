package entidades;

import java.util.ArrayList;
public class Administrador extends Persona {

    //C onstructor completo
    public Administrador(String correoElectronico, String nombreUsuario, String contrasenia) {
        super(correoElectronico, nombreUsuario, contrasenia);
    }

    @Override
    public String getRol() {
        return "Administrador";
    }

    @Override
    public boolean puedeEscucharCompleta(Cancion cancion) {
        return true;
    }

    @Override
    public ArrayList<ListaReproduccion> getListasVisibles(Aplicacion aplicacion) {
        ArrayList<ListaReproduccion> todas = new ArrayList<>();
        for (Usuario usuario : aplicacion.getUsuarios()) {
            todas.addAll(usuario.getListasReproduccion());
        }
        return todas;
    }

}
