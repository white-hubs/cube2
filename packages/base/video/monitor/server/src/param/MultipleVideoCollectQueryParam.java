package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 多路视频查询参数
 *
 * @author lanchun
 * @date 2021/12/6 19:50
 */
@Data
public class MultipleVideoCollectQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 所属区域id
     */
    private List<String> areaId;

    /**
     * 设备标签集合
     */
    private List<String> tagCodes;
}