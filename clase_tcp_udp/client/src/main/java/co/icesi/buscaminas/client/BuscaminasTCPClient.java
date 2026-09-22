package co.icesi.buscaminas.client;

import co.icesi.buscaminas.client.dtos.Request;
import co.icesi.buscaminas.client.dtos.Response;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class BuscaminasTCPClient {

    private final String host;
    private final int port;
    private final Gson gson;

    public BuscaminasTCPClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.gson = new Gson();
    }

    /**
     * Envía una petición JSON al servidor sobre TCP y recibe la respuesta.
     * En este diseño (Short-lived connection), se abre y cierra el socket en cada llamada.
     */
    public Response sendRequest(Request request) {
        try (
            Socket socket = new Socket(host, port);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // 1. Serializar la petición a JSON
            String jsonRequest = gson.toJson(request);

            // 2. Enviar la trama terminada en salto de línea (\n) y forzar el envío con flush()
            writer.write(jsonRequest);
            writer.newLine();
            writer.flush();

            // 3. Leer la respuesta enviada por el servidor
            String jsonResponse = reader.readLine();

            if (jsonResponse != null) {
                // 4. Deserializar la respuesta a la clase Response
                return gson.fromJson(jsonResponse, Response.class);
            }

        } catch (Exception e) {
            System.err.println("Error de comunicación con el servidor: " + e.getMessage());
        }

        return null;
    }
}