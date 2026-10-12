package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wql
 * @describe 二维码中转页安全培训
 * @date 2024/3/19 19:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafetyCultivateVO {
    private Long id;
    /**
     * 培训主题
     */
    private String themeName;
    /**
     * 实体id
     */
    private Long objId;
    /**
     * 培训类型
     */
    private String cultivateType;
    /**
     * 参与人数
     */
    private Integer personNum;
    /**
     * 培训日期
     */
    private LocalDateTime cultivateDate;
    /**
     * 培训时长(分钟)
     */
    private Integer cultivateDuration;
    /**
     * 现场照片
     */
    private String photo;
    /**
     * 现场照片文件路径
     */
    private List<FileVO> photoFiles;
    /**
     * 创建人
     */
    private String createBy;
    private String creatorId;
    private ActorTypeEnum creatorType;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
