package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/10
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholeSpaceDTO extends ConvertionDTO<WholeSpaceDTO, Space> {

    public WholeSpaceDTO() {}

    public WholeSpaceDTO(Space space) {
        super();
        map(space);
    }

    public WholeSpaceDTO(List<Space> list) {
        super();
        map(list);
    }

    private String id;

    private String name;

    private String companyId;
    private String companyName;

    private Integer rentStatus;

    private Integer status;

    private List<String> area;

    private String signedWho;

    private Integer owner;

    private LocalDateTime signedTime;
    private String mobile;
    private String nickname;

}
