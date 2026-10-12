package com.guhecloud.cube.iot.mgt.base.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class DictItemVo {
    private Long id;
    private Long dictId;
    private Long itemParentId;
    private Integer itemSort;
    private String itemLevel;
    private String itemLabel;
    private String itemValue;
    private Integer status;
    private String remark;

    private String createTimeFormat;

    private String updateTimeFormat;

    /**
     * 字典关联的文件对象
     */
    private List<FileVO> fileVOs;

    /**
     * 子节点
     */
    private List<DictItemVo> children;

    /**
     * 父节点
     */
    private DictItemVo parent;

    public boolean hasChildren(){
        return Objects.nonNull(children) && !children.isEmpty();
    }

    public static DictItemVo findSingleBottomItem(DictItemVo dictItemVo) {
        if (Objects.isNull(dictItemVo.getChildren()) || dictItemVo.getChildren().isEmpty()) {
            return dictItemVo;
        }

        return findSingleBottomItem(dictItemVo.getChildren().get(0));
    }

    public static void extractChildItem(DictItemVo dictItemVo, List<DictItemVo> dictItemVos) {
        if (Objects.isNull(dictItemVo.getChildren()) || dictItemVo.getChildren().isEmpty()) {
            return;
        }


        dictItemVos.addAll(dictItemVo.getChildren());

        if (Objects.nonNull(dictItemVo.getChildren()) && !dictItemVo.getChildren().isEmpty())
            dictItemVo.getChildren().forEach(item -> extractChildItem(item, dictItemVos));
    }

    public String toJsonStringWithRecursiveTree() {
        return "{" +
                "\"id\":" + id +
                ", \"dictId\":" + dictId +
                ", \"itemParentId\":" + itemParentId +
                ", \"itemSort\":" + itemSort +
                ", \"itemLevel\":\"" + itemLevel + '\"' +
                ", \"itemLabel\":\"" + itemLabel + '\"' +
                ", \"itemValue\":\"" + itemValue + '\"' +
                ", \"status\":" + status +
                ", \"remark\":\"" + remark + '\"' +
                ", \"children\":[" + (children == null ? null : children.stream().map(DictItemVo::toJsonStringWithRecursiveTree).reduce((a, b) -> a + "," + b).orElse(null)) + "]" +
                '}';
    }
}
