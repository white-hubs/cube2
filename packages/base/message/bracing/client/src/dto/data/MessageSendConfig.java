package com.guhecloud.cube.base.bracing.dto.data;

import com.guhecloud.cube.base.bracing.dto.request.AssemblyNoticeObjDTO;
import com.guhecloud.cube.base.bracing.dto.request.AssemblyNoticeWayDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 消息发送配置
 * @date 2023/9/4 16:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MessageSendConfig {

    /**
     * 业务类型
     */

    private String businessType;
    /**
     * 消息配置id
     */
    private Long notifyId;
    /**
     * 消息场景Code
     */
    private String sceneCode;
    /**
     * 通知时间
     */
    private Integer value;
    /**
     * 配置参数
     */
    private String config;
    /**
     * 通知对象
     */
    private List<AssemblyNoticeObjDTO> noticeObjList;
    /**
     * 通知渠道
     */
    private List<AssemblyNoticeWayDTO> noticeWayDTOS;
}
