package com.guhecloud.cube.biz.adapter.park.server.facade.model;

import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 门禁请求头部参数
 *
 * @author lanchun
 * @date 2021/7/19 17:11
 */
@Data
public class Head {

    private String mcode;

    private String mid;

    private String date;

    private String time;

    private String ver;

    private String msgatr;

    private String safeflg;

    private String mac;

    public Head() {
    }

    public Head(String mcode, String ver, String msgatr, String safeflg) {
        var date = LocalDateTime.now();
        this.mcode = mcode;
        this.ver = ver;
        this.msgatr = msgatr;
        this.safeflg = safeflg;
        this.date = DateUtils.format(date, "yyyyMMdd");
        this.time = DateUtils.format(date, "HHmmss");
        this.mid = Head.getUUID();
        this.ver = "0001";
    }

    public static String getUUID() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type) {
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char) (rd.nextInt(25) + 65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char) (rd.nextInt(25) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }
}
