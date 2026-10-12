package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo;

import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.EducationEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.GenderEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.ProbationPeriodEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.StaffStatusEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.annotation.ExcelField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2023/02/10
 * @description 员工花名册导出
 */
@Data
public class StaffExcelVO {

    @ExcelField(value = "姓名", maxLength = 20, isNotNull = true)
    private String name;

    @ExcelField(value = "工号", maxLength = 30)
    private String jobNumber;

    @ExcelField(value = "联系电话", isNotNull = true)
    private String mobile;

    @ExcelField(value = "出生日期", dateFormat = "yyyy-MM-dd")
    private LocalDateTime dateOfBirth;

    @ExcelField(value = "性别", isNotNull = true, configKey = "gender")
    private String genderName;

    private String gender;

    @ExcelField(value = "学历", configKey = "education")
    private String educationName;

    private String education;

    @ExcelField(value = "职位", configKey = "postIds")
    private String postNames;

    private String positionIds;

    private List<Long> postIds;

    @ExcelField(value = "部门", isNotNull = true)
    private String organizationName;

    private Long organizationId;

    @ExcelField(value = "员工类型", configKey = "type")
    private String typeName;

    private String type;

    @ExcelField(value = "员工状态", configKey = "status")
    private String statusName;

    private String status;

    @ExcelField(value = "入职日期", dateFormat = "yyyy-MM-dd")
    private LocalDateTime inductionDate;

    @ExcelField(value = "试用期", configKey = "probationPeriod")
    private String probationPeriodName;

    private String probationPeriod;

    @ExcelField(value = "转正日期", dateFormat = "yyyy-MM-dd")
    private LocalDateTime regularWorkerDate;

    @ExcelField(value = "首次缴纳社保日期", dateFormat = "yyyy-MM")
    private LocalDateTime paySocialInsuranceFirstDate;

    @ExcelField(value = "紧急联系人", maxLength = 30)
    private String emergencyContact;

    @ExcelField(value = "紧急联系人电话", maxLength = 30)
    private String emergencyContactMobile;

    @ExcelField(value = "身份证号")
    private String idCard;

    @ExcelField(value = "区域", configKey = "areaIdList")
    private String areaNameList;

    private String areaIds;

    @ExcelField(value = "短号")
    private String cornet;

    public void setEnumName() {
        this.gender = GenderEnum.getNameByValues(this.genderName);
        this.education = EducationEnum.getNameByValues(this.educationName);
        this.status = StaffStatusEnum.getNameByValues(this.statusName);
        this.probationPeriod = ProbationPeriodEnum.getNameByValues(this.probationPeriodName);

    }

}