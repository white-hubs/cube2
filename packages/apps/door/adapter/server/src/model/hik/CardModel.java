package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;
import lombok.Getter;

/**
 * 门禁卡
 */
@Data
public class CardModel {
    private String cardId;//用于存放第三方的ID
    private String cardNo;
    private String personId;
    private String personName;
    private String useStatus;//使用状态 1 正常 2 挂失
    private String startDate;//生效日期
    private String endDate;//失效日期
    private String lossDate;//挂失开始日期
    private String unlossDate;//解除挂失日期
    private String description;//卡描述
    private String createTime;//创建时间
    private String updateTime;//更新时间
    private String cardType;//卡类型 卡片类型,1：IC卡；2：CPU卡；3：远距离卡；4：MIFARE卡，默认1-IC卡
    private String cardTypeName;
    private String orgId;//组织ID
    private Boolean isBindFinger;//是否绑定指纹
    private Boolean isBindFace;//是否绑定人脸

    public String getCardTypeName() {
        return CardTypeEnum.getDisplayNameByValue(cardType);
    }

    @Getter
    public enum CardTypeEnum {
        IC_CARD("1", "IC卡"),
        CPU_CARD("2", "CPU卡"),
        DISTANT_CARD("3", "远距离卡"),
        MIFARE_CARD("4", "MIFARE卡");

        private final String value;

        private final String displayName;

        CardTypeEnum(String value, String displayName) {
            this.value = value;
            this.displayName = displayName;
        }

        public static String getDisplayNameByValue(String value) {
            for (CardModel.CardTypeEnum type : CardModel.CardTypeEnum.values()) {
                if (StringUtils.isNotEmpty(value) && type.getValue().equals(value)) {
                    return type.getDisplayName();
                }
            }
            return null; // 如果没有匹配的枚举项，则返回 null 或其他适当的默认值
        }
    }

    @Getter
    public enum UseStatusEnum {
        NORMAL("1"),
        LOSS("2");

        private final String value;

        UseStatusEnum(String value) {
            this.value = value;
        }

        public static String getNameByValue(String value) {
            for (CardModel.UseStatusEnum status : CardModel.UseStatusEnum.values()) {
                if (StringUtils.isNotEmpty(value) && status.getValue().equals(value)) {
                    return status.name();
                }
            }
            return null; // 如果没有匹配的枚举项，则返回 null 或其他适当的默认值
        }
    }

}
