package logica;

import entidades.Administrador;
import entidades.Aplicacion;
import entidades.Cancion;
import entidades.ListaReproduccion;
import entidades.Persona;
import entidades.Usuario;
import excepciones.AplicacionException;
import excepciones.CancionNoCompradaException;
import excepciones.CredencialesInvalidasException;
import excepciones.DatoInvalidoException;
import excepciones.SaldoInsuficienteException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;


public class GestorAplicacion {

    public static final String[] TERRITORIOS = {"Costa Rica", "Panama", "Nicaragua", "Honduras",
        "El Salvador", "Guatemala", "Mexico", "Colombia"};
    public static final int TAMANIO_TOP = 3;
    public static final int EDAD_MINIMA = 18;

    // Atributos
    private Aplicacion aplicacion;

    // Constructor completo
    public GestorAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    // Getters
    public Aplicacion getAplicacion() {
        return aplicacion;
    }

    // Setters
    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    // Cuentas 

    public Administrador registrarAdministrador(String correo, String nombreUsuario, String contrasenia,
            String confirmacion) throws AplicacionException {
        if (aplicacion.hayAdministrador()) {
            throw new DatoInvalidoException("Ya existe un administrador registrado.");
        }
        validarCorreo(correo);
        validarNombreUsuario(nombreUsuario);
        validarContrasenia(contrasenia, confirmacion);
        Administrador administrador = new Administrador(correo.trim(), nombreUsuario.trim(), contrasenia);
        aplicacion.setAdministrador(administrador);
        return administrador;
    }

