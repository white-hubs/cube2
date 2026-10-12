package com.guhecloud.cube.srv.gen.third.connection.server.ncdx.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.guhecloud.cube.srv.gen.third.connection.server.ncdx.dto.NcdxEducationalDTO;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NcdxThirdServiceHttpUtils {

    public static NcdxEducationalDTO getNcdxEducational(String url) {
        HttpRequest httpRequest = HttpUtil.createGet(url);
        try (HttpResponse response = httpRequest.execute()) {
            if (response.isOk()) {
                String body = response.body();
                if (StringUtils.isNotBlank(body)) {
                    NcdxResponse ncdxResponse = JsonUtils.deserialize(body, NcdxResponse.class);
                    if (ncdxResponse != null && Boolean.TRUE.equals(ncdxResponse.success)) {
                        NcdxEducationalDTO dto = JsonUtils.deserialize(ncdxResponse.getData(), NcdxEducationalDTO.class);
                        return dto == null ? new NcdxEducationalDTO() : dto;
                    }
                    log.error("NcdxThirdHttpService 返回错误:{}", ncdxResponse);
                }
            }
            log.error("NcdxThirdHttpService 请求失败");
        } catch (Exception e) {
            log.error("NcdxThirdHttpService getNcdxEducational：", e);
            return new NcdxEducationalDTO();
        }

        return new NcdxEducationalDTO();
    }

    @Data
    static class NcdxResponse {
        private NcdxEducationalDTO data;
        private Boolean success;
        private Integer state;
        private String message;
    }

    public static NcdxEducationalDTO getNcdxEducationalTest(String url) {

        String body = "{\"data\":{\"classInfo\":[{\"pxsj_sdate\":\"2025-04-24 00:00:00\",\"bclx_text\":\"市委党校\",\"num\":28,\"teas_jwgl_bjgl_id\":\"041e8961d0e44b07a907794ce0938a7e\",\"name\":\"0410测试班级\",\"pxsj_edate\":\"2025-04-30 23:59:59\",\"bclx_value\":\"1\",\"pxsj\":\"2025-04-24 00:04至2025-04-30 23:04\",\"school_year\":\"2025\"},{\"pxsj_sdate\":\"2025-04-24 00:00:00\",\"bclx_text\":\"市委党校(行政学院)\",\"num\":26,\"teas_jwgl_bjgl_id\":\"041e8961d0e44b07a907794ce0938a7e\",\"name\":\"0410测试班级\",\"pxsj_edate\":\"2025-04-30 23:59:59\",\"bclx_value\":\"1\",\"pxsj\":\"2025-04-24 00:04至2025-04-30 23:04\",\"school_year\":\"2024\"},{\"pxsj_sdate\":\"2025-04-24 00:00:00\",\"bclx_text\":\"市委党校(行政学院)\",\"num\":22,\"teas_jwgl_bjgl_id\":\"041e8961d0e44b07a907794ce0938a7a\",\"name\":\"班级二\",\"pxsj_edate\":\"2025-04-30 23:59:59\",\"bclx_value\":\"1\",\"pxsj\":\"2025-04-24 00:04至2025-04-30 23:04\",\"school_year\":\"2025\"},{\"pxsj_sdate\":\"2025-04-24 00:00:00\",\"bclx_text\":\"市委党校(行政学院)\",\"num\":28,\"teas_jwgl_bjgl_id\":\"041e8961d0e44b07a907794ce0938a7e\",\"name\":\"0410测试班级\",\"pxsj_edate\":\"2025-04-30 23:59:59\",\"bclx_value\":\"1\",\"pxsj\":\"2025-04-24 00:04至2025-04-30 23:04\",\"school_year\":\"2024\"}],\"jzg_num\":193,\"wlx\":0,\"wjx\":0,\"swdx_num\":19,\"lx\":2,\"wtbb_xy_num\":1,\"jx\":0,\"wtbb_num\":2,\"lj_xy_num\":16,\"bj_num\":21,\"shzyxy_xy_num\":0,\"shzyxy_num\":0,\"year_xy_num\":1,\"swdx_xy_num\":15,\"zx_xy_num\":16,\"jg_num\":56},\"success\":true,\"state\":20000,\"message\":\"成功\"}";
        if (StringUtils.isNotBlank(body)) {
            NcdxResponse ncdxResponse = JsonUtils.deserialize(body, NcdxResponse.class);
            if (ncdxResponse != null && Boolean.TRUE.equals(ncdxResponse.success)) {
                NcdxEducationalDTO dto = JsonUtils.deserialize(ncdxResponse.getData(), NcdxEducationalDTO.class);
                return dto == null ? new NcdxEducationalDTO() : dto;
            }
            log.error("NcdxThirdHttpService 返回错误:{}", ncdxResponse);
        }

        return new NcdxEducationalDTO();
    }

    public static void main(String[] args) {
        getNcdxEducationalTest("ss");
    }
}
