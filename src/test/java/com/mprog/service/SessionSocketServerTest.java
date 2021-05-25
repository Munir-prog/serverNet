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

    private SessionSocketServer sss;

    @BeforeEach
    void prepare() {
        this.clientOut = Mockito.mock(PrintWriter.class);
        this.clientIn = Mockito.mock(BufferedReader.class);
        this.sss = new SessionSocketServer(clientOut, clientIn);
    }

    @Test
    void givenString_whenParseStringToInteger_thenReturnInteger(){
        String test = "123";
        var result = SessionSocketServer.parseStringToInteger(test);
        Assertions.assertThat(result).isEqualTo(123);
    }

    @Test
    void givenSomeName_whenGetClientsName_thenReturnName() throws IOException {
        String name = "Test";
//        Mockito.doNothing().when(clientOut).println((String) Mockito.any());
        Mockito.doReturn(name).when(clientIn).readLine();

        Assertions.assertThat(sss.getClientName()).isEqualTo(name);
    }

}