    public Usuario registrarUsuario(String nombreCompleto, LocalDate fechaNacimiento, String nacionalidad,
            String cedula, String avatar, String correo, String nombreUsuario, String contrasenia,
            String confirmacion) throws AplicacionException {
        validarNoVacio(nombreCompleto, "nombre completo");
        if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
            throw new DatoInvalidoException("La fecha de nacimiento no es valida.");
        }
        if (Period.between(fechaNacimiento, LocalDate.now()).getYears() < EDAD_MINIMA) {
            throw new DatoInvalidoException("Solo pueden registrarse personas mayores de edad.");
        }
        boolean territorioValido = false;
        for (String territorio : TERRITORIOS) {
            if (territorio.equals(nacionalidad)) {
                territorioValido = true;
            }
        }
        if (!territorioValido) {
            throw new DatoInvalidoException("La nacionalidad debe ser de la lista de territorios disponibles.");
        }
        if (cedula == null || !cedula.trim().matches("^[A-Za-z0-9-]{5,20}$")) {
            throw new DatoInvalidoException("La cedula debe tener entre 5 y 20 caracteres (letras, numeros o guion).");
        }
        validarCorreo(correo);
        for (Usuario existente : aplicacion.getUsuarios()) {
            if (existente.getCedula().equalsIgnoreCase(cedula.trim())) {
                throw new DatoInvalidoException("Ya existe un usuario con esa cedula.");
            }
            if (existente.getCorreoElectronico().equalsIgnoreCase(correo.trim())) {
                throw new DatoInvalidoException("Ya existe un usuario con ese correo electronico.");
            }
        }
        validarNombreUsuario(nombreUsuario);
        validarContrasenia(contrasenia, confirmacion);
        Usuario usuario = new Usuario(nombreCompleto.trim(), fechaNacimiento, nacionalidad, cedula.trim(), avatar,
                correo.trim(), nombreUsuario.trim(), contrasenia, Usuario.BONO_BIENVENIDA);
        aplicacion.registrarUsuario(usuario);
        return usuario;
    }

    // Devuelve un Administrador o un Usuario segun las credenciales
    public Persona iniciarSesion(String nombreUsuario, String contrasenia) throws AplicacionException {
        if (!aplicacion.hayAdministrador()) {
            throw new AplicacionException("No hay un administrador registrado.");
        }
        Administrador administrador = aplicacion.getAdministrador();
        if (administrador.getNombreUsuario().equals(nombreUsuario) && administrador.getContrasenia().equals(contrasenia)) {
            return administrador;
        }
        Usuario usuario = aplicacion.buscarUsuario(nombreUsuario);
        if (usuario != null && usuario.getContrasenia().equals(contrasenia)) {
            return usuario;
        }
        throw new CredencialesInvalidasException("Nombre de usuario o contrasenia incorrectos.");
    }

    public void cambiarContrasenia(Persona persona, String actual, String nueva, String confirmacion)
            throws AplicacionException {
        if (!persona.getContrasenia().equals(actual)) {
            throw new CredencialesInvalidasException("La contrasenia actual es incorrecta.");
        }
        validarContrasenia(nueva, confirmacion);
        if (nueva.equals(actual)) {
            throw new DatoInvalidoException("La nueva contrasenia debe ser distinta a la actual.");
        }
        persona.setContrasenia(nueva);
    }

    public void recargarSaldo(Usuario usuario, double monto) throws DatoInvalidoException {
        if (monto <= 0 || decimales(monto) > 2) {
            throw new DatoInvalidoException("El monto debe ser mayor que cero, con maximo dos decimales.");
        }
        usuario.setSaldo(Math.round((usuario.getSaldo() + monto) * 100) / 100.0);
    }

    // Catalogo (administrador) 

    public Cancion subirCancion(String nombre, String genero, String artista, String compositor,
            LocalDate fechaLanzamiento, String album, String caratula, double precio, String rutaAudio)
            throws AplicacionException {
        validarNoVacio(nombre, "nombre de la cancion");
        validarNoVacio(genero, "genero");
        validarNoVacio(artista, "artista");
        if (fechaLanzamiento == null) {
            throw new DatoInvalidoException("Debe indicar la fecha de lanzamiento.");
        }
        if (precio <= 0 || decimales(precio) > 2) {
            throw new DatoInvalidoException("El precio debe ser mayor que cero, con maximo dos decimales.");
        }
        int duracion = Reproductor.calcularDuracionSegundos(rutaAudio);
        String autor = (compositor == null || compositor.isBlank()) ? artista.trim() : compositor.trim();
        Cancion cancion = new Cancion(nombre.trim(), genero.trim(), artista.trim(), autor, fechaLanzamiento,
                album, caratula, precio, rutaAudio.trim(), duracion);
        if (aplicacion.getCatalogo().contains(cancion)) {
            throw new DatoInvalidoException("Esa cancion ya existe en el catalogo.");
        }
        aplicacion.agregarCancion(cancion);
        return cancion;
    }

    // Busquedas 

    public ArrayList<Cancion> buscarPorNombre(String texto) {
        ArrayList<Cancion> resultado = new ArrayList<>();
        for (Cancion cancion : aplicacion.getCatalogo()) {
            if (coincide(cancion.getNombre(), texto)) {
                resultado.add(cancion);
            }
        }
        return resultado;
    }

    public ArrayList<Cancion> buscarPorGenero(String texto) {
        ArrayList<Cancion> resultado = new ArrayList<>();
        for (Cancion cancion : aplicacion.getCatalogo()) {
            if (coincide(cancion.getGenero(), texto)) {
                resultado.add(cancion);
            }
        }
        return resultado;
    }

    public ArrayList<Cancion> buscarPorArtista(String texto) {
        ArrayList<Cancion> resultado = new ArrayList<>();
        for (Cancion cancion : aplicacion.getCatalogo()) {
            if (coincide(cancion.getArtista(), texto)) {
                resultado.add(cancion);
            }
        }
        return resultado;
    }

    // Un usuario busca entre sus listas; el administrador, entre las de todos
    public ArrayList<ListaReproduccion> buscarListasPorNombre(Persona persona, String texto) {
        ArrayList<ListaReproduccion> resultado = new ArrayList<>();
        for (ListaReproduccion lista : persona.getListasVisibles(aplicacion)) {
            if (coincide(lista.getNombre(), texto)) {
                resultado.add(lista);
            }
        }
        return resultado;
    }

    // Compras, calificaciones y listas

    public void comprarCancion(Usuario usuario, Cancion cancion) throws AplicacionException {
        if (usuario.poseeCancion(cancion)) {
            throw new DatoInvalidoException("Ya compraste esta cancion.");
        }
        if (Math.round(usuario.getSaldo() * 100) < Math.round(cancion.getPrecio() * 100)) {
            throw new SaldoInsuficienteException("Saldo insuficiente. Precio: $"
                    + String.format(Locale.US, "%.2f", cancion.getPrecio()) + " | Saldo: $"
                    + String.format(Locale.US, "%.2f", usuario.getSaldo()));
        }
        usuario.setSaldo(Math.round((usuario.getSaldo() - cancion.getPrecio()) * 100) / 100.0);
        usuario.agregarAColeccion(cancion);
    }

    public void calificarCancion(Usuario usuario, Cancion cancion, double calificacion) throws AplicacionException {
        if (!usuario.poseeCancion(cancion)) {
            throw new CancionNoCompradaException("Solo puedes calificar canciones que ya compraste.");
        }
        if (calificacion < 0.0 || calificacion > 5.0 || decimales(calificacion) > 1) {
            throw new DatoInvalidoException("La calificacion debe estar entre 0.0 y 5.0, con maximo un decimal.");
        }
        cancion.calificar(usuario.getNombreUsuario(), calificacion);
    }

    public ListaReproduccion crearLista(Usuario usuario, String nombre) throws AplicacionException {
        validarNoVacio(nombre, "nombre de la lista");
        for (ListaReproduccion lista : usuario.getListasReproduccion()) {
            if (lista.getNombre().equalsIgnoreCase(nombre.trim())) {
                throw new DatoInvalidoException("Ya tienes una lista con ese nombre.");
            }
        }
        ListaReproduccion nueva = new ListaReproduccion(new ArrayList<>(), LocalDate.now(), nombre.trim());
        usuario.agregarListaReproduccion(nueva);
        return nueva;
    }

    public void agregarCancionALista(Usuario usuario, ListaReproduccion lista, Cancion cancion)
            throws AplicacionException {
        if (!usuario.poseeCancion(cancion)) {
            throw new CancionNoCompradaException("Solo puedes agregar a una lista canciones que ya compraste.");
        }
        if (lista.contiene(cancion)) {
            throw new DatoInvalidoException("La cancion ya esta en la lista.");
        }
        lista.agregarCancion(cancion);
    }

    // Top 3s (se recalculan en cada consulta) 

    public ListaReproduccion top3MejoresCalificadas() {
        ArrayList<Cancion> ordenadas = new ArrayList<>(aplicacion.getCatalogo());
        ordenadas.sort(Comparator.comparingDouble(Cancion::getCalificacion).reversed()
                .thenComparing(Cancion::getNombre));
        return crearTop("Top 3 mejor calificadas", ordenadas);
    }

    public ListaReproduccion top3MasCompradas() {
        ArrayList<Cancion> ordenadas = new ArrayList<>();
        for (Cancion cancion : aplicacion.getCatalogo()) {
            if (contarCompras(cancion) > 0) {
                ordenadas.add(cancion);
            }
        }
        ordenadas.sort(Comparator.comparingInt(this::contarCompras).reversed().thenComparing(Cancion::getNombre));
        return crearTop("Top 3 mas compradas", ordenadas);
    }

    public ListaReproduccion top3MasIncluidasEnListas() {
        ArrayList<Cancion> ordenadas = new ArrayList<>();
        for (Cancion cancion : aplicacion.getCatalogo()) {
            if (contarListasQueContienen(cancion) > 0) {
                ordenadas.add(cancion);
            }
        }
        ordenadas.sort(Comparator.comparingInt(this::contarListasQueContienen).reversed()
                .thenComparing(Cancion::getNombre));
        return crearTop("Top 3 mas incluidas en listas", ordenadas);
    }

    private int contarCompras(Cancion cancion) {
        int total = 0;
        for (Usuario usuario : aplicacion.getUsuarios()) {
            if (usuario.poseeCancion(cancion)) {
                total++;
            }
        }
        return total;
    }

    private int contarListasQueContienen(Cancion cancion) {
        int total = 0;
        for (Usuario usuario : aplicacion.getUsuarios()) {
            for (ListaReproduccion lista : usuario.getListasReproduccion()) {
                if (lista.contiene(cancion)) {
                    total++;
                }
            }
        }
        return total;
    }

    private ListaReproduccion crearTop(String nombre, ArrayList<Cancion> ordenadas) {
        ArrayList<Cancion> primeras = new ArrayList<>();
        for (int i = 0; i < ordenadas.size() && i < TAMANIO_TOP; i++) {
            primeras.add(ordenadas.get(i));
        }
        return new ListaReproduccion(primeras, LocalDate.now(), nombre);
    }

    // Validaciones y utilidades 

    private void validarNoVacio(String texto, String campo) throws DatoInvalidoException {
        if (texto == null || texto.isBlank()) {
            throw new DatoInvalidoException("El campo " + campo + " no puede estar vacio.");
        }
    }

    private void validarCorreo(String correo) throws DatoInvalidoException {
        if (correo == null || !correo.trim().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new DatoInvalidoException("El correo electronico no tiene un formato valido.");
        }
    }

    private void validarNombreUsuario(String nombreUsuario) throws DatoInvalidoException {
        validarNoVacio(nombreUsuario, "nombre de usuario");
        boolean ocupado = aplicacion.hayAdministrador()
                && aplicacion.getAdministrador().getNombreUsuario().equalsIgnoreCase(nombreUsuario.trim());
        for (Usuario usuario : aplicacion.getUsuarios()) {
            if (usuario.getNombreUsuario().equalsIgnoreCase(nombreUsuario.trim())) {
                ocupado = true;
            }
        }
        if (ocupado) {
            throw new DatoInvalidoException("El nombre de usuario \"" + nombreUsuario.trim() + "\" ya esta en uso.");
        }
    }

    private void validarContrasenia(String contrasenia, String confirmacion) throws DatoInvalidoException {
        if (contrasenia == null || contrasenia.length() < 8 || contrasenia.length() > 12) {
            throw new DatoInvalidoException("La contrasenia debe tener entre 8 y 12 caracteres.");
        }
        boolean mayuscula = false;
        boolean minuscula = false;
        boolean numero = false;
        boolean especial = false;
        for (char c : contrasenia.toCharArray()) {
            if (Character.isUpperCase(c)) {
                mayuscula = true;
            } else if (Character.isLowerCase(c)) {
                minuscula = true;
            } else if (Character.isDigit(c)) {
                numero = true;
            } else if (!Character.isWhitespace(c)) {
                especial = true;
            }
        }
        if (!(mayuscula && minuscula && numero && especial)) {
            throw new DatoInvalidoException(
                    "La contrasenia debe incluir una mayuscula, una minuscula, un numero y un caracter especial.");
        }
        if (!contrasenia.equals(confirmacion)) {
            throw new DatoInvalidoException("Las contrasenias ingresadas no coinciden.");
        }
    }

    private int decimales(double valor) {
        return Math.max(0, BigDecimal.valueOf(valor).stripTrailingZeros().scale());
    }

    private boolean coincide(String texto, String criterio) {
        return normalizar(texto).contains(normalizar(criterio));
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto == null ? "" : texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase();
    }

}
