package com.guhecloud.cube.srv.electron.guide.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideDeviceManageModifyParam {

    /**
     * 点位名称
     */
    @Size(max = 100, message = "点位名称不能大于100")
    private String name;

    /**
     * 图标
     */
    @Size(max = 500, message = "图标不能大于500")
    private String logo;

    /**
     * 标题
     */
    @Size(max = 100, message = "标题不能大于100")
    private String title;

    /**
     * 点位坐标
     */
    @Size(max = 200, message = "点位坐标不能大于200")
    private String coordinate;

    /**
     * 层级
     */
    @Size(max = 10, message = "层级长度不能大于10")
    private String zoom;
}
