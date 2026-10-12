package com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration;

import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.AnnouncementLevelEnumVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum AnnouncementLevelEnum {
    Announcement_Level1("Announcement_Level1", "一级"),
    Announcement_Level2("Announcement_Level2", "二级"),
    Announcement_Level3("Announcement_Level3", "三级");

    private final String code;
    private final String name;

    AnnouncementLevelEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getFireinspEnum(String code) {
        for (AnnouncementLevelEnum fireinspEnum : AnnouncementLevelEnum.values()) {
            if (fireinspEnum.getCode().equals(code)) {
                return fireinspEnum.getName();
            }
        }
        return "";
    }

    public static List<String> getEnumTypeName() {
        ArrayList<String> enumTypeNameArr = new ArrayList<>();
        for (AnnouncementLevelEnum fireinspEnum : AnnouncementLevelEnum.values()) {
            String enumName = fireinspEnum.getName();
            enumTypeNameArr.add(enumName);
        }
        return enumTypeNameArr;
    }

    public static List<AnnouncementLevelEnumVo> getEnumTypeVo() {
        ArrayList<AnnouncementLevelEnumVo> enumTypeArr = new ArrayList<>();

        for (AnnouncementLevelEnum fireinspEnum : AnnouncementLevelEnum.values()) {
            enumTypeArr.add(new AnnouncementLevelEnumVo(fireinspEnum.getName(), fireinspEnum.getCode()));
        }
        return enumTypeArr;
    }
}
