package io.github.beohoang98.chat_server.utils;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import org.jetbrains.annotations.NotNull;

public class SendJSON {

    public static SendJSON ins = new SendJSON();

    public void send(@NotNull PrintWriter writer, String command,
        Object data) {
        Gson gson = new Gson();
        String dataStr = gson.toJson(data);
        writer.println(command + " " + dataStr);
        writer.flush();
    }

    public void send(@NotNull Socket socket, String command, Object data) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            send(writer, command, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
