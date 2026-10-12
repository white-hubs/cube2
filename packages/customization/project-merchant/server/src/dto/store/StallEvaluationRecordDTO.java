package com.guhecloud.cube.deliver.project.merchant.dto.store;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRecord;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRecordTag;
import com.guhecloud.cube.deliver.project.merchant.utils.SensitiveWordUtil;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author benhuabao
 * @date 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StallEvaluationRecordDTO extends ConvertionDTO<StallEvaluationRecordDTO, StallEvaluationRecord> {
    public StallEvaluationRecordDTO() {
    }

    public StallEvaluationRecordDTO(StallEvaluationRecord stallEvaluationRecord) {
        super();
        map(stallEvaluationRecord);
    }

    public StallEvaluationRecordDTO(List<StallEvaluationRecord> list) {
        super();
        map(list);
    }

    private Long id;

    @NotNull
    @NotBlank
    private String projectId;

    private String stallId;

    private String stallName;

    private String code;

    private Long userId;

    private String mobile;

    private String username;

    private String nickname;

    private Integer zScore;

    private Integer sScore;

    private Integer tScore;

    private Integer wScore;

    private Float score;

    private Integer honestyScore;

    private String descr;

    private Boolean hasFile;

    private List<String> tagList;

    private String tags;

    private List<StallEvaluationRecordTag> recordTags;

    private int examineStatus;

    private String examineRemark;

    private LocalDateTime examineTime;

    private List<Long> fileIds;

    private List<FileVO> fileDTOS;

    private String createBy;

    private LocalDateTime createTime;

    public void setDescr(String descr) {
        this.descr = SensitiveWordUtil.replaceSensitiveWord(descr);
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
        if (tagList != null && tagList.size() > 0) {
            this.tags = String.join("~", tagList);
        }
    }

    public void setTags(String tags) {
        this.tags = tags;
        if (StringUtils.isNotEmpty(tags)) {
            this.tagList = Arrays.asList(tags.split("~"));
        }
    }

}
