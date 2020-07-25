package io.github.beohoang98.chat_server.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.Instant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class SendJSON {

    public static SendJSON ins = new SendJSON();

    public Logger logger = LogManager.getRootLogger();

    public void send(@NotNull PrintWriter writer, String command,
        Object data) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Timestamp.class, (JsonDeserializer<Timestamp>) (json, t, ctx) -> Timestamp.from(Instant.ofEpochMilli(json.getAsLong())))
            .registerTypeAdapter(Timestamp.class, (JsonSerializer<Timestamp>) (timestamp, t, ctx) -> new JsonPrimitive(timestamp.getTime()))
            .create();
        logger.info(String.format("%s - %s", command, data));
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
