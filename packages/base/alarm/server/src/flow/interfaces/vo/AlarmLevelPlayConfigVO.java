package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

/**
 * 报警级别播放配置
 *
 * @author lanchun
 * @date 2024/12/4 10:20
 */
@Data
public class AlarmLevelPlayConfigVO {

    private Long id;

    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 是否循环播放
     */
    private Boolean looped;

    /**
     * 播放次数
     */
    private Integer playNum;

    /**
     * 播放文件信息
     */
    private FileVO file;
}
