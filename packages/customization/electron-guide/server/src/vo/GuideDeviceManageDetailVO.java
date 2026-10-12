package com.guhecloud.cube.srv.electron.guide.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideDeviceManageDetailVO {

    private Long id;

    /**
     * 点位名称
     */
    private String name;

    /**
     * 图标
     */
    private String logo;

    /**
     * 标题
     */
    private String title;

    /**
     * 点位坐标
     */
    private String coordinate;

    /**
     * 层级
     */
    private String zoom;
}
