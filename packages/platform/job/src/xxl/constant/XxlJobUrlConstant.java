package com.guhecloud.platform.job.xxl.constant;

/**
 * xxl-job常用api接口地址常量类
 *
 * @author lanchun
 * @since 2020/9/7 16:24
 */
public class XxlJobUrlConstant {

    public static final String LOGIN_URL = "/login";

    public static final String ADD_URL = "/jobinfo/add";//添加job接口地址

    public static final String UPDATE_URL = "/jobinfo/update";//修改job接口地址

    public static final String REMOVE_URL = "/jobinfo/remove";//删除job接口地址

    public static final String START_URL = "/jobinfo/start";//启动job接口地址

    public static final String STOP_URL = "/jobinfo/stop"; //停止job接口地址

    public static final String TRIGGER_URL = "/jobinfo/trigger";//手动触发接口地址

    public static final String JOB_GROUP_URL = "/jobgroup/pageList";//执行器列表

    public static final String JOB_GROUP_ADD_URL = "/jobgroup/save";//添加执行器

    public static final String JOB_GROUP_DEL_URL = "/jobgroup/remove";//删除执行器

    public static final String JOB_URL = "/jobinfo/pageList";// 获取job分页列表
}
