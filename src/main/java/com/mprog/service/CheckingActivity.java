package com.mprog.service;

import com.mprog.utill.PropertiesUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import static com.mprog.utill.Logger.log;
import static java.nio.charset.StandardCharsets.UTF_8;

public class CheckingActivity implements Runnable {

    private static final String TIME_OUT = "app.timeOut";
    private static final String MAIL_WATCHDOG = "app.mailWatchdog";
    private PrintWriter clientOut;
    public final SessionSocketServer sessionSocketServer;
    public static Long timeOut = Long.parseLong(PropertiesUtil.get(TIME_OUT));
    public static Long mailWatchdog = Long.parseLong(PropertiesUtil.get(MAIL_WATCHDOG));
    private static EchoProtocol deletable = null;
    private Map<String, Socket> clientsSockets;

    public CheckingActivity(SessionSocketServer sessionSocketServer) {
        this.sessionSocketServer = sessionSocketServer;
    }

    @Override
    public void run() {
        try {
            while (waitTwentySec()) {
                var clientsName = sessionSocketServer.getClientsName();
                clientsSockets = sessionSocketServer.getClientsSockets();
                checkActivity(clientsName);
            }
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }

    private void checkActivity(Map<EchoProtocol, String> clientsName) throws InterruptedException, IOException {
        for (Map.Entry<EchoProtocol, String> entry : clientsName.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            informClient(entry, key);
            waitFifteenSec();
            checkResponseMessage(key, clientOut, value);
            key.setResponse(null);
        }
        clientsName.remove(deletable);
    }

    private void informClient(Map.Entry<EchoProtocol, String> entry, EchoProtocol key) {
        clientOut = key.getWriter();
        clientOut.println("Hello " + entry.getValue() + " from server! Please send \"response: ok\" if you are here." +
                "(You have 15 seconds for response)");
    }

    private void checkResponseMessage(EchoProtocol key, PrintWriter clientOut, String value) throws IOException {
        var outputStream = getSocketOutputStream(value);
        var response = key.getResponse();
        if (response != null) {
            if (!response.equals("response: ok")) {
                log("Connection with \"" + value + "\" was closed.");
                outputStream.println("[Warning:] Your session will be closed!");
                key.getSocket().close();
                deletable = key;
            } else {
                clientOut.println("Continue...");
            }
        }else {
            log("Connection with \"" + value + "\" was closed.");
            outputStream.println("[Warning:] Your session will be closed!");
            key.getSocket().close();
            deletable = key;
        }
    }

    private PrintWriter getSocketOutputStream(String value) throws IOException {
        PrintWriter out = null;
        for (Map.Entry<String, Socket> entry : clientsSockets.entrySet()) {
            var name = entry.getKey();
            if (name.equals(value)){
                var valueSocket = entry.getValue();
                out = new PrintWriter(valueSocket.getOutputStream(), true, UTF_8);
            }
        }
        return out;
    }

    private boolean waitTwentySec() throws InterruptedException {
        Thread.sleep(mailWatchdog);
        return true;
    }

    private void waitFifteenSec() throws InterruptedException {
        Thread.sleep(timeOut);
    }
}
