package co.icesi.buscaminas.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import co.icesi.buscaminas.client.dtos.Cell;
import co.icesi.buscaminas.client.dtos.Request;
import co.icesi.buscaminas.client.dtos.Response;

public class MainClient {

    private static final String HOST = "localhost";
    private static final int PORT = 12345;
    // Instanciamos tu BuscaminasTCPClient pasando host y puerto al constructor
    private static final BuscaminasTCPClient client = new BuscaminasTCPClient(HOST, PORT);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("=============================================");
            System.out.println("BUSCAMINAS DISTRIBUIDO - CLIENTE TCP");
            System.out.println("=============================================");
            System.out.println("[1] Iniciar nueva partida (Filas, Columnas, Minas)");
            System.out.println("[2] Destapar celda (Fila, Columna)");
            System.out.println("[3] Marcar/Desmarcar bandera (Fila, Columna)");
            System.out.println("[4] Consultar estado actual del tablero");
            System.out.println("[5] Rendirse y revelar tablero completo");
            System.out.println("[6] Salir");
            System.out.print("Seleccione una opción: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1 -> handleInitGame(scanner);
                case 2 -> handleSelectCell(scanner);
                case 3 -> handleMarkCell(scanner);
                case 4 -> handleGetBoard();
                case 5 -> handleSowAll();
                case 6 -> {
                    running = false;
                    System.out.println("¡Gracias por jugar!");
                }
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        scanner.close();
    }

    // --- ACCIONES DEL MENÚ ---

    private static void handleInitGame(Scanner scanner) {
        System.out.print("Filas (n): ");
        int n = scanner.nextInt();
        System.out.print("Columnas (m): ");
        int m = scanner.nextInt();
        System.out.print("Minas: ");
        int minas = scanner.nextInt();

        Request req = new Request();
        req.action = "INIT_GAME";
        req.data = Map.of("n", String.valueOf(n), "m", String.valueOf(m), "minas", String.valueOf(minas));

        sendAndProcess(req);
    }

    private static void handleSelectCell(Scanner scanner) {
        System.out.print("Fila (i): ");
        int i = scanner.nextInt();
        System.out.print("Columna (j): ");
        int j = scanner.nextInt();

        Request req = new Request();
        req.action = "SELECT_CELL";
        req.data = Map.of("i", String.valueOf(i), "j", String.valueOf(j));

        sendAndProcess(req);
    }

    private static void handleMarkCell(Scanner scanner) {
        System.out.print("Fila (i): ");
        int i = scanner.nextInt();
        System.out.print("Columna (j): ");
        int j = scanner.nextInt();

        Request req = new Request();
        req.action = "MARK_CELL";
        req.data = Map.of("i", String.valueOf(i), "j", String.valueOf(j));

        sendAndProcess(req);
    }

    private static void handleGetBoard() {
        Request req = new Request();
        req.action = "GET_BOARD";
        req.data = new HashMap<>();

        sendAndProcess(req);
    }

    private static void handleSowAll() {
        Request req = new Request();
        req.action = "SOW_ALL";
        req.data = new HashMap<>();

        sendAndProcess(req);
    }

    // --- ENVÍO TCP Y PROCESAMIENTO DE RESPUESTAS ---

    // --- RENDERIZADOR DEL TABLERO ---

}