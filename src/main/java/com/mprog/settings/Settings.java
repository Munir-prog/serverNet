package com.mprog.settings;

import com.mprog.utill.PropertiesUtil;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public interface Settings {
    String HOST = "app.host";
    String PORT = "app.port";
    SocketAddress ADDRESS = new InetSocketAddress(PropertiesUtil.get(HOST), Integer.parseInt(PropertiesUtil.get(PORT)));
}
