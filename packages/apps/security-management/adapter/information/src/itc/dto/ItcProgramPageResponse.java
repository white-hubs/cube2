package com.guhecloud.cube.srv.gen.adapter.information.server.itc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ItcProgramPageResponse {
    private String code;
    private String message;
    private DataObject data;

    @Data
    public static class DataObject {
        private List<ItcProgramPageDTO> list;
        private Integer total;
    }

    /**
     * "id": 2,//⾃增id
     * "group_id": 6,//分组id
     * "uid": "admin",//创建⼈uid
     * "type": 4,//11=H5制作，12=RTSP，3=播放列表，4=⽂章
     * "material_ids": "1",//素材id、逗号分隔
     * "title": "⼩开⼼的布局",//标题
     * "total_size": 0,//总⼤⼩ 单位：byte
     * "status": 1,//状态（1=正常，10=⼰删除）
     * "cover_path": "/itcsaas/upload/aa.jpg",//封⾯图
     * "description": "adafdsfsf",//H5编辑器json对象/播放列表⾥的素材
     * （每条按JSON排）/⽂章详情情内容/RTSP的备注
     * "url": "http://www.baidu.com/",//H5资源URL地址/RTSP链接地址
     * "is_shop": 0, //是否是购买的（1=是、0=否）
     * "shop_at": null, //购买时间
     * "sale_price": 0,//售价（货币单位：元）
     * "direction": 1, //屏⽅向：1=模屏，2=竖屏（H5）
     * "height": 0, //分辨率宽度（H5）
     * "width": 0, //分辨率⾼度（H5）
     * "is_share": 0, //是否已分享：1=已分享、0=未分享
     * "created_at": "2020-10-24 10:35:50",//创建时间
     * "updated_at": "2020-10-24 10:35:50"//更新时间
     */
    @Data
    public static class ItcProgramPageDTO {
        /**
         * 自增id
         */
        private Long id;
        /**
         * 分组id
         */
        @JsonProperty("group_id")
        private Integer groupId;
        /**
         * 创建⼈uid
         */
        private String uid;
        /**
         * 类型（11=H5制作，12=RTSP，3=播放列表，4=⽂章）
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
         * H5编辑器json对象/播放列表⾥的素材
         * （每条按JSON排）/⽂章详情情内容/RTSP的备注
         */
        private String description;
        /**
         * H5资源URL地址/RTSP链接地址
         */
        private String url;
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
         * 售价（货币单位：元）
         */
        @JsonProperty("sale_price")
        private Integer salePrice;
        /**
         * 屏⽅向：1=模屏，2=竖屏（H5）
         */
        private Integer direction;
        /**
         * 分辨率宽度（H5）
         */
        private Integer height;
        /**
         * 分辨率⾼度（H5）
         */
        private Integer width;
        /**
         * 是否已分享：1=已分享、0=未分享
         */
        @JsonProperty("is_share")
        private Integer isShare;
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
