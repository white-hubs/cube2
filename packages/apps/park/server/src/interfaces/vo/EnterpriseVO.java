package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.EnterpriseScaleEnum;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.EnterpriseTypeEnum;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

import java.util.List;

@Data
public class EnterpriseVO extends BasicTenantEntity<Long> {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 法人
     */
    private String corporation;

    /**
     * 类型
     */
    private EnterpriseTypeEnum type;

    /**
     * 类型名
     */
    private String typeName;

    /**
     * 行业类型ID
     */
    private Long industryTypeId;

    /**
     * 行业类型名
     */
    private String industryTypeName;

    /**
     * 规模
     */
    private EnterpriseScaleEnum scale;

    /**
     * 规模名
     */
    private String scaleName;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系人电话
     */
    private String contactsPhone;

    /**
     * 文件信息
     */
    private List<FileVO> fileVOS;

}
