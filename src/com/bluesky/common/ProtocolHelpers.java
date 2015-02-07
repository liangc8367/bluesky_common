package com.bluesky.common;

import com.bluesky.protocol.*;
import java.net.DatagramPacket;
import java.util.logging.Logger;

/**
 * misc helper func to process protocol
 * Created by liangc on 11/01/15.
 */
public class ProtocolHelpers {
    public static String peepProtocol(DatagramPacket packet){
        ProtocolBase proto = ProtocolFactory.getProtocol(packet);
        return proto.toString();
    }
}
