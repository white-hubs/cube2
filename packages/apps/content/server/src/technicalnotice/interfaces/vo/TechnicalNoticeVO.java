package com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.interfaces.vo;

import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.interfaces.vo.NoticeConfigVO;
import lombok.Data;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/20
 */
@Data
public class TechnicalNoticeVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 通报标题
     */
    private String title;

    /**
     * 通报说明
     */
    private String illustrate;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     *
     */
    private List<NoticeConfigVO> noticeConfigs;

}
