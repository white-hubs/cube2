package com.guhecloud.platform.job.xxl.operator;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.platform.job.xxl.constant.XxlJobUrlConstant;
import com.guhecloud.platform.job.xxl.dto.XxlGroupInfo;
import com.guhecloud.platform.job.xxl.dto.XxlJobInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 调用xxl接口操作类
 *
 * @author lanchun
 * @since 2020/9/7 15:52
 */
@Slf4j
@Component("platformXxlJobApiOperator")
@ConditionalOnProperty(prefix = "job.impl.xxl-job.admin", name = "addresses")//有xxl.job配置时该组件生效
public class XxlJobApiOperator {
    private static String appName;
    private static String addresses;
    private static String userName;
    private static String passWord;
    private static RestTemplate restTemplate;

    @Value("${job.impl.xxl-job.executor.appname}")
    public void setAppName(String appName) {
        XxlJobApiOperator.appName = appName;
    }

    @Value("${job.impl.xxl-job.admin.addresses}")
    public void setAddresses(String addresses) {
        XxlJobApiOperator.addresses = addresses;
    }

    @Value("${job.impl.xxl-job.username}")
    public void setUserName(String userName) {
        XxlJobApiOperator.userName = userName;
    }

    @Value("${job.impl.xxl-job.password}")
    public void setPassWord(String passWord) {
        XxlJobApiOperator.passWord = passWord;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        XxlJobApiOperator.restTemplate = restTemplate;
    }

    /**
     * 新增任务
     *
     * @param xxlJobInfo 任务信息
     *
     * @return 是否成功
     */
    public static String addJob(XxlJobInfo xxlJobInfo) {
        var groupId = getGroupId();
        xxlJobInfo.setJobGroup(groupId);
        var resp = doPost(addresses + XxlJobUrlConstant.ADD_URL, xxlJobInfo.toForm(), OperateResp.class);
        return resp.getContent();
    }

    /**
     * 修改定时任务
     *
     * @param xxlJobInfo 任务信息
     *
     * @return 是否成功
     */
    public static boolean updateJob(XxlJobInfo xxlJobInfo) {
        //获取执行器集合
        var groupId = getGroupId();
        xxlJobInfo.setJobGroup(groupId);
        var resp = doPost(addresses + XxlJobUrlConstant.UPDATE_URL, xxlJobInfo.toForm(), OperateResp.class);
        return checkOperateResp(resp, "修改失败");
    }

