package com.mprog.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class EchoProtocolTest {

//    @Mock
    private EchoProtocol echoProtocol;

//    @Mock
    private SessionSocketServer sss;

    private Socket socket;

    @BeforeEach
    void prepare() {
        this.echoProtocol = Mockito.spy(new EchoProtocol(sss, socket));
    }


    @Test
    void shouldReturnTrueWhenGivenStringStartedWithResponse(){
        String str = "response: ";

        var result = echoProtocol.checkMessageForResponse(str);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenGivenStringStartedWithoutResponse(){
        String str = "hello man";

        var result = echoProtocol.checkMessageForResponse(str);

        Assertions.assertThat(result).isFalse();
    }


    @Test
    void shouldReturnTrueWhenGivenSomeMessageThatNotEmpty(){
        String name = "jack";
        String msg = "Hello";

        Mockito.doNothing().when(echoProtocol).mailMessages(Mockito.any(), Mockito.any());

        var result = echoProtocol.pushToDbAndMail(msg, name);
        Assertions.assertThat(result).isTrue();

    }

    @Test
    void shouldReturnFalseWhenGivenSomeMessageThatIsEmpty(){
        String name = "jack";
        String msg = "";

        Mockito.doNothing().when(echoProtocol).mailMessages(Mockito.any(), Mockito.any());

        var result = echoProtocol.pushToDbAndMail(msg, name);
        Assertions.assertThat(result).isFalse();

    }
}