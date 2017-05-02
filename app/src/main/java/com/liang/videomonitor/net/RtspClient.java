package com.liang.videomonitor.net;

import com.liang.videomonitor.model.RtspStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by prafly-software on 2017/4/28.
 */

public class RtspClient implements IRtspEvent {

    private RtspStatus rtspStatus;

    private static final int BUFFER_SIZE=4096;

    private static final String VERSION=" RTSP/1.0";

    private boolean isSended=false;

    private InetSocketAddress remoteAddress=null;

    private SocketChannel socketChannel=null;

    private Selector selector=null;

    private String url=null;

    private ByteBuffer sendBuf=null;

    private ByteBuffer receiveBuf=null;

    private String sessionid=null;

    private long seq;

    private String trackInfo=null;

    public boolean isShutRtspThread=true;  //rtsp数据接收线程结束标志位

    private int codec_width=0;

    private int codec_Height=0;

    private String port;//接收rtp包端口

    private String port2;//接收rtsp

    public RtspClient(InetSocketAddress remoteAddress, String url, String port) {
        this.remoteAddress = remoteAddress;
        this.url = url;
        this.port = port;
        isShutRtspThread=false;
        this.port2 = String.valueOf(Integer.valueOf(port, 10) + 1);
        this.sendBuf=ByteBuffer.allocate(BUFFER_SIZE);
        this.sendBuf=ByteBuffer.allocate(BUFFER_SIZE);
        this.seq=1;
        connectRtsp();
    }

    private void connectRtsp()
    {
        // 禁用IPV6模式
        System.setProperty("java.net.preferIPv6Addresses", "false");
        // 创建新的选择器
        try {
            selector = Selector.open();
            socketChannel=SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(remoteAddress);
            socketChannel.register(selector, SelectionKey.OP_ACCEPT | SelectionKey.OP_CONNECT
                    | SelectionKey.OP_READ | SelectionKey.OP_WRITE,this);
            isSended=false;
            rtspStatus=RtspStatus.init;
            Thread.sleep(1000);
            if (socketChannel.finishConnect()){
                System.out.println("端口打开成功");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // 创建socket通道
    }



    @Override
    public void doOption() {

    }

    @Override
    public void doDescribe() {

    }

    @Override
    public void doSetUp() {

    }

    @Override
    public void doPlay() {

    }

    @Override
    public void doPause() {

    }

    @Override
    public void doTearDown() {
        StringBuilder sb = new StringBuilder();
        sb.append("TEARDOWN ");
        sb.append(url);
        sb.append(VERSION);
        sb.append("\r\n");
        sb.append("CSeq: ");
        sb.append(seq++);
        sb.append("\r\n");
        sb.append("Session: ");
        sb.append(sessionid);
        sb.append("\r\n");
        sb.append("\r\n");
        System.out.println(sb.toString());
        send(sb.toString().getBytes());
    }

    private boolean isConnected(){
        return socketChannel!=null&& socketChannel.isConnected();
    }

    @Override
    public void send(byte[] out) {
        if (out==null||out.length<1)
            return ;
        synchronized (sendBuf){
            sendBuf.clear();
            sendBuf.put(out);
            sendBuf.flip();
            if (isConnected()){
                try {
                    socketChannel.write(sendBuf);
                } catch (IOException e) {
                    System.out.println("通道为空或未连接！");
                    e.printStackTrace();
                }
            }
        }
    }

    //注册
    @Override
    public void select() {
        int keyCount=0;
        if (selector==null)
        {
            return;
        }else
        {
            try {
                keyCount=selector.select(1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
