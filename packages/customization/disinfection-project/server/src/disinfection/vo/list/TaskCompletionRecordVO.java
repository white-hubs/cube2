package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskCompletionRecordVO {
    List<PlaceRecordVO> placeRecords;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long executorId;
    private String executorName;

    private BigDecimal process;
    private String taskDesc;
    private String appendix;

    private LocalDateTime createTime;
    private String createBy;
    private String createName;
    private List<FileVO> files;

    @Data
    public static class PlaceRecordVO {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long placeId;
        private String placeName;
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long placeTypeId;
        private String placeTypeName;
        private String placeDetail;
        private String appendix;
        private List<FileVO> files;
    }
}
