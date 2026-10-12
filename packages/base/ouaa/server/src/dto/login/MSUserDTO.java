package com.guhecloud.cube.biz.ouaa.server.dto.login;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-06  14:08
 * @Description:
 * @Version: 1.0
 */
@Data
public class MSUserDTO {
    private String id;
    private String xm;
    private int xb;
    private String tx_url;
    private String mz;
    private String csrq;
    private String zjhm;
    private String bgdh;
    private String dzyx;
    private String zzdh;
    private String ssbm;
    private String xg_sj;
    private String xg_id;
    private String remark;
    private String px;
    private String mzmc;
    private String ssbmmc;
    private String account;
    private String password;

    public User getUser() {
        User user = new User();
        user.setName(this.getXm());
        user.setGender(this.getGender());
        user.setMobile(this.getBgdh());
        user.setUsername(this.getBgdh());
        user.setPassword(this.getPassword());
        user.setAdmin(false);
        user.setEnabled(true);
        user.setDeleted(false);
        EntityAuditor.populateInnerUpdateAudit(user);
        if (ObjectUtils.isEmpty(user.getName()) || ObjectUtils.isEmpty(user.getMobile())) {
            return null;
        }
        return user;
    }

    private Gender getGender() {
        if (ObjectUtils.isEmpty(this.getXb())) {
            return Gender.NONE;
        } else if (1 == this.getXb()) {
            return Gender.MALE;
        } else if (2 == this.getXb()) {
            return Gender.FEMALE;
        }
        return Gender.NONE;
    }
}
