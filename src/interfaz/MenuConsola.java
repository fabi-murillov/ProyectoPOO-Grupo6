package interfaz;

import entidades.Administrador;
import entidades.Aplicacion;
import entidades.Cancion;
import entidades.ListaReproduccion;
import entidades.Persona;
import entidades.Reproducible;
import entidades.Usuario;
import excepciones.AplicacionException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import logica.GestorAplicacion;
import logica.Reproductor;

public class MenuConsola {

    // Atributos
    private Scanner entrada;
    private Aplicacion aplicacion;
    private GestorAplicacion gestor;
    private Reproductor reproductor;

    // Constructor completo
    public MenuConsola(Aplicacion aplicacion, GestorAplicacion gestor, Reproductor reproductor) {
        this.entrada = new Scanner(System.in);
        this.aplicacion = aplicacion;
        this.gestor = gestor;
        this.reproductor = reproductor;
    }

    // Getters
    public Aplicacion getAplicacion() {
        return aplicacion;
    }

    public GestorAplicacion getGestor() {
        return gestor;
    }

    public Reproductor getReproductor() {
        return reproductor;
    }

    // Setters
    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    public void setGestor(GestorAplicacion gestor) {
        this.gestor = gestor;
    }

    public void setReproductor(Reproductor reproductor) {
        this.reproductor = reproductor;
    }

    // Flujo principal

    public void iniciar() {
        try {
            System.out.println("===== APLICACION DE STREAMING MUSICAL =====");
            exigirAdministrador();
            boolean salir = false;
            while (!salir) {
                System.out.println("\n--- MENU PRINCIPAL ---");
                System.out.println("1. Iniciar sesion\n2. Registrarse como usuario\n0. Salir");
                switch (leerEntero("Opcion: ", 0, 2)) {
                    case 1:
                        iniciarSesion();
                        break;
                    case 2:
                        registrarUsuario();
                        break;
                    default:
                        salir = true;
                }
            }
            System.out.println("Hasta pronto.");
        } catch (NoSuchElementException e) {
            System.out.println("\nSe cerro la entrada de datos. Fin del programa.");
        }
    }

