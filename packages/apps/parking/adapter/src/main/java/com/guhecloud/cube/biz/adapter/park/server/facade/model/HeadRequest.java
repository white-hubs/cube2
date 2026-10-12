package com.guhecloud.cube.biz.adapter.park.server.facade.model;

import com.guhecloud.platform.base.util.DigestUtils;
import lombok.Data;

import java.text.MessageFormat;

/**
 * 门禁请求头部参数
 *
 * @author lanchun
 * @date 2021/7/19 17:11
 */
@Data
public class HeadRequest extends Head {

    public HeadRequest(String mcode, String ver, String msgatr, String safeflg, String key) {
        super(mcode, ver, msgatr, safeflg);
        this.setMac(key);
    }

    @Override
    public void setMac(String key) {
        String seriData = MessageFormat.format("mcode={0}&mid={1}&date={2}&time={3}&ver={4}&msgatr={5}&safeflg={6}&key={7}",
                                               super.getMcode(),
                                               super.getMid(),
                                               super.getDate(),
                                               super.getTime(),
                                               super.getVer(),
                                               super.getMsgatr(),
                                               super.getSafeflg(),
                                               key
                                              );
        super.setMac(DigestUtils.md5Hex(seriData).toUpperCase());
    }

    @Override
    public String getMac() {
        return super.getMac();
    }
}
