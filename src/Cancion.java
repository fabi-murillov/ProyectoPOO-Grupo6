public class Cancion {

    //Atributos
    private String nombre;
    private String genero;
    private String artista;
    private String compositor;
    private String fechaLanzamiento;
    private String album; 
    private String caratula;
    private double calificacion;
    private double precio;

    //Constructor completo
    public Cancion(String nombre, String genero, String artista, String compositor,
            String fechaLanzamiento, String album, String caratula, double calificacion, double precio) {
        this.nombre = nombre;
        this.genero = genero;
        this.artista = artista;
        this.compositor = compositor;
        this.fechaLanzamiento = fechaLanzamiento;
        this.album = album;
        this.caratula = caratula;
        this.calificacion = calificacion;
        this.precio = precio;
    }

    //Getters
    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public String getArtista() {
        return artista;
    }

    public String getCompositor() {
        return compositor;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public String getAlbum() {
        return album;
    }

    public String getCaratula() {
        return caratula;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public double getPrecio() {
        return precio;
    }

    //Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public void setCompositor(String compositor) {
        this.compositor = compositor;
    }

    public void setFechaLanzamiento(String fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    //toString()
    @Override
    public String toString() {
        return "Cancion: " + nombre + "\nGenero: " + genero + "\nArtista: " + artista +
                "\nCompositor: " + compositor + "\nFecha de lanzamiento: " + fechaLanzamiento +
                "\nAlbum: " + album + "\nCaratula: " + caratula +
                "\nCalificacion: " + calificacion + "\nPrecio: $" + precio;
    }
}
