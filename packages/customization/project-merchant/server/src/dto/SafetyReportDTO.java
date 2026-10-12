package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.SafetyReport;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 安全月报
 * </p>
 *
 * @author zwb
 * @since 2021-06-24
 */

@Data
public class SafetyReportDTO extends ConvertionDTO<SafetyReportDTO, SafetyReport> {

    public SafetyReportDTO() {
    }

    public SafetyReportDTO(SafetyReport safetyReport) {
        super();
        map(safetyReport);
    }

    public SafetyReportDTO(List<SafetyReport> safetyReports) {
        super();
        map(safetyReports);
    }

    private static final long serialVersionUID = 1L;

    private String id;

    private String projectId;

    private String firmId;

    private String firmName;

    private String title;

    private String status;

    private String userId;

    private String username;

    private String nickname;

    private LocalDateTime filledTime;

    private LocalDateTime createTime;

    private int summaryFirmSize;

    private List<SafetyReport> summaryFirmReport;

}
