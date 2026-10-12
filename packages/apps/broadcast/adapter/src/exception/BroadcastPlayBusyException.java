package com.guhecloud.cube.biz.adapter.broadcast.server.exception;

/**
 * 广播播放忙碌异常
 *
 * @author lanchun
 * @date 2022/12/9 15:07
 */
public class BroadcastPlayBusyException extends BroadcastPlayFailException {

    public BroadcastPlayBusyException(String message) {
        super("PLAY_BUSY", message);
    }
}
