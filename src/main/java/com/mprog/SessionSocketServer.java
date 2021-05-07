package com.mprog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.mprog.settings.Settings.*;
import static com.mprog.utill.Logger.log;

public class SessionSocketServer implements Runnable{
    private Map<EchoProtocol, String> clientsName;
    private Map<String, Socket> clientsSockets;

    @Override
    public void run() {
        clientsName = new HashMap<>();
        clientsSockets = new HashMap<>();
        try (ServerSocket serverSocket = new ServerSocket()){
            serverSocket.bind(ADDRESS);
            checkClientsActivity(clientsName);
            acceptClients(serverSocket);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkClientsActivity(Map<EchoProtocol, String> clientsName) {
        var checkingActivity = new CheckingActivity(this);
        var threadForActivity = new Thread(checkingActivity);
        threadForActivity.start();
    }


    private void acceptClients(ServerSocket serverSocket) throws IOException {
        while (true) {
            var clientSocket = serverSocket.accept();
            log("connected " + clientSocket);
            startClient(clientSocket);
        }
    }

    private void startClient(Socket clientSocket) {
        var clientName = getClientName(clientSocket);
            EchoProtocol echoProtocol = new EchoProtocol(this, clientSocket);
        var thread = new Thread(echoProtocol);
        thread.start();
        clientsName.put(echoProtocol, clientName);
        clientsSockets.put(clientName, clientSocket);
    }

    private String getClientName(Socket clientSocket) {
        String name = null;
        try {
            var out = new PrintWriter(clientSocket.getOutputStream(), true,
                    StandardCharsets.UTF_8);
            var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
                    StandardCharsets.UTF_8));

            out.println("Login. Input username: ");
            while(name == null){
                if (in.ready()){
                    name = in.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    public Map<EchoProtocol, String> getClientsName(){
        return clientsName;
    }

    public Map<String, Socket> getClientsSockets() {
        return clientsSockets;
    }
}

