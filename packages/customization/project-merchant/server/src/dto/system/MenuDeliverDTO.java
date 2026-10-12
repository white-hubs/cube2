package com.guhecloud.cube.deliver.project.merchant.dto.system;

import com.guhecloud.cube.biz.ouaa.client.dto.MenuDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MenuDeliverDTO {

    private String id;

    private String pid;

    private String code;

    private String name;

    private String icon;

    private String path;

    private Integer sort;

    private List<MenuDeliverDTO> children;
}
