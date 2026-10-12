package com.guhecloud.cube.srv.gen.safety.personnel.server.common.interfaces;

import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CertExpireStatusEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CertStatusEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonStatusEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonTypeCodeEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.ResumeRecordTypeEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.vo.EnumOptionVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.vo.SafetyPersonnelEnumsVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 安全人员服务枚举元数据接口。
 */
@RestController("safetyPersonnelMetaController")
@RequestMapping
public class SafetyPersonnelMetaController {

    /**
     * 查询本服务对外使用的枚举字典。
     *
     * @return 枚举选项集合
     */
    @GetMapping("/meta/enums")
    public SafetyPersonnelEnumsVO enums() {
        SafetyPersonnelEnumsVO vo = new SafetyPersonnelEnumsVO();
        vo.setPersonTypeCodes(toOptions(PersonTypeCodeEnum.values()));
        vo.setPersonStatuses(toOptions(PersonStatusEnum.values()));
        vo.setCertStatuses(toOptions(CertStatusEnum.values()));
        vo.setCertExpireStatuses(toOptions(CertExpireStatusEnum.values()));
        vo.setResumeRecordTypes(toOptions(ResumeRecordTypeEnum.values()));
        return vo;
    }

    private static List<EnumOptionVO> toOptions(PersonTypeCodeEnum[] values) {
        return Arrays.stream(values)
                .map(item -> new EnumOptionVO(item.getCode(), item.getDescription()))
                .toList();
    }

    private static List<EnumOptionVO> toOptions(PersonStatusEnum[] values) {
        return Arrays.stream(values)
                .map(item -> new EnumOptionVO(item.getCode(), item.getDescription()))
                .toList();
    }

    private static List<EnumOptionVO> toOptions(CertStatusEnum[] values) {
        return Arrays.stream(values)
                .map(item -> new EnumOptionVO(item.getCode(), item.getDescription()))
                .toList();
    }

    private static List<EnumOptionVO> toOptions(CertExpireStatusEnum[] values) {
        return Arrays.stream(values)
                .map(item -> new EnumOptionVO(item.getCode(), item.getDescription()))
                .toList();
    }

    private static List<EnumOptionVO> toOptions(ResumeRecordTypeEnum[] values) {
        return Arrays.stream(values)
                .map(item -> new EnumOptionVO(item.getCode(), item.getDescription()))
                .toList();
    }
}
