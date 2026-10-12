package com.guhecloud.cube.srv.gen.identity.server.qrcode.enumeration;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.EntityTreeVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeEntityTypeVO;
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
public enum QrcodeEnum {

    /**
     * 元素类型（文本、图片、二维码）
     */
    textBox("文本"),
    imgBox("图片"),
    qrBox("二维码"),

    /**
     * 文本类型
     */
    FIX_TEXT("固定文本"),
    ENTITY_ATTRIBUTES("实体属性"),

    /**
     * 实体类型
     */
    DEVICE("设备"),
    STALL("档位"),
    PROPERTY("资产"),
    SITE_POINT("现场点"),
    RISK_SOURCE("风险源"),
    PATROL_POINT("巡更点"),

    /**
     * 实体公共属性
     */
    NAME("名称"),
    TYPE("类型"),
    AREA("所属区域"),

    /**
     * 现场点
     */
    MANAGE_DEPT("管理部门"),
    DERAIL_ADDRESS("具体地址"),
    CODE_NUM("现场点编号"),

    /**
     * 设备属性
     */
    USE_DEPT("使用部门"),
    MANAGER("管理人员"),
    CODE("设备编号"),

    /**
     * 档位属性
     */
    LOCATION("具体位置"),
    DEPT_NAMES("管理部门"),

    /**
     * 资产属性
     */
    UNIT("单位"),
    NUMBER("数量"),
    TYPE_NAME("资产类别"),
    START_TIME("入库日期"),
    RECEIVE_DEP_NAME("使用部门"),
    PROPERTY_CODE("资产编码"),
    FINANCE_CODE("财务编码"),
    /**
     * 风险源属性
     */
    MAINTENANCE_DEP_NAME("维护部门"),
    SERIAL_NUMBER("风险源编码"),
    ;

    /**
     * 描述信息
     */
    private final String description;

    QrcodeEnum(@NonNull String description) {
        this.description = description;
    }

    /**
     * 实体类型+属性的树形结构
     *
     * @return
     */
    public static List<EntityTreeVO> getEntityTree() {
        List<EntityTreeVO> list = new ArrayList<>();
        EntityTreeVO deviceEntity = EntityTreeVO.builder().code(DEVICE.name()).name(DEVICE.description).build();
        EntityTreeVO StallEntity = EntityTreeVO.builder().code(STALL.name()).name(STALL.description).build();
        EntityTreeVO property = EntityTreeVO.builder().code(PROPERTY.name()).name(PROPERTY.description).build();
        EntityTreeVO sitePoint = EntityTreeVO.builder().code(SITE_POINT.name()).name(SITE_POINT.description).build();
        EntityTreeVO riskSource = EntityTreeVO.builder().code(RISK_SOURCE.name()).name(RISK_SOURCE.description).build();
        list.add(getDeviceEntityAttributeList(deviceEntity));
        list.add(getStallEntityAttributeList(StallEntity));
        list.add(getPropertyEntityAttributeList(property));
        list.add(getSitPointEntityAttributeList(sitePoint));
        list.add(getRiskSourceEntityAttributeList(riskSource));
        return list;
    }

    private static EntityTreeVO getRiskSourceEntityAttributeList(EntityTreeVO riskSource) {
        List<EntityTreeVO> attributes = getPublicAttributes();
        riskSource.setAttributeVOS(attributes);
        return riskSource;
    }