    /**
     * 启动任务
     *
     * @param id 任务id
     *
     * @return 是否成功
     */
    public static boolean startJob(String id) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("id", id);
        var resp = doPost(addresses + XxlJobUrlConstant.START_URL, params, OperateResp.class);
        return checkOperateResp(resp, "启动失败");
    }

    /**
     * 暂停任务
     *
     * @param id 任务id
     *
     * @return 是否成功
     */
    public static boolean stopJob(String id) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("id", id);
        var resp = doPost(addresses + XxlJobUrlConstant.STOP_URL, params, OperateResp.class);
        return checkOperateResp(resp, "暂停失败");
    }

    /**
     * 手动触发任务
     *
     * @param id    任务id
     * @param param 参数
     *
     * @return 是否成功
     */
    public static boolean triggerJob(String id, String param) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("id", id);
        params.add("executorParam", param);
        params.add("addressList", null);
        var resp = doPost(addresses + XxlJobUrlConstant.TRIGGER_URL, params, OperateResp.class);
        return checkOperateResp(resp, "触发失败");
    }

    /**
     * 删除任务
     *
     * @param id 任务id
     *
     * @return 是否成功
     */
    public static boolean removeJob(String id) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("id", id);
        var resp = doPost(addresses + XxlJobUrlConstant.REMOVE_URL, params, OperateResp.class);
        return checkOperateResp(resp, "删除失败");
    }

    /**
     * 获取任务信息
     *
     * @param id 任务id
     *
     * @return 任务对象
     */
    public static XxlJobInfo getJobInfo(String id) {
        var groupId = getGroupId(appName);
        var array = getJobList(groupId, null, 0, 1000);
        if (array.getData() != null && !array.getData().isEmpty()) {
            return array.getData().stream().filter(x -> String.valueOf(x.getId()).equals(id)).findFirst().orElse(null);
        }
        return null;
    }

    /**
     * 获取任务所有列表
     *
     * @return 任务集合
     */
    public static List<XxlJobInfo> getJobList() {
        List<XxlJobInfo> result = new ArrayList<>();
        var groupId = getGroupId(appName);
        var start = 0;
        var length = 100;
        long total;
        do {
            XxlJobInfoArrayResp array = getJobList(groupId, "", start, length);
            result.addAll(array.getData());
            total = array.recordsTotal;
            start++;
        } while (length * length < total);

        return result;
    }

    public static List<XxlJobInfo> getJobList(String name) {
        List<XxlJobInfo> result = new ArrayList<>();
        var groupId = getGroupId(appName);
        var start = 0;
        var length = 100;
        long total;
        do {
            XxlJobInfoArrayResp array = getJobList(groupId, name, start, length);
            result.addAll(array.getData());
            total = array.recordsTotal;
            start++;
        } while (length * length < total);

        return result;
    }

    /**
     * 获取执行器id
     *
     * @return 执行器id
     */
    public static Integer getGroupId() {
        return getGroupId(appName);
    }

    /**
     * 获取执行器id
     *
     * @param appName 执行器名
     *
     * @return 执行器id
     */
    public static Integer getGroupId(String appName) {

        return getGroupInfo(appName).getId();
    }

    /**
     * 获取执行器信息
     *
     * @param appName 执行器名
     *
     * @return 执行器信息
     */
    public static XxlGroupInfo getGroupInfo(String appName) {

        var jobGroupArray = getGroupList(appName, 0, 1);
        if (jobGroupArray.getData() != null && !jobGroupArray.getData().isEmpty()) {
            return jobGroupArray.getData().getFirst();
        }
        return new XxlGroupInfo();
    }

    /**
     * 获取执行器集合
     *
     * @return 执行器集合
     */
    public static List<XxlGroupInfo> getGroupList() {
        List<XxlGroupInfo> result = new ArrayList<>();
        long total;
        var start = 0;
        var length = 1000;
        do {
            XxlJobGroupArrayResp jobGroupArray = getGroupList("", start, length);
            result.addAll(jobGroupArray.data);
            total = jobGroupArray.getRecordsTotal();
            start++;
        } while ((long) start * length < total);

        return result;
    }

    private static boolean checkOperateResp(OperateResp resp, String msg) {
        if (resp == null || 200 != resp.getCode()) {
            log.error("{}:{}", msg, resp);
            return false;
        }
        return true;
    }

    private static XxlJobInfoArrayResp getJobList(Integer groupId, String jobName, Integer start, Integer length) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("jobGroup", groupId);
        params.add("triggerStatus", -1);
        params.add("jobDesc", "");
        params.add("executorHandler", jobName);
        params.add("author", "");
        params.add("start", start);
        params.add("length", length);
        return doPost(addresses + XxlJobUrlConstant.JOB_URL, params, XxlJobInfoArrayResp.class);
    }

    private static XxlJobGroupArrayResp getGroupList(String appName, Integer start, Integer length) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("appname", appName);
        params.add("title", null);
        params.add("start", start);
        params.add("length", length);
        return doPost(addresses + XxlJobUrlConstant.JOB_GROUP_URL, params, XxlJobGroupArrayResp.class);
    }

    private static String doPost(String url, MultiValueMap<String, Object> map) {
        return doPost(url, map, String.class);
    }

    private static <T> T doPost(String url, MultiValueMap<String, Object> map, Class<T> clazz) {
        //获取cookie
        var cookie = xxlLogin();
        List<String> cookies = new ArrayList<>();
        cookies.add(cookie);
        var headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var request = new HttpEntity<>(map, headers);
        var stringResponseEntity = restTemplate.postForEntity(url, request, String.class);
        return JSON.parseObject(stringResponseEntity.getBody(), clazz);
    }

    /**
     * 登陆xxl获取cookie
     *
     * @return cookie
     */
    private static String xxlLogin() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("userName", userName);
        map.add("password", passWord);
        map.add("ifRemember", "on");
        var request = new HttpEntity<>(map, headers);
        var response = restTemplate.postForEntity(
                addresses + XxlJobUrlConstant.LOGIN_URL, request, String.class);
        return Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).getFirst();
    }

    @Data
    static class OperateResp {
        private Integer code;
        private String content;
        private String msg;
    }

    @Data
    static class XxlJobGroupArrayResp {
        private Long recordsFiltered;
        private Long recordsTotal;
        private List<XxlGroupInfo> data;
    }

    @Data
    static class XxlJobInfoArrayResp {
        private Long recordsFiltered;
        private Long recordsTotal;
        private List<XxlJobInfo> data;
    }
}
