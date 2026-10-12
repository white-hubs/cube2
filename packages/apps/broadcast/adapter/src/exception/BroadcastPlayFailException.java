package com.guhecloud.cube.biz.adapter.broadcast.server.exception;

/**
 * 广播播放失败异常
 *
 * @author lanchun
 * @date 2022/12/9 15:08
 */
public class BroadcastPlayFailException extends BroadcastException {

    public BroadcastPlayFailException(String message) {
        super("PLAY_FAIL", message);
    }

    public BroadcastPlayFailException(String code, String message) {
        super(code, message);
    }
}
