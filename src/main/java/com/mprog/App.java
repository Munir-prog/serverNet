package com.mprog;

import com.mprog.service.SessionSocketServer;

import static com.mprog.utill.Logger.log;

public class App {
    public static void main(String[] args) {
        log("Server starting...");
        //пришлось так сделать(null, null) чтобы протестировать SessionSocketServer.getClientName
        new SessionSocketServer(null, null).run();
        log("Server finished");
    }
}
