package com.mprog.service;

import com.mprog.dto.UserDto;
import lombok.SneakyThrows;

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

public class SessionSocketServer implements Runnable {

    private static final UserService userService = UserService.getInstance();
    private Map<EchoProtocol, String> clientsName;
    private Map<String, Socket> clientsSockets;
    private static PrintWriter clientOut;
    private static BufferedReader clientIn;
    private static int registrationAndLoginIndex = 0;

    public SessionSocketServer(PrintWriter clientOut, BufferedReader clientIn){
        SessionSocketServer.clientOut = clientOut;
        SessionSocketServer.clientIn = clientIn;
    }
    @Override
    public void run() {
        clientsName = new HashMap<>();
        clientsSockets = new HashMap<>();
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(ADDRESS);
            startCheckingClientsActivity(clientsName);
            acceptClients(serverSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCheckingClientsActivity(Map<EchoProtocol, String> clientsName) {
        var checkingActivity = new CheckingActivity(this);
        var threadForActivity = new Thread(checkingActivity);
        threadForActivity.start();
    }


    private void acceptClients(ServerSocket serverSocket) throws IOException {
        while (true) {
            var clientSocket = serverSocket.accept();
            authenticateClient(clientSocket);
            startClient(clientSocket);
        }
    }

    @SneakyThrows
    private static void authenticateClient(Socket clientSocket) {
        clientOut = new PrintWriter(clientSocket.getOutputStream(), true,
                StandardCharsets.UTF_8);
        clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
                StandardCharsets.UTF_8));

        while (true) {
            clientOut.println("Choose option (1. Register or 2. Login)");
            var s = clientIn.readLine();
            // ERROR of parsing NumberFormatException
            registrationAndLoginIndex = parseStringToInteger(s);
            if (registrationAndLoginIndex == 1) {
                registerClient();
                loginClient();
            } else if (registrationAndLoginIndex == 2) {
                loginClient();
            }
            if (registrationAndLoginIndex == 1 || registrationAndLoginIndex == 2) {
                return;
            }
        }
    }

    private static int parseStringToInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @SneakyThrows
    private static void registerClient() {
        clientOut.println("    * * *  Welcome To Registration  * * *  Email >  ");
        var email = clientIn.readLine();
        clientOut.println("\nPassword > ");
        var password = clientIn.readLine();
        var userDto = UserDto.builder()
                .email(email)
                .password(password)
                .build();

        userService.create(userDto);
    }

    @SneakyThrows
    private static void loginClient() {

        clientOut.println("    * * *  Welcome To Login  * * * Email > ");
        var em = clientIn.readLine();
        clientOut.println("Password > ");
        var pas = clientIn.readLine();
        var userLogin = userService.login(em, pas);
        userLogin.ifPresentOrElse(
                SessionSocketServer::onLoginSuccess,
                SessionSocketServer::onLoginFail
        );
    }

    private static void onLoginSuccess(UserDto userDto) {
        log("connected " + userDto.toString());
    }

    private static void onLoginFail() {
        clientOut.println("No such client!\nPlease retry!");
        registrationAndLoginIndex = 0;
    }

    private void startClient(Socket clientSocket) {
        var clientName = getClientName();
        EchoProtocol echoProtocol = new EchoProtocol(this, clientSocket);
        var thread = new Thread(echoProtocol);
        thread.start();
        clientsName.put(echoProtocol, clientName);
        clientsSockets.put(clientName, clientSocket);
    }

    @SneakyThrows
    String getClientName() {
        String name = null;

        clientOut.println("Login. Input username: ");
        while (name == null) {
            name = clientIn.readLine();
        }
        return name;
    }

    //TODO "CC"
//            var out = new PrintWriter(clientSocket.getOutputStream(), true,
//                    StandardCharsets.UTF_8);
//            var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
//                    StandardCharsets.UTF_8));

    public Map<EchoProtocol, String> getClientsName() {
        return clientsName;
    }

    public Map<String, Socket> getClientsSockets() {
        return clientsSockets;
    }

}

