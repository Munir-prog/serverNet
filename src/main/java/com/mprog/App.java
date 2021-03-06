package com.mprog;

import com.mprog.service.SessionSocketServer;

import static com.mprog.utill.Logger.log;

public class App {
    public static void main(String[] args) {
        log("Server starting...");
        new SessionSocketServer().run();
        log("Server finished");
    }
}
