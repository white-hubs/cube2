package com.guhecloud.cube.base.visualization.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  10:56
 * @Description:
 * @Version: 1.0
 */
@Data
public class ModelManageVO {
    private Long id;
    /**
     * 模型名称
     */
    private String name;
    /**
     * 模型类型
     */
    private String modelType;
    private String modelTypeName;
    /**
     * 模型地址
     */
    private String address;
    /**
     * 模型封面
     */
    private FileVO file;
    /**
     * 模型导航
     */
    private List<ModelRouteVO> modelRoute;
}
