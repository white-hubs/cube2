package com.guhecloud.cube.srv.gen.adapter.information.server.itc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ItcMaterialResponse {
    private String code;
    private String message;
    private ItcProgramDTO data;

    /**
     * "id": 2,//⾃增id
     * "group_id": 6,//分组id
     * "group": "默认分组", //分组名称
     * "uid": "admin",//创建⼈uid
     * "type": 4,//1=H5制作，2=RTSP，3=播放列表，4=⽂章
     * "material_ids": "1",//素材id、逗号分隔
     * "title": "⼩开⼼的布局",//标题
     * "total_size": 0,//总⼤⼩ 单位：byte
     * "status": 1,//状态（1=正常，10=⼰删除）
     * "cover_path": "/itcsaas/upload/aa.jpg",//封⾯图
     * "description": "adafdsfsf",//H5编辑器json对象/播放列表⾥的素材（每条
     * 按JSON排）/⽂章详情情内容/RTSP的备注
     * "html_content": "adafdsfsf",//HTML内容
     * "is_shop": 0, //是否是购买的（1=是、0=否）
     * "shop_at": null, //购买时间
     * "sale_price": 0, //售价（元）
     * "url": "http://www.baidu.com/",//H5资源URL地址/RTSP链接地址
     * "list_play_type": 2, //播放列表播放类型（2=⾃动，3=⼿动）
     * "created_at": "2020-10-24 10:35:50",//创建时间
     * "updated_at": "2020-10-24 10:35:50"//更新时间
     */
    @Data
    public static class ItcProgramDTO {
        /**
         * 自增id
         */
        private Integer id;
        /**
         * 分组id
         */
        @JsonProperty("group_id")
        private Integer groupId;
        /**
         * 分组名称
         */
        private String group;
        /**
         * 创建⼈uid
         */
        private String uid;
        /**
         * 1=H5制作，2=RTSP，3=播放列表，4=⽂章
         */
        private Integer type;
        /**
         * 素材id、逗号分隔
         */
        @JsonProperty("material_ids")
        private String materialIds;
        /**
         * 标题
         */
        private String title;
        /**
         * 总⼤⼩ 单位：byte
         */
        @JsonProperty("total_size")
        private Integer totalSize;
        /**
         * 状态（1=正常，10=⼰删除）
         */
        private Integer status;
        /**
         * 封⾯图
         */
        @JsonProperty("cover_path")
        private String coverPath;
        /**
         * H5编辑器json对象/播放列表⾥的素材（每条按JSON排）/⽂章详情情内容/RTSP的备注
         */
        private String description;
        /**
         * HTML内容
         */
        private String htmlContent;
        /**
         * 是否是购买的（1=是、0=否）
         */
        @JsonProperty("is_shop")
        private Integer isShop;
        /**
         * 购买时间
         */
        @JsonProperty("shop_at")
        private String shopAt;
        /**
         * 售价（元）
         */
        @JsonProperty("sale_price")
        private Integer salePrice;
        /**
         * H5资源URL地址/RTSP链接地址
         */
        private String url;
        /**
         * 播放列表播放类型（2=⾃动，3=⼿动）
         */
        @JsonProperty("list_play_type")
        private Integer listPlayType;
        /**
         * 创建时间
         */
        @JsonProperty("created_at")
        private String createdAt;
        /**
         * 更新时间
         */
        @JsonProperty("updated_at")
        private String updatedAt;
    }
}
