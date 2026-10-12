package com.guhecloud.cube.biz.ouaa.server.enumeration;

import com.guhecloud.cube.biz.ouaa.server.vo.DataPermissionVO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 二维码
 *
 * @author bq
 * @date 2022/07/27
 */
@Getter
public enum DataPermissionEnum {
    SELF("本账号数据可见", 1),
    DEPT("本部门数据可见", 2),
    CUSTOM_DEPT("自定义查看部门", 3),
    ALL("全部可见", 4),
    ;

    /**
     * 描述信息
     */
    private final String description;
    private final int sortNo;

    DataPermissionEnum(@NonNull String description, @NotNull int sortNo) {
        this.description = description;
        this.sortNo = sortNo;
    }

    public static List<DataPermissionVO> getList() {
        List<DataPermissionVO> descList = new ArrayList<>();
        for (DataPermissionEnum permission : DataPermissionEnum.values()) {
            descList.add(DataPermissionVO.builder()
                                 .code(permission.name())
                                 .name(permission.getDescription())
                                 .build());
        }
        return descList;
    }
}
