package com.liang.videomonitor.net;

/**
 * Created by prafly-software on 2017/4/28.
 */

public interface IRtspEvent {

    void doOption();

    void doDescribe();

    void doSetUp();

    void doPlay();

    void doPause();

    void doTearDown();

    void send(byte[] out);

    void select();

}
