package co.icesi.buscaminas;

import co.icesi.buscaminas.controllers.TCPController;
import co.icesi.buscaminas.services.ServicesImpl;

public class Main {

    public static void main(String[] args) {
        ServicesImpl serv = new ServicesImpl();

        // Servidor enfocado exclusivamente en atender peticiones TCP
        TCPController iceController = new TCPController(serv);
        iceController.startService();
    }
}