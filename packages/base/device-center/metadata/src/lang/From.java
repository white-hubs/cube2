package com.guhecloud.cube.base.device.center.metadata.lang;

public enum From {
    SELF_MGT_FE("self-mgt-fe", "自身前端管理页面调用"),
    OTHER_MGT_FE("other-mgt-fe", "其他前端管理页面调用"),
    CLIENT_BE("client-be", "客户端后端");

    public final String code;
    public final String desc;

    From(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
