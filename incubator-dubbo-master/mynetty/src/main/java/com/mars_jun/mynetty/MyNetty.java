package com.mars_jun.mynetty;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.*;

public class MyNetty implements Transporter {
    @Override
    public Server bind(URL url, ChannelHandler handler) throws RemotingException {
        System.err.println("start bind.....");
        return null;
    }

    @Override
    public Client connect(URL url, ChannelHandler handler) throws RemotingException {
        System.err.println("start connect.....");
        return null;
    }
}
