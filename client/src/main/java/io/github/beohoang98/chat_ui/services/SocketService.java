/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import io.github.beohoang98.chat_ui.App;
import io.github.beohoang98.chat_ui.events.ErrorEvent;
import io.github.beohoang98.chat_ui.events.GetMessageResponseEvent;
import io.github.beohoang98.chat_ui.events.LoginEvent;
import io.github.beohoang98.chat_ui.events.MessageEvent;
import io.github.beohoang98.chat_ui.events.OnlineUserEvent;
import io.github.beohoang98.chat_ui.events.RegisterEvent;
import io.github.beohoang98.chat_ui.models.MessageModel;
import io.github.beohoang98.chat_ui.models.UserModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author noobcoder
 */
public class SocketService {

    public Logger logger = LogManager.getRootLogger();
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Timestamp.class, (JsonDeserializer<Timestamp>) (json, t, ctx) -> Timestamp.from(Instant.ofEpochMilli(json.getAsLong())))
        .registerTypeAdapter(Timestamp.class, (JsonSerializer<Timestamp>) (timestamp, t, ctx) -> new JsonPrimitive(timestamp.getTime()))
        .create();

    public static SocketService instance = new SocketService();
    Socket socket;
    PrintWriter dataOutputStream;
    Scanner inputStream;
    Thread listener;

    Map<String, EventCallback> eventHandlers = new ConcurrentHashMap<>();

    public void open(String host, int port) throws UnknownHostException, IOException {
        open(InetAddress.getByName(host), port);
    }

    public void open(InetAddress host, int port) throws UnknownHostException, IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 10000);

        dataOutputStream = new PrintWriter(socket.getOutputStream());
        inputStream = new Scanner(socket.getInputStream());

        if (listener != null && !listener.isInterrupted()) {
            listener.interrupt();
        }
        listener = new Thread(new StartListen());
        listener.start();
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void close() throws IOException {
        listener.interrupt();
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public void send(String event, Object data) throws IOException {
        logger.debug("SEND: " + event + " " + data);
        if (dataOutputStream != null) {
            dataOutputStream.println(event + " " + gson.toJson(data));
            dataOutputStream.flush();
        } else {
            System.err.println("Not connected yet");
        }
    }

    public <T> void on(String event, EventCallback<T> callback) {
        this.eventHandlers.put(event, callback);
    }

    public class StartListen implements Runnable {

        @Override
        public void run() {
            while (!socket.isClosed() && socket.isConnected()) {
                String line = inputStream.nextLine();
                Pattern pattern = Pattern.compile("([\\w-]+)\\s?(.*)?", Pattern.UNICODE_CASE);
                Matcher m = pattern.matcher(line);
                if (!m.find()) {
                    continue;
                }
                String event = m.group(1);
                String data = m.groupCount() > 1 ? m.group(2) : null;
                handleData(event, data);
            }
        }
    }

    void handleData(@Nonnull String event, @Nullable String data) {
        logger.debug(String.format("Event %s: %s\n", event, data));
        switch (event) {
            case "LOGGED": {
                UserModel userModel = gson.fromJson(data, UserModel.class);
                App.eventBus.post(new LoginEvent(userModel));
                break;
            }
            case "REGISTER_SUCCESS": {
                UserModel userModel = gson.fromJson(data, UserModel.class);
                App.eventBus.post(new RegisterEvent(userModel));
                break;
            }
            case "MESSAGE": {
                MessageModel message = gson.fromJson(data, MessageModel.class);
                App.eventBus.post(new MessageEvent(message));
                break;
            }
            case "MESSAGE_LIST": {
                GetMessageResponseEvent resEvent = gson.fromJson(data, GetMessageResponseEvent.class);
                App.eventBus.post(resEvent);
                break;
            }
            case "ONLINE": {
                List<String> users = gson.fromJson(data, ArrayList.class);
                App.eventBus.post(new OnlineUserEvent(users));
                break;
            }
            case "ERROR": {
                App.eventBus.post(new ErrorEvent(data));
                break;
            }
        }
    }

    public interface EventCallback<T> {

        void onNext(T data);
    }

    public static class EventRunThread extends Thread {

        final EventCallback callback;
        final Object data;

        public EventRunThread(EventCallback callback, Object data) {
            super();
            this.callback = callback;
            this.data = data;
        }

        @Override
        public void run() {
            callback.onNext(data);
        }
    }
}
