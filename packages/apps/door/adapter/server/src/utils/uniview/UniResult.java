package com.guhecloud.cube.srv.gen.adapter.access.control.server.utils.uniview;

public class UniResult {

    /**
     * 错误信息码
     */
    private Integer ErrCode;
    /**
     * 错误信息
     */
    private String ErrMsg;
    /**
     * 数据信息
     */
    private Object data;

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "UniResult{" +
               "ErrCode=" + ErrCode +
               ", ErrMsg='" + ErrMsg + '\'' +
               ", data=" + data +
               '}';
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getErrCode() {
        return ErrCode;
    }

    public void setErrCode(Integer errCode) {
        ErrCode = errCode;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

}