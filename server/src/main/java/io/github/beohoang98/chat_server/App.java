package io.github.beohoang98.chat_server;

import java.io.IOException;

public class App {
    ChatServer server;
    int conLimit = 16;

    public static void main(String[] args) {
        App app = new App();
        app.start(8080, "0.0.0.0");
    }

    public void start(int port, String address) {
        Runtime.getRuntime().addShutdownHook(new ExitHandler());
        try {
            server = new ChatServer(port, conLimit, address);
            System.out.printf("Chat Server listen on %s:%d\n", address, port);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ExitHandler extends Thread {
        @Override
        public void run() {
            try {
                server.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
