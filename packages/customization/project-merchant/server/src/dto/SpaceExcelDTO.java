package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SpaceExcelDTO extends ConvertionDTO<SpaceExcelDTO, Space> {

    public SpaceExcelDTO() {
    }

    public SpaceExcelDTO(Space device) {
        super();
        map(device);
    }

    public SpaceExcelDTO(List<Space> list) {
        super();
        map(list);
    }

    @Excel(name = "ID", width = 25)
    private String id;

    @Excel(name = "项目ID", width = 20, type = Excel.Type.EXPORT, isExport = false)
    private String projectId;

    @Excel(name = "*区域1", width = 30, isNotNull = true)
    private String areaId1;

    @Excel(name = "*区域2", width = 30, isNotNull = true)
    private String areaId2;

    @Excel(name = "区域3", width = 30)
    private String areaId3;

    private String deptId;

    @Excel(name = "*部门名", width = 15, isNotNull = true)
    private String deptName;

    //@Excel(name = "商铺名", width = 20, type = Excel.Type.EXPORT, isExport = false)
    private String companyId;

    //@Excel(name = "编码", width = 20, type = Excel.Type.EXPORT, isExport = false)
    private String code;

    @Excel(name = "*名称", prompt = "30字以内", width = 30, isNotNull = true, maxLength = 50)
    private String name;

    @Excel(name = "*类型", width = 30, isNotNull = true)
    private String type;

    @Excel(name = "*属性", width = 20, isNotNull = true)
    private String attribute;

    @Excel(name = "*经营类型", width = 30, isNotNull = true)
    private String tradeModel;

    @Excel(name = "占地面积", width = 20, prompt = "0.00")
    private Float coverArea;

    @Excel(name = "消防面积", width = 20, prompt = "0.00")
    private Float fireArea;

    @Excel(name = "清洁面积", width = 20, prompt = "0.00")
    private Float cleanArea;

    @Excel(name = "租赁面积", width = 20, prompt = "0.00")
    private Float leaseArea;

    @Excel(name = "租赁实际面积", width = 20, prompt = "0.00")
    private Float leaseRealArea;

    @Excel(name = "租赁公摊面积", width = 20, prompt = "0.00")
    private Float leasePublicArea;

    @Excel(name = "车位面积", width = 20, prompt = "0.00")
    private Float parkingArea;

    //@Excel(name = "备注", prompt = "60字以内", width = 60,maxLength = 100)
    private String remark;

    @Excel(name = "构建编码", prompt = "30字以内", width = 30, maxLength = 50)
    private String ecode;

    @Excel(name = "创建人", type = Excel.Type.EXPORT, isExport = false)
    private String createBy;

    @Excel(name = "创建时间", type = Excel.Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss", width = 20, isExport = false)
    private LocalDateTime createTime;

    @Excel(name = "修改人", type = Excel.Type.EXPORT, isExport = false)
    private String updateBy;

    @Excel(name = "修改时间", type = Excel.Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss", width = 20, isExport = false)
    private LocalDateTime updateTime;

}
