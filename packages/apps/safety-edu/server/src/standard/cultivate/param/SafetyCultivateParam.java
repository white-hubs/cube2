package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 安全培训(二维码中转页)
 * @date 2024/3/19 19:38
 */
@Data
public class SafetyCultivateParam {
    private Long id;
    /**
     * 培训主题
     */
    @NotBlank(message = "培训主题不能为空")
    private String themeName;
    /**
     * 实体id
     */
    @NotNull(message = "请选择对应实体")
    private Long objId;
    /**
     * 培训类型
     */
    @NotBlank(message = "培训类型不能为空")
    private String cultivateType;
    /**
     * 参与人数
     */
    @NotNull(message = "参与人数不能为空")
    private Integer personNum;
    /**
     * 培训日期
     */
    @NotNull(message = "培训日期不能为空")
    private LocalDateTime cultivateDate;
    /**
     * 培训时长(分钟)
     */
    private Integer cultivateDuration;
    /**
     * 现场照片
     */
    private String photo;
}
