package com.guhecloud.cube.srv.gen.energymanagement.server.util;

import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyTypeStatVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class EnergyStatUtil {
    public static EnergyTypeStatVO getStat(List<EnergyTypeStatVO> list) {
        EnergyTypeStatVO result = new EnergyTypeStatVO();
        BigDecimal ddValue = BigDecimal.ZERO;
        BigDecimal dcValue = BigDecimal.ZERO;
        BigDecimal zhValue = BigDecimal.ZERO;
        BigDecimal fValue = BigDecimal.ZERO;
        BigDecimal pValue = BigDecimal.ZERO;
        BigDecimal gValue = BigDecimal.ZERO;
        BigDecimal jValue = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(list)) {
            for (EnergyTypeStatVO energyTypeStatVO : list) {
                // 用量
                ddValue = ddValue.add(Optional.ofNullable(energyTypeStatVO.getDdValue()).orElse(BigDecimal.ZERO));
                // 碳排  (碳排总量 = 电碳排 + 燃气碳排 +燃油碳排+集中供热碳排+集中供冷碳排-可再生能源碳排+其他碳排)
                if (energyTypeStatVO.getDcValue() != null) {
                    if (InstrumentTypeEnum.RENEWABLE_ENERGY.getCode().equals(energyTypeStatVO.getInstrumentType())) {
                        // 碳排是减去可再生能源
                        dcValue = dcValue.subtract(energyTypeStatVO.getDcValue());
                    } else if (
                            InstrumentTypeEnum.ELECTRICITY_METER.getCode().equals(energyTypeStatVO.getInstrumentType())
                            || InstrumentTypeEnum.GAS_METER.getCode().equals(energyTypeStatVO.getInstrumentType())
                            || InstrumentTypeEnum.FUEL_METER.getCode().equals(energyTypeStatVO.getInstrumentType())
                            || InstrumentTypeEnum.HEAT_METER.getCode().equals(energyTypeStatVO.getInstrumentType())
                            || InstrumentTypeEnum.COOL_METER.getCode().equals(energyTypeStatVO.getInstrumentType())
                            || InstrumentTypeEnum.OTHER.getCode().equals(energyTypeStatVO.getInstrumentType())
                    ) {
                        dcValue = dcValue.add(energyTypeStatVO.getDcValue());
                    }
                }
                zhValue = zhValue.add(Optional.ofNullable(energyTypeStatVO.getZhValue()).orElse(BigDecimal.ZERO));
                fValue = fValue.add(Optional.ofNullable(energyTypeStatVO.getFValue()).orElse(BigDecimal.ZERO));
                pValue = pValue.add(Optional.ofNullable(energyTypeStatVO.getPValue()).orElse(BigDecimal.ZERO));
                gValue = gValue.add(Optional.ofNullable(energyTypeStatVO.getGValue()).orElse(BigDecimal.ZERO));
                jValue = jValue.add(Optional.ofNullable(energyTypeStatVO.getJValue()).orElse(BigDecimal.ZERO));
            }
            EnergyTypeStatVO defaultVO = list.getFirst();
            if (defaultVO != null) {
                result.setStatDate(defaultVO.getStatDate());
                result.setInstrumentType(defaultVO.getInstrumentType());
                result.setInstrumentTypeName(defaultVO.getInstrumentTypeName());
            }
        }
        result.setDdValue(ddValue);
        result.setDcValue(dcValue);
        result.setZhValue(zhValue);
        result.setFValue(fValue);
        result.setPValue(pValue);
        result.setGValue(gValue);
        result.setJValue(jValue);
        return result;
    }
}
