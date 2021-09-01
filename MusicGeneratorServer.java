import java.util.*;
import java.io.*;

import java.net.*;
import java.nio.file.*;

import com.sun.net.httpserver.*;

public class MusicGeneratorServer {
    // Port number used to connect to this server
    private static final int PORT = Integer.parseInt(
                System.getenv().getOrDefault("MUSIC_GENERATOR_PORT", "8000"));
    // HTTP OK response
    private static final int HTTP_OK = 200;

    private static final String[] FUN_MIDI_FILES = 
                {"midi/darude.mid", "midi/pirate.mid", "midi/queen.mid", "midi/mystery.mid"};

    public static void main(String[] args) throws FileNotFoundException, IOException {
        MusicGenerator generator = new MusicGenerator();
        
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 10);

        // Loads the index.html file
        server.createContext("/", (HttpExchange t) -> {
            String html = Files.readString(Paths.get("index.html"));
            send(t, "text/html; charset=utf-8", html);
        });
        
        // Sends the generated MIDI file to the server to be played
        server.createContext("/play", (HttpExchange t) -> {   
            // Here, you can change generator.SAVE_TO to one of the FUN_MIDI_FILES 
            // to listen to something cool!
            byte[] midiBytes = fileToByteArray(new File(generator.SAVE_TO));
            sendByteArray(t, "audio/midi", midiBytes);
        });
        
        // Generates a new file and sends whether or not the generation was successful
        server.createContext("/generate", (HttpExchange t) -> {
            if (generator.generateFile()) {
                send(t, "text/plain", "success");
            } else {
                send(t, "text/plain", "failure");
            }
        });

        server.setExecutor(null);
        server.start();
    }

    // Turns a File, "f" into a byte[] array. If the file doesn't exist or 
    // another error occurs, it prints so to the console
    private static byte[] fileToByteArray(File f) {
        byte[] response = new byte[(int) f.length()];
        try {
            FileInputStream fis = new FileInputStream(f);
            fis.read(response);
            fis.close();
        } catch (IOException ex) {
            System.out.println("Midi to byte array IOException: " + ex);
        }
        return response;
    }

    // Sends data through the HttpExchange "t", assigning the header of the 
    // message the given "contentType"
    private static void send(HttpExchange t, String contentType, String data)
            throws IOException, UnsupportedEncodingException {
        byte[] response = data.getBytes("UTF-8");
        sendByteArray(t, contentType, response);
    }

    // Sends an array of bytes "response" through the HttpExchange "t", assigning the header 
    // of the message the given "contentType"
    private static void sendByteArray(HttpExchange t, String contentType, byte[] response) 
            throws IOException, UnsupportedEncodingException {
        t.getResponseHeaders().set("Content-Type", contentType);
        t.sendResponseHeaders(HTTP_OK, response.length);
        try (OutputStream os = t.getResponseBody()) {
            os.write(response);
        }
    }
}

