package com.bluesky.common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import java.util.logging.Logger;
import java.lang.IllegalStateException;

/**
 * UDP Service is responsible for send/receive packets to/from
 * connected PTTApp server.
 * Since Andriod doesn't support asynchronous UDP socket, I have
 * to implement such asynchronous behaviour in this class, so Signaling
 * service won't be blocked on receiving.
 *
 * UDP Service is a threaded class for receiving, while sedning is done
 * in caller's context.
 *
 * Created by liangc on 28/12/14.
 */
public class UDPService extends Thread{
    /** Configuration for UDP Service */
    static public class Configuration {
        /* local addr & port */
        public InetSocketAddress addrLocal; // a port of 0 implies any available port
        public InetSocketAddress addrRemote;
        /* thread name, priority */

        public boolean clientMode; // true for client mode,
    }

    /** Completion handler */
    static public interface CompletionHandler {
        /** invoked when receive is done */
        public void completed(DatagramPacket packet);
    }

    /** public methods of UDP Service */

    public UDPService(Configuration config, OLog logger){
        super("UdpSvc");
        mConfig = config;
        mLogger = logger;
    }

    public void setCompletionHandler(CompletionHandler handler){
        mRegisteredHandler = handler;
    }

    public boolean startService(){
        if(!mRunning) {
            mRunning = true;
            try {
                start();
            } catch (IllegalThreadStateException e) {
                mLogger.w(TAG, "UDP Service has already started");
                mRunning = false;
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean stopService(){
        if(mRunning) {
            mRunning = false;
            mSocket.close();
            return true;
        } else {
            return false;
        }
    }

    public boolean send(DatagramPacket packet){
        if(mSocket != null){
            try {
                mSocket.send(packet);
            } catch (IOException e){
                mLogger.w(TAG, "send failed:" + e);
                return false;
            }
            return true;
        }
        return false;
    }

    /** send payload to bounded target
     *
     * @param payload
     * @return
     */
    public boolean send(ByteBuffer payload){
        if(!mBound){
            throw new IllegalStateException();
        }
        DatagramPacket pkt  = new DatagramPacket(payload.array(), payload.capacity());
        return send(pkt);
    }

    public boolean send(InetSocketAddress target, ByteBuffer payload){
        boolean res;
        try {
            DatagramPacket pkt = new DatagramPacket(payload.array(), payload.capacity(), target);
            res = send(pkt);
        } catch (SocketException e) {
            mLogger.w(TAG, "exception in send:" + e);
            res = false;
        }
        return res;
    }

    public void run(){
        if(!bind()){
            return;
        }

        if(mConfig.clientMode){
            if(!connect()){
                return;
            }
        }

        int count = 0;
        mLogger.i(TAG, "udp receiving...");
        while(mRunning){

            byte[]          rxedBuffer = new byte[MAX_UDP_PACKET_LENGTH];
            DatagramPacket  rxedPacket = new DatagramPacket(rxedBuffer, rxedBuffer.length);
            try {
                mSocket.receive(rxedPacket);
            }catch (IOException e){
                mLogger.w(TAG, "rxed failed:" + e);
                continue;
            }

            ++count;
            mLogger.d(TAG, "Rxed: " + count);
            if (mRegisteredHandler != null) {
                mRegisteredHandler.completed(rxedPacket);
            } else {
                rxedPacket = null;
            }

        }
    }

    /** synchronous receive,
     *  @NOTE: not implemented yet, better to throw exception
     */
    public void receive(){
        ;
    }

    /** private methods */
    /** bind and bind udp socket per configuration
     *
     * @return true if success, else false
     */
    private boolean bind(){
        try {
            mLogger.i(TAG, "to bind:" + mConfig.addrLocal);
            mSocket = new DatagramSocket(mConfig.addrLocal); // create and bind
        }catch ( Exception e ){
            mLogger.w(TAG, "failed to bind:" + e);
            mSocket = null;
            return false;
        }
        mLogger.i(TAG, "bound local: " + mSocket.getLocalAddress() + ":" + mSocket.getLocalPort());
        mBound = true;
        return true;
    }

    /** bind and connect udp socket per configuration
     *
     * @return true if success, else false
     */
    private boolean connect(){
        try {
            mSocket.connect(mConfig.addrRemote);
        }catch ( Exception e ){
            mLogger.e(TAG, "failed to connect:" + e);
            mSocket.close();
            mSocket = null;
            return false;
        }
        mLogger.i(TAG, "connected remote:" + mSocket.getInetAddress() + ":" + mSocket.getPort());
        return true;
    }



    /** private members */
    Configuration   mConfig = null;
    boolean         mRunning = false;
    boolean         mBound   = false;
    DatagramSocket  mSocket = null;
    CompletionHandler   mRegisteredHandler = null;

    private OLog mLogger;

    private final static String TAG = ":UDPSvc:";
    private final static int MAX_UDP_PACKET_LENGTH = 1000; //TODO: to make it even smaller
}
