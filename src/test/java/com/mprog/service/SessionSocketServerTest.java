package com.mprog.service;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SessionSocketServerTest {

    private BufferedReader clientIn;

    private PrintWriter clientOut;

    @BeforeEach
    void prepare() {

    }
    @Test
    void givenSomeName_whenGetClientsName_thenReturnName() throws IOException {
        this.clientOut = Mockito.mock(PrintWriter.class);
        this.clientIn = Mockito.mock(BufferedReader.class);

        SessionSocketServer sss = new SessionSocketServer(clientOut, clientIn);
        String name = "Test";

//        Mockito.doNothing().when(clientOut).println((String) Mockito.any());
        Mockito.doReturn(name).when(clientIn).readLine();

        Assertions.assertThat(sss.getClientName()).isEqualTo(name);

    }

}