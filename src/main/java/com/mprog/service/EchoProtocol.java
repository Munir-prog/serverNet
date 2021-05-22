package com.mprog.service;

import com.mprog.dto.MessageDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class EchoProtocol implements Runnable{

    private final MessageService messageService = MessageService.getInstance();
    private  PrintWriter clientOut;
    private BufferedReader clientIn;
    private final SessionSocketServer sessionSocketServer;
    private final Socket socket;
    private String response;

    EchoProtocol(SessionSocketServer sessionSocketServer, Socket socket) {
        this.sessionSocketServer = sessionSocketServer;
        this.socket = socket;
    }

    @Override
    public void run() {
        try(socket){
            tryRun();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tryRun() throws IOException, InterruptedException {
        clientOut = new PrintWriter(socket.getOutputStream(), true,
                StandardCharsets.UTF_8);
        clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                StandardCharsets.UTF_8));

        showHistoryOfMessages(clientOut);
        while (!socket.isClosed()) {
            Thread.sleep(1000L);
            if (clientIn.ready()) {
                var msg = clientIn.readLine();
                checkMessageForResponse(msg);
            }
        }
    }

    private void showHistoryOfMessages(PrintWriter clientOut) {
        var history = messageService.getHistory();
        history.forEach(messageDto -> clientOut.println(messageDto.getMessage()));
    }

    private void checkMessageForResponse(String msg) {
        if (msg.startsWith("response:")) {
            response = msg;
        } else {
            var name = findName();
            if (!msg.equals("")) {
                //
                var messageDto = MessageDto.builder()
                        .message(msg)
                        .build();
                messageService.create(messageDto);
                mailMessages(msg, name);
            }
        }
    }

    private void mailMessages(String msg, String name) {
        sessionSocketServer.getClientsName()
                .keySet()
                .stream()
                .filter(value -> !value.equals(this))
                .map(EchoProtocol::getWriter)
                .forEach(writer -> writer.println(LocalTime.now().format(DateTimeFormatter.ofPattern("k:m-")) + name + " > " + msg));
    }

    private String findName() {
        String userName = "someName";
        var optionalUserName = sessionSocketServer.getClientsName().entrySet().stream()
                .filter(value -> value.getKey().equals(this))
                .map(Map.Entry::getValue)
                .findAny();
        if (optionalUserName.isPresent()){
            userName = optionalUserName.get();
        }
        return userName;
    }

    public PrintWriter getWriter(){
        return clientOut;
    }

    public BufferedReader getClientIn() {
        return clientIn;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}