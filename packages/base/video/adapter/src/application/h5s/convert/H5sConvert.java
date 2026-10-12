package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.convert;

public class H5sConvert {

    private H5sConvert() {

    }

    public static String convertStreamType(Integer streamType) {
        String streamTypeStr = "";
        switch (streamType) {
            case 0:
                streamTypeStr = "main";
                break;
            default:
                streamTypeStr = "sub";
        }
        return streamTypeStr;
    }

    public static String convertPtzAction(String command) {
        String h5sPtzAction = "";
        switch (command) {
            case "LEFT":
                h5sPtzAction = "left";
                break;
            case "RIGHT":
                h5sPtzAction = "right";
                break;
            case "UP":
                h5sPtzAction = "up";
                break;
            case "DOWN":
                h5sPtzAction = "down";
                break;
            case "ZOOM_IN":
                h5sPtzAction = "zoomin";
                break;
            case "ZOOM_OUT":
                h5sPtzAction = "zoomout";
                break;
            case "LEFT_UP":
                h5sPtzAction = "upleft";
                break;
            case "LEFT_DOWN":
                h5sPtzAction = "downleft";
                break;
            case "RIGHT_UP":
                h5sPtzAction = "upright";
                break;
            case "RIGHT_DOWN":
                h5sPtzAction = "downright";
                break;
            case "FOCUS_NEAR":
                h5sPtzAction = "focusout";
                break;
            case "FOCUS_FAR":
                h5sPtzAction = "focusin";
                break;
            case "IRIS_ENLARGE":
                h5sPtzAction = "risin";
                break;
            case "IRIS_REDUCE":
                h5sPtzAction = "irisout";
                break;
            case "WIPER_SWITCH":
                h5sPtzAction = "wiperon";
                break;
            default:
                h5sPtzAction = "left";
        }
        return h5sPtzAction;
    }
}
