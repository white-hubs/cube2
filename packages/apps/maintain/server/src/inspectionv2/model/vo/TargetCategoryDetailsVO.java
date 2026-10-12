package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-14  11:16
 * @Description:
 * @Version: 1.0
 */
@Data
public class TargetCategoryDetailsVO {
    /**
     * 名称
     */
    private String name;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 占比
     */
    private String proportion;
    private static List<String> statisticType = new ArrayList<>();

    static {
        statisticType.add("inspection");
        statisticType.add("abnormal");
        statisticType.add("task");
    }

    public static List<TargetCategoryDetailsVO> getTargetCategoryDetails(List<TargetCategoryDetailsVO> device,
                                                                         Map<String, Integer> number
                                                                        ) {
        if (device.size() < 1) {
            TargetCategoryDetailsVO details = new TargetCategoryDetailsVO();
            details.setProportion("0%");
            details.setName("inspection");
            details.setNum(0);
            device.add(details);
            TargetCategoryDetailsVO abnormal = new TargetCategoryDetailsVO();
            abnormal.setProportion("0%");
            abnormal.setName("abnormal");
            abnormal.setNum(0);
            device.add(abnormal);
            TargetCategoryDetailsVO task = new TargetCategoryDetailsVO();
            task.setProportion("0%");
            task.setName("task");
            task.setNum(0);
            device.add(task);
            return device;
        }
        for (String category : statisticType) {
            Boolean bool = true;
            for (TargetCategoryDetailsVO ex : device) {
                if (category.equals(ex.getName())) {
                    Integer toatl = number.get(category);
                    ex.setProportion(new BigDecimal(ex.getNum()).divide(new BigDecimal(toatl), 2, BigDecimal.ROUND_HALF_UP)
                                             .multiply(new BigDecimal(100)) + "%");
                    bool = false;
                    break;
                }
            }
            if (bool) {
                TargetCategoryDetailsVO details = new TargetCategoryDetailsVO();
                details.setName(category);
                details.setNum(0);
                details.setProportion("0%");
                device.add(details);
            }
        }
        return device;
    }
}
