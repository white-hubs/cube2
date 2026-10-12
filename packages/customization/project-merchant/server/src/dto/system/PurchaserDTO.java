package com.guhecloud.cube.deliver.project.merchant.dto.system;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.system.PurchaserDeliver;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商户端-采购商
 * </p>
 *
 * @author bq
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PurchaserDTO extends ConvertionDTO<PurchaserDTO, PurchaserDeliver> implements Serializable {

    private static final long serialVersionUID = 1L;

    public PurchaserDTO() {
    }

    public PurchaserDTO(PurchaserDeliver purchaserDeliver) {
        super();
        map(purchaserDeliver);
    }

    public PurchaserDTO(List<PurchaserDeliver> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 拼音首字母
     */
    private String pinyin;

    /**
     * 性别（男：1，女：2，未知：9）
     */
    private Integer gender;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户头像路径
     */
    private String avatar;

    /**
     * 状态（启用，停用）
     */
    private Boolean enabled;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 备注
     */
    private String remark;

    private String createBy;

    private LocalDateTime createTime;

}
