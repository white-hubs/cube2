package com.guhecloud.cube.biz.adapter.park.server.facade.model;

import com.guhecloud.platform.base.util.DigestUtils;
import lombok.Data;
import lombok.ToString;

import java.text.MessageFormat;

@Data
@ToString
public class HeadResponse extends Head {

    private String desc;

    private String rcode;

    public HeadResponse() {
    }

    public HeadResponse(String mcode, String ver, String msgatr, String safeflg, String key) {
        super(mcode, ver, msgatr, safeflg);
        this.desc = "";
        this.rcode = "0000";
        this.setMac(key);
    }

    @Override
    public void setMac(String key) {
        String seriData = MessageFormat.format("mcode={0}&mid={1}&date={2}&time={3}&ver={4}&msgatr={5}&rcode={6}&desc={7}&safeflg={8}&key={9}",
                                               super.getMcode(),
                                               super.getTime(),
                                               super.getDate(),
                                               super.getTime(),
                                               super.getVer(),
                                               super.getMsgatr(),
                                               super.getSafeflg(),
                                               key
                                              );
        super.setMac(DigestUtils.md5Hex(seriData).toUpperCase());
    }
}
