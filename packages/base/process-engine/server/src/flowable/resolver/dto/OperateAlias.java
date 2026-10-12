package com.guhecloud.cube.base.flow.flowable.resolver.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/7 10:05
 * @Description:
 */
@Data
public class OperateAlias {
    /**
     * 操作编码
     */
    private String code;
    /**
     * 操作名称
     */
    private String name;
    /**
     * 操作别名
     */
    private String alias;
    /**
     * 操作权限
     */
    private String perm;

    //再次发起
    public static OperateAlias getReStartAlias() {
        OperateAlias repeat = new OperateAlias();
        repeat.setCode("REPEAT");
        repeat.setName("再次发起");
        repeat.setAlias("");
        repeat.setPerm("S");
        return repeat;
    }

    //撤销
    public static OperateAlias getUndoAlias() {
        OperateAlias undo = new OperateAlias();
        undo.setCode("UNDO");
        undo.setName("撤销");
        undo.setAlias("");
        undo.setPerm("S");
        return undo;
    }

    //发起人存在
    public static List<String> getStartAlias() {
        return Arrays.asList("REPEAT", "UNDO");
    }

    public static List<OperateAlias> getActiveOperateAlias() {
        List<OperateAlias> operateAlias = new ArrayList<>();
        OperateAlias alias = new OperateAlias();
        alias.setCode("PASS");
        alias.setName("同意");
        alias.setAlias("");
        alias.setPerm("S");
        operateAlias.add(alias);
        alias = new OperateAlias();
        alias.setCode("BACK");
        alias.setName("退回");
        alias.setAlias("");
        alias.setPerm("S");
        operateAlias.add(alias);
        alias = new OperateAlias();
        alias.setCode("TRANSFER");
        alias.setName("转交");
        alias.setAlias("");
        alias.setPerm("S");
        operateAlias.add(alias);
        alias = new OperateAlias();
        alias.setCode("REJECT");
        alias.setName("驳回");
        alias.setAlias("");
        alias.setPerm("S");
        operateAlias.add(alias);
        alias = new OperateAlias();
        alias.setCode("WITHDRAW");
        alias.setName("撤回");
        alias.setAlias("");
        alias.setPerm("S");
        operateAlias.add(alias);
        alias = new OperateAlias();
        alias.setCode("COMMENT");
        alias.setName("评论");
        alias.setAlias("");
        alias.setPerm("H");
        operateAlias.add(alias);
        return operateAlias;
    }

    public static OperateAlias getWithdrawAlias() {
        OperateAlias withdraw = new OperateAlias();
        withdraw.setCode("WITHDRAW");
        withdraw.setName("撤回");
        withdraw.setAlias("");
        withdraw.setPerm("S");
        return withdraw;
    }
}
