package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/10
 */
@Data
public class SpotCheckResourceVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商户主键
     */
    private Long supplierId;
    /**
     * 商户名称
     */
    private String supplierName;

    /**
     * 商户编码
     */
    private String supplierCode;

    /**
     * 来货报备号
     */
    private String porkReportCode;

    /**
     * 档位主键
     */
    private Long stallId;

    /**
     * 档位号
     */
    private String stallName;

    /**
     * 养殖场
     */
    private String originalPlace;

    /**
     * 屠宰场名称
     */
    private String abattoirName;
    /**
     * 到达时间
     */
    private LocalDateTime arrivedTime;

    /**
     * 报备时间
     */
    private LocalDateTime porkReportTime;

    /**
     * 报备商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 报备商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 来货报备审核员
     */
    private String porkReportAuditorName;

    /**
     * 审核时间
     */
    private LocalDateTime porkReportAuditTime;

    /**
     * 车牌号
     */
    private String vehiclePlate;

    /**
     * 检疫票号
     */
    private List<String> pratiqueCodes;

    /**
     * 登记员
     */
    private String registerName;
    /**
     * 登记时间
     */
    private LocalDateTime registerTime;

    /**
     * 瘦肉精
     */
    private SpotCheckEnum clenbuterol;

    /**
     * 含水量
     */
    private SpotCheckEnum watered;

    /**
     * 抽检员名称
     */
    private String inspectorName;

    /**
     * 抽检时间
     */
    private LocalDateTime spotCheckTime;

    /**
     * 理货数据主键
     */
    private Long porkArrangeId;

    /**
     * 理货数据审核员
     */
    private String porkArrangeAuditorName;
    /**
     * 理货数据审核时间
     */
    private LocalDateTime porkArrangeAuditTime;

    /**
     * 理货数据审核状态
     */
    private PorkArrangeAuditEnum porkArrangeAuditStatus;

    /**
     * 理货数据详情主键
     */
    private Long porkArrangeDetailId;

    /**
     * 电子标签
     */
    private String label;

    /**
     * 脚标
     */
    private String footMarkCode;

    /**
     * 订单主键
     */
    private Long orderId;

    /**
     * 买家名称
     */
    private String customerName;

    /**
     * 订单时间
     */
    private LocalDateTime orderTime;

    /**
     * 检疫证明
     */
    private List<FileVO> fileVOS;

}
