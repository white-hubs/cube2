package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerFirstPart;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerSignatory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

/**
 * @author wangliang
 * @author 2022/01/13
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerFirstPartDTO extends ConvertionDTO<WholesalerFirstPartDTO, WholesalerFirstPart> {

    public WholesalerFirstPartDTO() {}

    public WholesalerFirstPartDTO(WholesalerFirstPart part) {
        super();
        map(part);
    }

    public WholesalerFirstPartDTO(List<WholesalerFirstPart> list) {
        super();
        map(list);
    }

    private Long id;

    private String projectId;

    @Length(max = 100, message = "甲方名称长度不能超过150个字")
    private String name;

    @Length(max = 150, message = "住所长度不能超过150个字")
    private String address;

    private String postCode;

    private String mobile;

    private List<WholesalerSignatory> signatoryList;

    private Set<Long> fileIds;

    private List<FileVO> fileDtos;
}
