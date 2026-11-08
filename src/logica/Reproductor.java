package logica;

import entidades.Cancion;
import entidades.Persona;
import entidades.Reproducible;
import excepciones.AplicacionException;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Reproductor {

    public static final int SEGUNDOS_PREVISUALIZACION = 30;

    // Atributos
    private Random aleatorio;

    // Constructor completo
    public Reproductor(Random aleatorio) {
        this.aleatorio = aleatorio;
    }

    // Getters
    public Random getAleatorio() {
        return aleatorio;
    }

    // Setters
    public void setAleatorio(Random aleatorio) {
        this.aleatorio = aleatorio;
    }

    // Reproduce una cancion o una lista
    public void reproducir(Persona persona, Reproducible elemento) throws AplicacionException {
        for (Cancion cancion : elemento.obtenerCanciones()) {
            reproducirCancion(persona, cancion);
        }
    }

    // Reproduce y saca de la cola todo lo que la persona haya puesto en ella
    public void reproducirCola(Persona persona) throws AplicacionException {
        while (!persona.getCola().isEmpty()) {
            reproducir(persona, persona.desencolar());
        }
    }

    private void reproducirCancion(Persona persona, Cancion cancion) throws AplicacionException {
        if (persona.puedeEscucharCompleta(cancion)) {
            System.out.println("Reproduciendo: " + cancion.getNombre() + " - " + cancion.getArtista()
                    + " (ENTER para detener)");
            reproducirSegmento(cancion, 0, cancion.getDuracionSegundos());
        } else {
            int duracion = cancion.getDuracionSegundos();
            int inicio = duracion > SEGUNDOS_PREVISUALIZACION
                    ? aleatorio.nextInt(duracion - SEGUNDOS_PREVISUALIZACION + 1) : 0;
            int segundos = Math.min(SEGUNDOS_PREVISUALIZACION, duracion);
            System.out.println("Previsualizacion de " + segundos + " s de: " + cancion.getNombre()
                    + " (desde el segundo " + inicio + ", ENTER para detener). Compra la cancion para escucharla completa.");
            reproducirSegmento(cancion, inicio, segundos);
        }
    }

    // Lee el archivo y envia al dispositivo de audio solo el tramo pedido
    private void reproducirSegmento(Cancion cancion, int inicioSegundos, int duracionSegundos)
            throws AplicacionException {
        AudioInputStream flujo = null;
        SourceDataLine linea = null;
        try {
            flujo = AudioSystem.getAudioInputStream(new File(cancion.getRutaAudio()));
            AudioFormat original = flujo.getFormat();
            if (original.getEncoding() != AudioFormat.Encoding.PCM_SIGNED
                    || original.getSampleSizeInBits() != 16 || original.isBigEndian()) {
                AudioFormat pcm = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, original.getSampleRate(), 16,
                        original.getChannels(), original.getChannels() * 2, original.getSampleRate(), false);
                flujo = AudioSystem.getAudioInputStream(pcm, flujo);
            }
            AudioFormat formato = flujo.getFormat();
            int tamanioFrame = formato.getFrameSize();

            linea = AudioSystem.getSourceDataLine(formato);
            linea.open(formato);
            linea.start();

            long bytesASaltar = (long) (inicioSegundos * formato.getFrameRate()) * tamanioFrame;
            while (bytesASaltar > 0) {
                long saltados = flujo.skip(bytesASaltar);
                if (saltados <= 0) {
                    break;
                }
                bytesASaltar -= saltados;
            }
            long bytesATocar = (long) (duracionSegundos * formato.getFrameRate()) * tamanioFrame;

            byte[] buffer = new byte[tamanioFrame * 1024];
            boolean detenida = false;
            while (bytesATocar > 0 && !detenida) {
                int leidos = flujo.read(buffer, 0, (int) Math.min(buffer.length, bytesATocar));
                if (leidos <= 0) {
                    break;
                }
                linea.write(buffer, 0, leidos);
                bytesATocar -= leidos;
                if (System.in.available() > 0) {
                    while (System.in.available() > 0) {
                        System.in.read();
                    }
                    detenida = true;
                }
            }
            if (detenida) {
                linea.flush();
            } else {
                linea.drain();
            }
        } catch (UnsupportedAudioFileException e) {
            throw new AplicacionException("Formato de audio no soportado: " + cancion.getRutaAudio());
        } catch (IOException e) {
            throw new AplicacionException("No se pudo leer el archivo de audio: " + cancion.getRutaAudio());
        } catch (LineUnavailableException | IllegalArgumentException e) {
            throw new AplicacionException("No hay un dispositivo de audio disponible para reproducir.");
        } finally {
            if (linea != null) {
                linea.close();
            }
            if (flujo != null) {
                try {
                    flujo.close();
                } catch (IOException e) {
                }
            }
        }
    }

    // Duracion en segundos de un archivo de audio 
    public static int calcularDuracionSegundos(String ruta) throws AplicacionException {
        if (ruta == null || ruta.isBlank()) {
            throw new AplicacionException("Debe indicar la ruta del archivo de audio.");
        }
        File archivo = new File(ruta.trim());
        if (!archivo.isFile()) {
            throw new AplicacionException("No existe el archivo de audio: " + ruta);
        }
        try (AudioInputStream flujo = AudioSystem.getAudioInputStream(archivo)) {
            long frames = flujo.getFrameLength();
            if (frames <= 0) {
                throw new AplicacionException("No se pudo determinar la duracion del audio: " + ruta);
            }
            return Math.max(1, Math.round(frames / flujo.getFormat().getFrameRate()));
        } catch (UnsupportedAudioFileException e) {
            throw new AplicacionException("Formato de audio no soportado (use WAV, AIFF o AU): " + ruta);
        } catch (IOException e) {
            throw new AplicacionException("No se pudo leer el archivo de audio: " + ruta);
        }
    }

}
