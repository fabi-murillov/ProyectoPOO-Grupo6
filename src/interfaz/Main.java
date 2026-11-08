package interfaz;

import entidades.Aplicacion;
import java.util.ArrayList;
import java.util.Random;
import logica.GestorAplicacion;
import logica.Reproductor;

public class Main {

    public static void main(String[] args) {
        Aplicacion aplicacion = new Aplicacion(null, new ArrayList<>(), new ArrayList<>());
        GestorAplicacion gestor = new GestorAplicacion(aplicacion);
        Reproductor reproductor = new Reproductor(new Random());

        new MenuConsola(aplicacion, gestor, reproductor).iniciar();
    }

}
