package io.github.beohoang98.chat_server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.github.beohoang98.chat_server.ChatServer;
import io.github.beohoang98.chat_server.entities.MessageEntity;
import io.github.beohoang98.chat_server.models.Auth;
import io.github.beohoang98.chat_server.models.GetMessageResponse;
import io.github.beohoang98.chat_server.models.InputMessage;
import io.github.beohoang98.chat_server.models.User;
import io.github.beohoang98.chat_server.service.MessageService;
import io.github.beohoang98.chat_server.service.UserService;
import io.github.beohoang98.chat_server.utils.SendJSON;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ClientHandler extends Thread {

    static Logger logger = LogManager.getRootLogger();

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
        super();
        this.socket = socket;
        this.isAuthorized = false;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        writer = new PrintWriter(socket.getOutputStream());
        this.server = server;
    }

    @Override
    public void run() {
        while (!socket.isClosed() && !this.isInterrupted()) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                Pattern pattern = Pattern.compile("([\\w-]+)\\s?(.*)?", Pattern.UNICODE_CASE);
                Matcher m = pattern.matcher(line);
                if (!m.find()) {
                    SendJSON.ins.send(writer, "ERROR", "Missing data");
                } else if (m.groupCount() > 3) {
                    SendJSON.ins.send(writer, "ERROR", "Over data");
                } else {
                    String command = m.group(1);
                    String dataStr = m.group(2);
                    handleInput(command, dataStr);
                }
            } catch (IOException ioException) {
                logger.error(ioException.getMessage());
                break;
            } catch (Exception e) {
                SendJSON.ins.send(writer, "ERROR", e.getMessage());
            }
        }
        try {
            logger.info("Close client " + socket.getRemoteSocketAddress().toString());
            if (!socket.isClosed()) {
                socket.close();
            }
            if (onClose != null) {
                onClose.onClose(user);
            }
        } catch (IOException ioException) {
            logger.error(ioException.getMessage());
            ioException.printStackTrace();
        }
        this.interrupt();
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
                String checkUser = UserService.instance.checkUser(auth.username, auth.password);
                if (checkUser == null) {
                    isAuthorized = true;
                    user = new User(auth.username);
                    SendJSON.ins.send(writer, "LOGGED", user);
                    if (onLoggedHandler != null) {
                        onLoggedHandler.onLogged(user);
                    }
                    return;
                } else {
                    throw new Exception(checkUser);
                }
            }
            case "MESSAGE": {
                InputMessage inputMessage = gson
                    .fromJson(dataStr, InputMessage.class);
                MessageEntity message = MessageService.instance.create(inputMessage, user);

                Socket toSocket = server.getClient(message.getToUsername());
                SendJSON.ins.send(toSocket, "MESSAGE", message);
                if (!inputMessage.getToUsername().equals(user.getUsername())) {
                    SendJSON.ins.send(writer, "MESSAGE", message);
                }
                return;
            }
            case "GET_MESSAGE": {
                String toUsername = gson.fromJson(dataStr, String.class);
                List<MessageEntity> msgList = MessageService.instance.list(user.getUsername(), toUsername);
                logger.debug("MESSAGE_LIST_COUNT: " + msgList.size());
                SendJSON.ins.send(writer, "MESSAGE_LIST", new GetMessageResponse(toUsername, msgList));
            }
            case "GET_ONLINE": {
                List<String> clients = new ArrayList<>(server.clients.keySet());
                server.emit("ONLINE", clients);
                return;
            }
            case "UPLOAD": {
                return;
            }
            default: {
                SendJSON.ins.send(writer, "ERROR", "Unknown command: " + command);
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
