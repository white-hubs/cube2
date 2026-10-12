package com.guhecloud.cube.srv.gen.loose.works.dto;

import com.guhecloud.cube.srv.gen.loose.works.po.ProjectReformPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 小散工程数据采集
 * @date 2023/9/19 14:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LooseWorksDTO {

    /**
     * 启动的小散工程
     */
    private List<ProjectScheduleDTO> projectScheduleList;

    /**
     * 工程整改单
     */
    private List<ProjectReformPO> projectReformList;

}
