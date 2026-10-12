package com.guhecloud.cube.deliver.project.merchant.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.mapper.CompanyMapper;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyDTO;
import com.guhecloud.cube.deliver.project.merchant.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyUploadFileUtils {

    public static final String REDIS_KEY_TEMPLATE_PROGRESS = "iops:company:batchUploadFile:progress:{0}";
    public static final String REDIS_KEY_TEMPLATE_ERRORS = "iops:company:batchUploadFile:errors:{0}";

    public final RedisTemplate<String, Object> redisTemplate;
    private final FileClient fileClient;
    private final CompanyMapper companyMapper;
    private final FileRefClient fileRefClient;

    @Async
    public void batchUpload(CompanyDTO dto, String uuid) {

        List<String> companyIds = dto.getCompanyIds();
        if (companyIds == null || companyIds.size() < 1) {
            redisTemplate.opsForValue().set(this.getProgressKey(uuid), 100, 1, TimeUnit.SECONDS);
            return;
        }
        for (String companyId : companyIds) {
            Company company = companyMapper.selectById(companyId);
            company.setCompanyPhotoFileId(
                    ObjectUtil.isNotEmpty(dto.getCompanyPhotoFileIds()) ? StrUtil.join(",", dto.getCompanyPhotoFileIds()) : null);
            company.setSecurityFileId(ObjectUtil.isNotEmpty(dto.getSecurityFileIds()) ? StrUtil.join(",", dto.getSecurityFileIds()) : "");
            company.setTrainingPhotoFileId(
                    ObjectUtil.isNotEmpty(dto.getTrainingPhotoFileIds()) ? StrUtil.join(",", dto.getTrainingPhotoFileIds()) : null);
            companyMapper.updateById(company);
        }

    }

    /*@Async
    public void batchUpload(CompanyDTO dto, String uuid) {

        Map<String, String> companyIdErrorMap = new HashMap<>();
        try {
            int total = 11;
            for (int i = 0; i < total; i++) {
                companyIdErrorMap.put("" + (i + 1), "发生异常");
                int v = (int) (((double) (i + 1) / total) * 100);
                if (v == 100){
                    redisTemplate.opsForValue().set(this.getErrorsKey(uuid), companyIdErrorMap, 60, TimeUnit.SECONDS);
                }
                redisTemplate.opsForValue().set(this.getProgressKey(uuid), v, 60, TimeUnit.SECONDS);
                System.out.println(v);
                Thread.sleep(1000);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public int getProgressValue(String uuid) {
        Object obj = redisTemplate.opsForValue().get(this.getProgressKey(uuid));
        return obj == null ? 0 : (int) obj;
    }

    public Map<String, String> getErrorsValue(String uuid) {
        Object obj = redisTemplate.opsForValue().get(this.getErrorsKey(uuid));
        return obj == null ? null : (Map<String, String>) obj;
    }

    public void deleteByUuid(String uuid) {
        redisTemplate.delete(this.getProgressKey(uuid));
        redisTemplate.delete(this.getErrorsKey(uuid));
    }

    public String getProgressKey(String uuid) {
        return MessageFormat.format(REDIS_KEY_TEMPLATE_PROGRESS, uuid);
    }

    public String getErrorsKey(String uuid) {
        return MessageFormat.format(REDIS_KEY_TEMPLATE_ERRORS, uuid);
    }

}
