package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerDeviceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商户设备类型
 * </p>
 *
 * @author bq
 * @since 2022-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerDeviceTypeDTO extends ConvertionDTO<WholesalerDeviceTypeDTO, WholesalerDeviceType> implements Serializable {

    private static final long serialVersionUID = 1L;

    public WholesalerDeviceTypeDTO() {
    }

    public WholesalerDeviceTypeDTO(WholesalerDeviceType type) {
        super();
        map(type);
    }

    public WholesalerDeviceTypeDTO(List<WholesalerDeviceType> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 父级ID
     */
    private String pid;

    /**
     * 名称
     */
    @Length(message = "名称最大长度不超过40", max = 40)
    private String name;

    /**
     * 是否是危险源
     */
    private boolean isDanger;

    /**
     * 排序
     */
    private int sort;

    /**
     * 排序
     */
    private List<WholesalerDeviceTypeDTO> children;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
