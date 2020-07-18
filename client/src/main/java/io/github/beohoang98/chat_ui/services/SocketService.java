/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.services;

import com.google.gson.Gson;
import io.github.beohoang98.chat_ui.App;
import io.github.beohoang98.chat_ui.events.ErrorEvent;
import io.github.beohoang98.chat_ui.events.LoginEvent;
import io.github.beohoang98.chat_ui.events.RegisterEvent;
import io.github.beohoang98.chat_ui.models.UserModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author noobcoder
 */
public class SocketService {

    public Logger logger = Logger.getLogger(SocketService.class.getName());

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
        Gson gson = new Gson();
        if (dataOutputStream != null) {
            dataOutputStream.println(event + " " + gson.toJson(data));
            dataOutputStream.flush();
        } else {
            Logger.getLogger(SocketService.class.getName()).warning("Not connected yet");
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
                if (!m.find()) continue;
                String event = m.group(1);
                String data = m.groupCount() > 1 ? m.group(2) : null;
                handleData(event, data);
            }
        }
    }

    void handleData(@Nonnull String event, @Nullable String data) {
        Gson gson = new Gson();
        System.out.printf("Event %s: %s\n", event, data);
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