    // Sin administrador no se permite ninguna otra accion
    private void exigirAdministrador() {
        while (!aplicacion.hayAdministrador()) {
            System.out.println("\nNo hay un administrador registrado. Debe registrarlo para continuar.");
            try {
                String correo = leerTexto("Correo electronico: ");
                String nombreUsuario = leerTexto("Nombre de usuario: ");
                String contrasenia = leerContrasenia("Contrasenia (8-12 caracteres, mayuscula, minuscula, numero y especial): ");
                String confirmacion = leerContrasenia("Repita la contrasenia: ");
                gestor.registrarAdministrador(correo, nombreUsuario, contrasenia, confirmacion);
                System.out.println("Administrador registrado correctamente.");
            } catch (AplicacionException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void iniciarSesion() {
        try {
            String nombreUsuario = leerTexto("Nombre de usuario: ");
            String contrasenia = leerContrasenia("Contrasenia: ");
            Persona persona = gestor.iniciarSesion(nombreUsuario, contrasenia);
            System.out.println("\nBienvenido, " + persona.getNombreUsuario() + " (" + persona.getRol() + ")");
            if (persona instanceof Usuario) {
                menuUsuario((Usuario) persona);
            } else {
                menuAdministrador((Administrador) persona);
            }
        } catch (AplicacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registrarUsuario() {
        try {
            System.out.println("\n--- REGISTRO DE USUARIO ---");
            String nombreCompleto = leerTexto("Nombre completo: ");
            LocalDate fechaNacimiento = leerFecha("Fecha de nacimiento (aaaa-mm-dd): ");
            System.out.println("Nacionalidad:");
            for (int i = 0; i < GestorAplicacion.TERRITORIOS.length; i++) {
                System.out.println("  " + (i + 1) + ". " + GestorAplicacion.TERRITORIOS[i]);
            }
            String nacionalidad = GestorAplicacion.TERRITORIOS[leerEntero("Seleccione: ", 1,
                    GestorAplicacion.TERRITORIOS.length) - 1];
            String cedula = leerTexto("Cedula: ");
            String avatar = leerTexto("Avatar (archivo de imagen, ENTER para el predeterminado): ");
            String correo = leerTexto("Correo electronico: ");
            String nombreUsuario = leerTexto("Nombre de usuario: ");
            String contrasenia = leerContrasenia("Contrasenia (8-12 caracteres, mayuscula, minuscula, numero y especial): ");
            String confirmacion = leerContrasenia("Repita la contrasenia: ");
            Usuario usuario = gestor.registrarUsuario(nombreCompleto, fechaNacimiento, nacionalidad, cedula, avatar,
                    correo, nombreUsuario, contrasenia, confirmacion);
            System.out.println("Registro exitoso. Bono de bienvenida: $" + dinero(Usuario.BONO_BIENVENIDA)
                    + ". Saldo actual: $" + dinero(usuario.getSaldo()));
        } catch (AplicacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Menu del usuario final

    private void menuUsuario(Usuario usuario) {
        boolean cerrar = false;
        while (!cerrar) {
            System.out.println("\n--- MENU DE USUARIO --- (Saldo: $" + dinero(usuario.getSaldo()) + ")");
            System.out.println("1. Recargar saldo\n2. Buscar canciones del catalogo\n3. Mi coleccion de canciones");
            System.out.println("4. Mis listas de reproduccion\n5. Top 3s\n6. Cola de reproduccion");
            System.out.println("7. Cambiar contrasenia\n0. Cerrar sesion");
            switch (leerEntero("Opcion: ", 0, 7)) {
                case 1:
                    try {
                        gestor.recargarSaldo(usuario, leerDecimal("Monto a recargar (USD): "));
                        System.out.println("Recarga exitosa. Saldo actual: $" + dinero(usuario.getSaldo()));
                    } catch (AplicacionException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 2:
                    buscarCanciones(usuario);
                    break;
                case 3:
                    Cancion cancion = elegirCancion(usuario.getColeccion());
                    if (cancion != null) {
                        accionesCancion(usuario, cancion);
                    }
                    break;
                case 4:
                    menuListas(usuario);
                    break;
                case 5:
                    menuTop3(usuario);
                    break;
                case 6:
                    menuCola(usuario);
                    break;
                case 7:
                    cambiarContrasenia(usuario);
                    break;
                default:
                    cerrar = true;
            }
        }
    }

    private void menuListas(Usuario usuario) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- MIS LISTAS DE REPRODUCCION ---");
            System.out.println("1. Ver mis listas\n2. Crear una lista\n3. Buscar una lista por nombre\n0. Volver");
            switch (leerEntero("Opcion: ", 0, 3)) {
                case 1:
                    ListaReproduccion lista = elegirLista(usuario.getListasReproduccion());
                    if (lista != null) {
                        accionesLista(usuario, lista);
                    }
                    break;
                case 2:
                    try {
                        ListaReproduccion nueva = gestor.crearLista(usuario, leerTexto("Nombre de la lista: "));
                        System.out.println("Lista creada: " + nueva.getNombre());
                    } catch (AplicacionException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 3:
                    buscarListas(usuario);
                    break;
                default:
                    volver = true;
            }
        }
    }

    // Menu del administrador 
    private void menuAdministrador(Administrador administrador) {
        boolean cerrar = false;
        while (!cerrar) {
            System.out.println("\n--- MENU DE ADMINISTRADOR ---");
            System.out.println("1. Subir una cancion al catalogo\n2. Ver el catalogo completo\n3. Buscar canciones");
            System.out.println("4. Buscar listas de reproduccion (de todos los usuarios)\n5. Top 3s");
            System.out.println("6. Cola de reproduccion\n7. Cambiar contrasenia\n0. Cerrar sesion");
            switch (leerEntero("Opcion: ", 0, 7)) {
                case 1:
                    subirCancion();
                    break;
                case 2:
                    Cancion cancion = elegirCancion(aplicacion.getCatalogo());
                    if (cancion != null) {
                        accionesCancion(administrador, cancion);
                    }
                    break;
                case 3:
                    buscarCanciones(administrador);
                    break;
                case 4:
                    buscarListas(administrador);
                    break;
                case 5:
                    menuTop3(administrador);
                    break;
                case 6:
                    menuCola(administrador);
                    break;
                case 7:
                    cambiarContrasenia(administrador);
                    break;
                default:
                    cerrar = true;
            }
        }
    }

    private void subirCancion() {
        try {
            System.out.println("\n--- SUBIR CANCION ---");
            String nombre = leerTexto("Nombre de la cancion: ");
            String genero = leerTexto("Genero: ");
            String artista = leerTexto("Artista o banda: ");
            String compositor = leerTexto("Compositor (ENTER si es el mismo artista): ");
            LocalDate fecha = leerFecha("Fecha de lanzamiento (aaaa-mm-dd): ");
            String album = leerTexto("Album (ENTER si no pertenece a ninguno): ");
            String caratula = album.isBlank() ? ""
                    : leerTexto("Caratula (archivo de imagen, ENTER para la predeterminada): ");
            double precio = leerDecimal("Precio (USD): ");
            String ruta = leerTexto("Ruta del archivo de audio (WAV, AIFF o AU): ");
            Cancion cancion = gestor.subirCancion(nombre, genero, artista, compositor, fecha, album, caratula,
                    precio, ruta);
            System.out.println("Cancion subida al catalogo:\n" + cancion);
        } catch (AplicacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Opciones compartidas 
    private void buscarCanciones(Persona persona) {
        System.out.println("\n--- BUSCAR CANCIONES ---");
        System.out.println("1. Por nombre\n2. Por genero\n3. Por artista\n0. Volver");
        int criterio = leerEntero("Opcion: ", 0, 3);
        if (criterio == 0) {
            return;
        }
        String texto = leerTexto("Texto a buscar: ");
        ArrayList<Cancion> resultado = criterio == 1 ? gestor.buscarPorNombre(texto)
                : criterio == 2 ? gestor.buscarPorGenero(texto) : gestor.buscarPorArtista(texto);
        Cancion elegida = elegirCancion(resultado);
        if (elegida != null) {
            accionesCancion(persona, elegida);
        }
    }

    private void buscarListas(Persona persona) {
        ListaReproduccion lista = elegirLista(gestor.buscarListasPorNombre(persona,
                leerTexto("Nombre de la lista a buscar: ")));
        if (lista != null) {
            accionesLista(persona, lista);
        }
    }

    // Acciones sobre una cancion
    private void accionesCancion(Persona persona, Cancion cancion) {
        boolean volver = false;
        boolean esUsuario = persona instanceof Usuario;
        while (!volver) {
            System.out.println("\n" + cancion);
            System.out.println("\n1. Reproducir" + (persona.puedeEscucharCompleta(cancion) ? "" : " (previsualizacion de 30 s)"));
            System.out.println("2. Agregar a la cola de reproduccion");
            if (esUsuario) {
                System.out.println("3. Comprar\n4. Calificar\n5. Agregar a una de mis listas");
            }
            System.out.println("0. Volver");
            try {
                switch (leerEntero("Opcion: ", 0, esUsuario ? 5 : 2)) {
                    case 1:
                        reproductor.reproducir(persona, cancion);
                        break;
                    case 2:
                        persona.encolar(cancion);
                        System.out.println("Agregada a la cola.");
                        break;
                    case 3:
                        gestor.comprarCancion((Usuario) persona, cancion);
                        System.out.println("Compra exitosa. Saldo actual: $" + dinero(((Usuario) persona).getSaldo()));
                        break;
                    case 4:
                        gestor.calificarCancion((Usuario) persona, cancion, leerDecimal("Calificacion (0.0 a 5.0): "));
                        System.out.println("Gracias por calificar.");
                        break;
                    case 5:
                        ListaReproduccion lista = elegirLista(((Usuario) persona).getListasReproduccion());
                        if (lista != null) {
                            gestor.agregarCancionALista((Usuario) persona, lista, cancion);
                            System.out.println("Cancion agregada a la lista " + lista.getNombre() + ".");
                        }
                        break;
                    default:
                        volver = true;
                }
            } catch (AplicacionException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Acciones sobre una lista
    private void accionesLista(Persona persona, ListaReproduccion lista) {
        boolean volver = false;
        boolean esDuenio = persona instanceof Usuario && ((Usuario) persona).getListasReproduccion().contains(lista);
        while (!volver) {
            System.out.println("\n" + lista);
            System.out.println("\n1. Reproducir la lista\n2. Agregar la lista a la cola de reproduccion");
            if (esDuenio) {
                System.out.println("3. Agregar una cancion de mi coleccion");
            }
            System.out.println("0. Volver");
            try {
                switch (leerEntero("Opcion: ", 0, esDuenio ? 3 : 2)) {
                    case 1:
                        reproductor.reproducir(persona, lista);
                        break;
                    case 2:
                        persona.encolar(lista);
                        System.out.println("Lista agregada a la cola.");
                        break;
                    case 3:
                        Cancion cancion = elegirCancion(((Usuario) persona).getColeccion());
                        if (cancion != null) {
                            gestor.agregarCancionALista((Usuario) persona, lista, cancion);
                            System.out.println("Cancion agregada.");
                        }
                        break;
                    default:
                        volver = true;
                }
            } catch (AplicacionException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Top 3
    private void menuTop3(Persona persona) {
        boolean volver = false;
        while (!volver) {
            ArrayList<ListaReproduccion> tops = new ArrayList<>();
            tops.add(gestor.top3MejoresCalificadas());
            tops.add(gestor.top3MasCompradas());
            tops.add(gestor.top3MasIncluidasEnListas());
            System.out.println("\n--- TOP 3s ---");
            for (int i = 0; i < tops.size(); i++) {
                System.out.println("\n" + (i + 1) + ". " + tops.get(i));
            }
            System.out.println("\nElija un top para reproducirlo o agregarlo a la cola (0 para volver).");
            int opcion = leerEntero("Opcion: ", 0, tops.size());
            if (opcion == 0) {
                volver = true;
            } else {
                accionesLista(persona, tops.get(opcion - 1));
            }
        }
    }

    private void menuCola(Persona persona) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- COLA DE REPRODUCCION ---");
            if (persona.getCola().isEmpty()) {
                System.out.println("(vacia)");
            }
            int posicion = 1;
            for (Reproducible elemento : persona.getCola()) {
                System.out.println(posicion++ + ". " + (elemento instanceof Cancion ? "[Cancion] " : "[Lista] ")
                        + elemento.getNombre());
            }
            System.out.println("\n1. Reproducir la cola\n2. Vaciar la cola\n0. Volver");
            try {
                switch (leerEntero("Opcion: ", 0, 2)) {
                    case 1:
                        reproductor.reproducirCola(persona);
                        break;
                    case 2:
                        persona.getCola().clear();
                        System.out.println("Cola vaciada.");
                        break;
                    default:
                        volver = true;
                }
            } catch (AplicacionException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void cambiarContrasenia(Persona persona) {
        try {
            String actual = leerContrasenia("Contrasenia actual: ");
            String nueva = leerContrasenia("Nueva contrasenia: ");
            String confirmacion = leerContrasenia("Repita la nueva contrasenia: ");
            gestor.cambiarContrasenia(persona, actual, nueva, confirmacion);
            System.out.println("Contrasenia actualizada.");
        } catch (AplicacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //  Seleccion y lectura de datos 

    private Cancion elegirCancion(ArrayList<Cancion> canciones) {
        if (canciones.isEmpty()) {
            System.out.println("No hay canciones para mostrar.");
            return null;
        }
        for (int i = 0; i < canciones.size(); i++) {
            System.out.println((i + 1) + ". " + canciones.get(i).resumen());
        }
        int opcion = leerEntero("Numero de cancion (0 para volver): ", 0, canciones.size());
        return opcion == 0 ? null : canciones.get(opcion - 1);
    }

    private ListaReproduccion elegirLista(ArrayList<ListaReproduccion> listas) {
        if (listas.isEmpty()) {
            System.out.println("No hay listas para mostrar.");
            return null;
        }
        for (int i = 0; i < listas.size(); i++) {
            System.out.println((i + 1) + ". " + listas.get(i).resumen());
        }
        int opcion = leerEntero("Numero de lista (0 para volver): ", 0, listas.size());
        return opcion == 0 ? null : listas.get(opcion - 1);
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

    private String leerContrasenia(String mensaje) {
        return leerTexto(mensaje);
    }

    private int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            try {
                int valor = Integer.parseInt(leerTexto(mensaje));
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("Ingrese un numero entre " + minimo + " y " + maximo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private double leerDecimal(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido (por ejemplo 4.5).");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje));
            } catch (DateTimeParseException e) {
                System.out.println("Fecha invalida. Use el formato aaaa-mm-dd.");
            }
        }
    }

    private String dinero(double monto) {
        return String.format(Locale.US, "%.2f", monto);
    }

}