    public static List<QrcodeEntityTypeVO.QrcodeEntityAttribute> getObjAttributeList(String objType) {
        List<QrcodeEntityTypeVO.QrcodeEntityAttribute> attributes = new ArrayList<>();
        attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(NAME.name()).name(NAME.description).build());
        attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(TYPE.name()).name(TYPE.description).build());
        attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(AREA.name()).name(AREA.description).build());
        if (DEVICE.name().equals(objType)) {
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(USE_DEPT.name()).name(USE_DEPT.description).build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(MANAGER.name()).name(MANAGER.description).build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(CODE.name()).name(CODE.description).build());
        } else if (STALL.name().equals(objType)) {
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(LOCATION.name()).name(LOCATION.description).build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(CODE.name()).name("档位编号").build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(DEPT_NAMES.name()).name(DEPT_NAMES.description).build());
        } else if (PROPERTY.name().equals(objType)) {
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(UNIT.name()).name(UNIT.description).build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(NUMBER.name()).name(NUMBER.description).build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(TYPE_NAME.name()).name(TYPE_NAME.description).build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(START_TIME.name()).name(START_TIME.description).build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(RECEIVE_DEP_NAME.name())
                                   .name(RECEIVE_DEP_NAME.description).build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(PROPERTY_CODE.name()).name(PROPERTY_CODE.description)
                                   .build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(FINANCE_CODE.name()).name(FINANCE_CODE.description)
                                   .build());
        } else if (SITE_POINT.name().equals(objType)) {
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(MANAGE_DEPT.name()).name(MANAGE_DEPT.description)
                                   .build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(DERAIL_ADDRESS.name()).name(DERAIL_ADDRESS.description)
                                   .build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(CODE_NUM.name()).name(CODE_NUM.description).build());
        } else if (RISK_SOURCE.name().equals(objType)) {
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(RECEIVE_DEP_NAME.name())
                                   .name(RECEIVE_DEP_NAME.description).build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(MAINTENANCE_DEP_NAME.name())
                                   .name(MAINTENANCE_DEP_NAME.description).build());
            attributes.add(QrcodeEntityTypeVO.QrcodeEntityAttribute.builder().code(SERIAL_NUMBER.name()).name(SERIAL_NUMBER.description)
                                   .build());

        }
        return attributes;
    }

    /**
     * 设备的属性列表
     *
     * @param deviceEntity
     *
     * @return
     */
    private static EntityTreeVO getDeviceEntityAttributeList(EntityTreeVO deviceEntity) {
        List<EntityTreeVO> attributes = getPublicAttributes();
        attributes.add(EntityTreeVO.builder().code(USE_DEPT.name()).name(USE_DEPT.description).build());
        attributes.add(EntityTreeVO.builder().code(MANAGER.name()).name(MANAGER.description).build());
        deviceEntity.setAttributeVOS(attributes);
        return deviceEntity;
    }

    /**
     * 档位的属性列表
     *
     * @param StallEntity
     *
     * @return
     */
    private static EntityTreeVO getStallEntityAttributeList(EntityTreeVO StallEntity) {
        List<EntityTreeVO> attributes = getPublicAttributes();
        attributes.add(EntityTreeVO.builder().code(LOCATION.name()).name(LOCATION.description).build());
        StallEntity.setAttributeVOS(attributes);
        return StallEntity;
    }

    /**
     * 公共的属性列表
     *
     * @return
     */
    private static List<EntityTreeVO> getPublicAttributes() {
        List<EntityTreeVO> attributes = new ArrayList<>();
        attributes.add(EntityTreeVO.builder().code(NAME.name()).name(NAME.description).build());
        attributes.add(EntityTreeVO.builder().code(TYPE.name()).name(TYPE.description).build());
        attributes.add(EntityTreeVO.builder().code(AREA.name()).name(AREA.description).build());
        return attributes;
    }

    /**
     * 资产的属性列表
     *
     * @param StallEntity
     *
     * @return
     */
    private static EntityTreeVO getPropertyEntityAttributeList(EntityTreeVO StallEntity) {
        List<EntityTreeVO> attributes = new ArrayList<>();
        attributes.add(EntityTreeVO.builder().code(NAME.name()).name(NAME.description).build());
        attributes.add(EntityTreeVO.builder().code(UNIT.name()).name(UNIT.description).build());
        attributes.add(EntityTreeVO.builder().code(NUMBER.name()).name(NUMBER.description).build());
        attributes.add(EntityTreeVO.builder().code(TYPE_NAME.name()).name(TYPE_NAME.description).build());
        attributes.add(EntityTreeVO.builder().code(START_TIME.name()).name(START_TIME.description).build());
        attributes.add(EntityTreeVO.builder().code(RECEIVE_DEP_NAME.name()).name(RECEIVE_DEP_NAME.description).build());
        attributes.add(EntityTreeVO.builder().code(PROPERTY_CODE.name()).name(PROPERTY_CODE.description).build());
        StallEntity.setAttributeVOS(attributes);
        return StallEntity;
    }

    private static EntityTreeVO getSitPointEntityAttributeList(EntityTreeVO sitePointEntity) {
        List<EntityTreeVO> attributes = getPublicAttributes();
        attributes.add(EntityTreeVO.builder().code(MANAGE_DEPT.name()).name(MANAGE_DEPT.description).build());
        sitePointEntity.setAttributeVOS(attributes);
        return sitePointEntity;
    }
}
