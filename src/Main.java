import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //Listas donde se registran los objetos
        ArrayList<Cancion> catalogo = new ArrayList<>();
        ArrayList<Usuario> usuarios = new ArrayList<>();

        // Objeto administrador
        Administrador administrador = new Administrador("admin@musicstream.com", "admin", "Admin#2026");

        // Objetos cancion (la tercera no pertenece a ningún álbum)
        catalogo.add(new Cancion("Provenza", "Reggaeton", "Karol G", "Karol G", "2022-04-21",
                "Manana Sera Bonito", "caratula_manana.png", 4.7, 1.29));
        catalogo.add(new Cancion("Believer", "Rock", "Imagine Dragons", "Dan Reynolds", "2017-02-01",
                "Evolve", "caratula_evolve.png", 4.4, 1.49));
        catalogo.add(new Cancion("Dance Monkey", "Pop", "Tones and I", "Toni Watson", "2019-05-10",
                "Sin album", "caratula_predeterminada.png", 4.1, 0.99));

        // Objetos usuario
        usuarios.add(new Usuario("Carlos Vargas Rojas", "1998-11-23", "Costa Rica", "304560789",
                "avatar_carlos.png", "carlosvargas@gmail.com", "cvargas", "Carlos#2026", 4.99));
        usuarios.add(new Usuario("Valeria Chaves Mora", "2001-03-15", "Panama", "801230456",
                "avatar_predeterminado.png", "valechaves@gmail.com", "vchaves", "Vale#12345", 4.99));

        // Objeto aplicacion
        Aplicacion aplicacion = new Aplicacion(administrador, catalogo, usuarios);

        // Colección y lista de reproducción del primer usuario
        Usuario usuario1 = usuarios.get(0);
        usuario1.agregarAColeccion(catalogo.get(0));
        usuario1.agregarAColeccion(catalogo.get(1));
        usuario1.agregarAColeccion(catalogo.get(2));

        ListaReproduccion lista1 = new ListaReproduccion(new ArrayList<>(), "2026-10-03", "Para estudiar", 4.3);
        lista1.agregarCancion(catalogo.get(1));
        lista1.agregarCancion(catalogo.get(2));
        usuario1.agregarListaReproduccion(lista1);

        // Colección del segundo usuario
        usuarios.get(1).agregarAColeccion(catalogo.get(0));

        // Mostrar los objetos y el contenido de las listas en consola
        System.out.println(aplicacion + "\n");
        System.out.println(administrador + "\n");
        mostrar("CATALOGO DE CANCIONES", aplicacion.getCatalogo());
        mostrar("USUARIOS", aplicacion.getUsuarios());
        mostrar("LISTAS DE REPRODUCCION DE " + usuario1.getNombreUsuario().toUpperCase(),
                usuario1.getListasReproduccion());

    }

    // Recorre una lista e imprime cada elemento
    private static void mostrar(String titulo, ArrayList<?> lista) {
        System.out.println("===== " + titulo + " =====");
        for (Object elemento : lista) {
            System.out.println(elemento);
            System.out.println();
        }
    }
}
