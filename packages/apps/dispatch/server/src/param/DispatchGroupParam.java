package com.guhecloud.cube.biz.dispatch.server.param;

import com.guhecloud.platform.base.util.BizCodeUtils;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.List;

/**
 * @author Appet
 * @Description 调度设备-分组
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchGroupParam {

    //父级节点
    private String parentId;
    //组编号
    private String code = BizCodeUtils.generate();
    //组名称
    private String name = "临时组(WEB)：" + getCode().split("_")[1];
    //组类型
    private Integer type = 1;
    //有效期
    private Integer validTime = 1;
    //组员
    private List<String> members;
    //是否录音
    private Boolean isRecord = true;
    //是否录像
    private Boolean isVideo = false;
    //组级别
    private Integer groupLevel;
    //排序号
    private Integer sort;
    //对讲空闲超时
    private Integer intercomIdleTimeOut;
    //话权方总超时
    private Integer intercomPowerTimeCount;
    //排队人数限制
    private Integer queueMaxNum;
    //对讲状态通知时间间隔
    private Integer intercomStateSpace;

    public String getCode() {
        if (StringUtils.isEmpty(code)) {
            code = BizCodeUtils.generate();
        }
        return code;
    }

    public String getName() {
        return StringUtils.isEmpty(name) ? "临时组(WEB)：" + getCode().split("_")[1] : name;
    }

    public Integer getType() {
        return type == null ? 1 : type;//默认临时组
    }

    public Integer getValidTime() {
        return validTime == null ? 1 : validTime;//默认有效期1天
    }
}