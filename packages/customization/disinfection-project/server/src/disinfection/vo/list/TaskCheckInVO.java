package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务打卡记录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCheckInVO {
    private Long id;
    /**
     * 任务ID
     */
    private Long taskId;
    private String taskName;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 打卡人名称
     */
    private String checkUserName;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    private String locationName;
    /**
     * 文件ids
     */
    private String files;
    /**
     * 文件列表
     */
    private List<FileVO> fileVOList;
    private LocalDateTime createTime;
}
