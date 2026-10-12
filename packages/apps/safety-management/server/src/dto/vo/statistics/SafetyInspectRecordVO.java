package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import com.guhecloud.cube.srv.gen.safety.management.enumeration.InspectionItemTagEnum;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

import java.time.LocalDateTime;

@Data
public class SafetyInspectRecordVO {
    private Long id;
    private LocalDateTime time;
    private String targetName;
    private Integer marketTenant;
    private String marketName;
    private Long userId;
    private String nickname;
    private String result;
    private Integer errNumber;
    private String taskName;

    /**
     * 异常项id
     */
    private String recordItemId;
    /**
     * 异常项
     */
    private String errorItem;
    /**
     * 异常tag
     */
    private String errorTag;

    public String getErrorTagName() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(errorTag)) {
            for (String tag : errorTag.split(",")) {
                String description = InspectionItemTagEnum.getDescription(tag);
                if (StringUtils.isNotBlank(description)) {
                    sb.append(InspectionItemTagEnum.getDescription(tag)).append(",");
                }
            }
            if (sb.length() > 0) {
                return sb.substring(0, sb.length() - 1);
            }
        }
        return sb.toString();
    }
}
