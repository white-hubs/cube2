package com.guhecloud.cube.base.videogw.server.common.application.commandgateway.command;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VideoFileStoreCommand extends VideoBaseCommand {

    /**
     * 是否本地存储(true:本地转存,false:不做转存)
     */
    private Boolean locallyStored;

    /**
     * 场景编号
     */
    private String sceneCode;

    /**
     * 转存延时时间(秒)
     */
    private Integer delayTime;
}
