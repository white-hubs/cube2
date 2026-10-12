package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CompanyExcelDTO extends ConvertionDTO<CompanyExcelDTO, Company> {

    public CompanyExcelDTO() {
    }

    public CompanyExcelDTO(Company company) {
        super();
        map(company);
    }

    public CompanyExcelDTO(List<Company> list) {
        super();
        map(list);
    }

    @Excel(name = "ID", width = 20, prompt = "新增时不填，修改时填入对应ID")
    private String id;

    @Excel(name = "项目ID", width = 20, type = Excel.Type.EXPORT, isExport = false)
    private String projectId;

    @Excel(name = "*商铺名称", prompt = "40字以内", width = 40, isNotNull = true, maxLength = 40)
    private String name;

    @Excel(name = "编码", width = 22, type = Excel.Type.EXPORT, isExport = false)
    private String code;

    //    @Excel(name = "*所属人姓名", prompt = "25字以内", width = 25, isNotNull = true, maxLength = 25)
    private String owner;

    //    @Mobile
    //    @Excel(name = "所属人电话", prompt = "11电话", width = 11,maxLength = 11)
    private String mobile;

    //    @Excel(name = "*关联的空间名称", prompt = "多个时用英文“,”分割。最多200个字符", width = 50,isNotNull = true)
    private String spaceNames;

    //    @Excel(name = "商户经营信息简介", prompt = "980字以内", width = 80, maxLength = 980)
    private String introduction;

    //    @Excel(name = "备注", prompt = "100字以内", width = 50, maxLength = 100)
    private String remark;

    //@Excel(name = "创建人", type = Excel.Type.EXPORT, isExport = false)
    private String createBy;

    //@Excel(name = "创建时间", type = Excel.Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss", width = 20, isExport = false)
    private LocalDateTime createTime;

    //@Excel(name = "修改人", type = Excel.Type.EXPORT, isExport = false)
    private String updateBy;

    //Excel(name = "修改时间", type = Excel.Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss", width = 20, isExport = false)
    private LocalDateTime updateTime;

}
