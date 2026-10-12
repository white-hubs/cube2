package com.guhecloud.cube.biz.mdata.server.scene.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

/**
 * 场景类型bim返回参数
 *
 * @author lanchun
 * @date 2021/12/2 17:43
 */
@Data
public class SceneTypeBimVO extends Tree.Simple<Long, SceneTypeBimVO> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 场景类型名称
     */
    private String name;

    /**
     * 场景类型编号
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 序列号
     */
    private String sequence;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 附件返回参数
     */
    private FileVO fileVO;
}
