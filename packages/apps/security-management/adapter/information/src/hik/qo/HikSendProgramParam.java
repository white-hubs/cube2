package com.guhecloud.cube.srv.gen.adapter.information.server.hik.qo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HikSendProgramParam {
    /**
     * 终端字段类型：idType,codeType。
     * 指定terminalUniqueFieldValues的值所属字段。
     * 值为"idType"表示terminalUniqueFieldValues为终端id字段的数组
     * 值为"codeType"表示terminalUniqueFields为终端业务场所编码值的数组
     */
    private String terminalUniqueFieldType;
    /**
     * 终端字段值的数组。字符串数组。terminalUniqueFieldType和terminalUniqueFieldValues为一组  和orgIndexCodes有且只能有一个
     */
    private List<String> terminalUniqueFieldValues;
    /**
     * 终端所属组织编码数组，会插播组织下所有终端，字符串数组
     */
    private List<String> orgIndexCodes;
    /**
     * 插播间隔时间，以秒为单位。执行插播下发指令后会间隔设置的时间后才会执行下一条指令。小于或等于5默认为即使插播；大于5排队插播。不填默认为0
     */
    private Integer intervalTime;
    /**
     * 插播内容对象
     */
    private HikInsertInfoDetailParam insertInfoDetail;

}
