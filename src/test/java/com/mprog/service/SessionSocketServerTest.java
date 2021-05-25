package com.mprog.service;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith({
        MockitoExtension.class
})
class SessionSocketServerTest {

    @BeforeEach
    void prepare() {
// Мы можем использовать это вместо MockitoExtension
//        this.clientOut = Mockito.mock(PrintWriter.class);
//        this.clientIn = Mockito.mock(BufferedReader.class);
//        this.sss = new SessionSocketServer(clientOut, clientIn);
    }

    @Mock
    private PrintWriter clientOut;
    @Mock
    private BufferedReader clientIn;
    @InjectMocks
    private SessionSocketServer sss;

    @Test
    void givenString_whenParseStringToInteger_thenReturnInteger(){
        String test = "123";
        var result = SessionSocketServer.parseStringToInteger(test);
        Assertions.assertThat(result).isEqualTo(123);
    }

    @Test
    void givenSomeName_whenGetClientsName_thenReturnName() throws IOException {
        String name = "Test";
        Mockito.doNothing().when(clientOut).println((String) Mockito.any());
        Mockito.doReturn(name).when(clientIn).readLine();

        Assertions.assertThat(sss.getClientName()).isEqualTo(name);
    }

}

