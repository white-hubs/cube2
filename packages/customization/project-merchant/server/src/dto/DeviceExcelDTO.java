package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Device;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceExcelDTO extends ConvertionDTO<DeviceExcelDTO, Device> {

    public DeviceExcelDTO() {
    }

    public DeviceExcelDTO(Device device) {
        super();
        map(device);
    }

    public DeviceExcelDTO(List<Device> list) {
        super();
        map(list);
    }

    @Excel(name = "id", width = 20)
    private String id;

    //@Excel(name = "项目ID", width = 20, type = Excel.Type.EXPORT, isExport = false)
    private String projectId;

    @Excel(name = "*区域1", width = 30, isNotNull = true)
    private String areaId1;

    @Excel(name = "*区域2", width = 30, isNotNull = true)
    private String areaId2;

    @Excel(name = "区域3", width = 30)
    private String areaId3;

    @Excel(name = "*类型", width = 30, isNotNull = true)
    private String typeId;

    //@Excel(name = "设备型号", type = Excel.Type.EXPORT, isExport = false)
    private String model;

    @Excel(name = "物联网设备ID", width = 30, maxLength = 20)
    private String iotDeviceId;

    @Excel(name = "物联网设备型号", width = 30, maxLength = 50)
    private String iotDeviceType;

    //@Excel(name = "BIM模型ID", type = Excel.Type.EXPORT, isExport = false)
    private String bimModelId;

    //@Excel(name = "编码", width = 22, type = Excel.Type.EXPORT, isExport = false)
    private String code;

    @Excel(name = "*名称", prompt = "30字以内", width = 30, isNotNull = true, maxLength = 30)
    private String name;

    @Excel(name = "构建编码", width = 30)
    private String ecode;

    //@Excel(name = "拼音", type = Excel.Type.EXPORT, isExport = false)
    private String pinyin;

    //@Excel(name = "状态", type = Excel.Type.EXPORT, readConverterExp = "deviceStatus_normal=正常,deviceStatus_abnormal=异常", isExport = false)
    private String status;

    @Excel(name = "价格", prompt = "0.00")
    private Double price;

    @Excel(name = "安装位置", prompt = "50字以内", width = 50, maxLength = 50)
    private String installAddress;

    @Excel(name = "安装时间", prompt = "yyyy-MM-dd", dateFormat = "yyyy-MM-dd", width = 20)
    private LocalDateTime installDate;

    @Excel(name = "启用时间", prompt = "yyyy-MM-dd", dateFormat = "yyyy-MM-dd", width = 20)
    private LocalDateTime startDate;

    @Excel(name = "到期时间", prompt = "yyyy-MM-dd", dateFormat = "yyyy-MM-dd", width = 20)
    private LocalDateTime expireDate;

    @Excel(name = "生产时间", prompt = "yyyy-MM-dd", dateFormat = "yyyy-MM-dd", width = 20)
    private LocalDateTime productionDate;

    //@Excel(name = "上次维保时间", prompt = "yyyy-MM-dd HH:mm:ss", dateFormat = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime maintDate;

    @Excel(name = "生产厂家", maxLength = 50)
    private String manufacturer;

    @Excel(name = "登录账号", prompt = "摄像机设备（请填）", width = 20, maxLength = 20)
    private String account;

    @Excel(name = "登录密码", prompt = "摄像机设备（请填）", width = 20, maxLength = 30)
    private String password;

    @Excel(name = "IP地址", prompt = "摄像机设备（请填）", width = 20, maxLength = 30)
    private String ipAddr;

    @Excel(name = "端口", prompt = "摄像机设备（请填）", width = 20, maxLength = 16)
    private String port;

    @Excel(name = "Token", prompt = "摄像机设备（请填）", width = 20, maxLength = 50)
    private String token;

    @Excel(name = "规格", width = 30)
    private String specification;

    @Excel(name = "管理部门", prompt = "填写系统中已存在的部门名", width = 20)
    private String deptId;

    @Excel(name = "联系人员", prompt = "20字以内", width = 20, maxLength = 20)
    private String contactPerson;

    @Excel(name = "联系方式", prompt = "11位电话号码", maxLength = 11)
    private String contactPhone;

    @Excel(name = "联系地址", prompt = "50字以内", width = 50, maxLength = 50)
    private String contactAddress;

    @Excel(name = "财政编码", prompt = "50字以内", maxLength = 50)
    private String financeCode;

    @Excel(name = "备注", prompt = "100字以内", width = 50, maxLength = 100)
    private String remark;

/*    @Excel(name = "二维码code", type = Excel.Type.EXPORT, isExport = false)
    private String qrCode;*/

    //@Excel(name = "GIS经度", type = Excel.Type.EXPORT, isExport = false)
    private String gisLongitude;

    //@Excel(name = "GIS纬度", type = Excel.Type.EXPORT, isExport = false)
    private String gisLatitude;

    //@Excel(name = "视频分析盒子ID", width = 20, type = Excel.Type.EXPORT, isExport = false)
    private String videoboxId;

    //@Excel(name = "创建人", type = Excel.Type.EXPORT, isExport = false)
    private String createBy;

    //@Excel(name = "创建时间", type = Excel.Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss", width = 20, isExport = false)
    private LocalDateTime createTime;

    //@Excel(name = "修改人", type = Excel.Type.EXPORT, isExport = false)
    private String updateBy;

    //@Excel(name = "修改时间", type = Excel.Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss", width = 20, isExport = false)
    private LocalDateTime updateTime;

}
