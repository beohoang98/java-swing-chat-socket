package io.github.beohoang98.chat_server;

import io.github.beohoang98.chat_server.handler.ClientHandler;
import io.github.beohoang98.chat_server.models.User;
import io.github.beohoang98.chat_server.utils.SendJSON;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer extends ServerSocket {
    public final Map<String, Socket> clients = new ConcurrentHashMap<>();

    public ChatServer(int port, int conLimit, String address) throws IOException {
        super(port, conLimit, InetAddress.getByName(address));
    }

    public void start() throws IOException {
        while (!isClosed()) {
            Socket newClient = this.accept();
            System.out.printf("New client connected %s:%d", newClient
                .getInetAddress(), newClient.getPort());
            ClientHandler handler = new ClientHandler(newClient,
                                                      this);
            handler.setOnLoggedHandler((User user) -> {
                clients.put(user.getUsername(), newClient);
                this.emit("ONLINE", clients);
            });
            handler.setOnClose((User user) -> {
                clients.remove(user.getUsername());
                this.emit("ONLINE", clients);
            });
            
            Thread thread = new Thread(handler);
            thread.start();
        }
    }
    
    public void emit(String command, Object data) {
        for (Socket client : clients.values()) {
            SendJSON.ins.send(client, command, data);
        }
    }

    public Socket getClient(String username) {
        return clients.get(username);
    }
}
