package com.guhecloud.cube.base.file.server.client.dto;

import com.guhecloud.cube.base.file.server.vo.FileVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-19  11:03
 * @Description:
 * @Version: 1.0
 */
@Data
public class FileTenantDTO {
    private Long id;
    private Integer code;
    private String name;
    private String address;
    private String location;
    private Integer building;
    private BigDecimal areaCovered;
    private Long logoId;
    private String file;
    private String remark;
    private Boolean enabled;
    private Boolean admin;
    private Boolean recentLogin;
    private Boolean mainEnterprise;
    private BigDecimal floorArea;
    List<FileVO> files;
    private LocalDateTime createTime;
}
