package com.guhecloud.cube.srv.gen.energymanagement.server.enumeration;

import com.guhecloud.cube.srv.gen.energymanagement.server.vo.InstrumentTypeEnumVO;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 仪表类型枚举
 *
 * @author liwei
 * @date 2022/07/05
 */
@Getter
public enum InstrumentTypeEnum {
    WATER_METER("water_meter", "水"),
    ELECTRICITY_METER("electricity_meter", "电"),
    GAS_METER("gas_meter", "气"),
    FUEL_METER("fuel_meter", "燃油"),
    HEAT_METER("heat_meter", "集中供热"),
    COOL_METER("cool_meter", "集中供冷"),
    RENEWABLE_ENERGY("renewable_energy", "可再生能源"),
    OTHER("other", "其他"),
    VAPOUR("vapour", "蒸汽"),
    ENERGY_METER("energy_meter", "能量");

    private final String code;
    private final String name;

    InstrumentTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getFireinspEnum(String code) {
        if (StringUtils.isEmpty(code)) {
            return "";
        }
        for (InstrumentTypeEnum fireinspEnum : InstrumentTypeEnum.values()) {
            if (fireinspEnum.getCode().equals(code)) {
                return fireinspEnum.getName();
            }
        }
        return "";
    }

    public static List<String> getEnumTypeName() {
        ArrayList<String> enumTypeNameArr = new ArrayList<>();
        for (InstrumentTypeEnum fireinspEnum : InstrumentTypeEnum.values()) {
            String enumName = fireinspEnum.getName();
            enumTypeNameArr.add(enumName);
        }
        return enumTypeNameArr;
    }

    public static List<InstrumentTypeEnumVO> getEnumTypeVo() {
        ArrayList<InstrumentTypeEnumVO> enumTypeArr = new ArrayList<>();
        for (InstrumentTypeEnum fireinspEnum : InstrumentTypeEnum.values()) {
            enumTypeArr.add(new InstrumentTypeEnumVO(fireinspEnum.getCode(), fireinspEnum.getName()));
        }
        return enumTypeArr;
    }

}
