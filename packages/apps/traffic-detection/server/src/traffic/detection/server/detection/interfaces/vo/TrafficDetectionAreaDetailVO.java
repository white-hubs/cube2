package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 人流区域信息返回参数
 *
 * @author lanchun
 * @date 2022/10/13 14:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDetectionAreaDetailVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 区域模型
     */
    private String modelArea;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 图片
     */
    private List<FileVO> files;
}
