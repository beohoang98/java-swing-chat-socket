package io.github.beohoang98.chat_server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.github.beohoang98.chat_server.ChatServer;
import io.github.beohoang98.chat_server.models.Auth;
import io.github.beohoang98.chat_server.models.InputMessage;
import io.github.beohoang98.chat_server.models.Message;
import io.github.beohoang98.chat_server.models.User;
import io.github.beohoang98.chat_server.service.UserService;
import io.github.beohoang98.chat_server.utils.SendJSON;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public class ClientHandler implements Runnable {

    final Socket socket;
    final ChatServer server;
    boolean isAuthorized;
    User user;
    BufferedReader reader;
    PrintWriter writer;

    OnLoggedHandler onLoggedHandler;
    OnClose onClose;

    public ClientHandler(@NotNull final Socket socket,
        final ChatServer server) throws IOException {
        this.socket = socket;
        this.isAuthorized = false;
        reader = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );
        writer = new PrintWriter(socket.getOutputStream());
        this.server = server;
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    continue;
                }
                String[] split = line.split(" ");
                if (split.length < 2) {
                    SendJSON.ins.send(writer, "ERROR", "Missing data");
                } else if (split.length > 2) {
                    SendJSON.ins.send(writer, "ERROR", "Over data");
                } else {
                    String command = split[0];
                    String dataStr = split[1];
                    handleInput(command, dataStr);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (Exception e) {
                SendJSON.ins.send(writer, "ERROR", "json invalid: " + e.getMessage());
            }
        }
        try {
            socket.close();
            if (onClose != null) {
                onClose.onClose(user);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    void handleInput(@NotNull String command, String dataStr) throws JsonSyntaxException, IOException, Exception {
        Gson gson = new Gson();
        if (!isAuthorized && !(command.equals("LOGIN") || command.equals("REGISTER"))) {
            SendJSON.ins.send(writer, "UN_AUTHORIZED", "You need login first");
        }
        switch (command) {
            case "REGISTER": {
                Auth auth = gson.fromJson(dataStr, Auth.class);
                User user = UserService.instance.register(auth.getUsername(), auth.getPassword());
                if (user == null) {
                    throw new Exception("Cannot register");
                }
                SendJSON.ins.send(writer, "REGISTER_SUCCESS", user);
                return;
            }
            case "LOGIN": {
                Auth auth = gson.fromJson(dataStr, Auth.class);
                if (UserService.instance.checkUser(auth.username, auth.password)) {
                    isAuthorized = true;
                    user = new User(auth.username);
                    SendJSON.ins.send(writer, "LOGGED", user);
                    if (onLoggedHandler != null) {
                        onLoggedHandler.onLogged(user);
                    }
                    return;
                } else {
                    throw new Exception("Login failed");
                }
            }
            case "MESSAGE": {
                InputMessage message = gson
                    .fromJson(dataStr, InputMessage.class);
                Message sendMessage = new Message(message);
                sendMessage.owner = user;
                sendMessage.createdAt = new Timestamp(System
                    .currentTimeMillis());

                Socket toSocket = server.getClient(message.toUser.username);
                SendJSON.ins.send(toSocket, "MESSAGE", sendMessage);
                SendJSON.ins.send(writer, "MESSAGE_SENT", sendMessage);
                return;
            }
            default: {
                SendJSON.ins.send(writer, "ERROR", "Unknown command");
                return;
            }
        }
    }

    public void setOnLoggedHandler(OnLoggedHandler onLoggedHandler) {
        this.onLoggedHandler = onLoggedHandler;
    }

    public void setOnClose(OnClose onClose) {
        this.onClose = onClose;
    }

    public interface OnLoggedHandler {

        void onLogged(@NotNull User user);
    }

    public interface OnClose {

        void onClose(@NotNull User user);
    }
}
