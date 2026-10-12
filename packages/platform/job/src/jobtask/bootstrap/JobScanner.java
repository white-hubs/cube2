package com.guhecloud.platform.job.jobtask.bootstrap;

import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.job.jobtask.dto.JobAddInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 扫描使用JobTaskAnnotation注解的job
 */
@Slf4j
@Component
public class JobScanner {
    private final ApplicationContext applicationContext;
    private final List<JobAddInfo> jobAddInfos = Collections.synchronizedList(new ArrayList<>());
    private volatile boolean scanned = false;

    public JobScanner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 扫描所有@JobTaskAnnotation方法
     */
    public synchronized List<JobAddInfo> scan() {
        if (scanned) {
            return jobAddInfos;
        }
        log.info("job start scanner...");
        var beanNames = applicationContext.getBeanDefinitionNames();
        for (var name : beanNames) {
            var bean = applicationContext.getBean(name);
            var clazz = AopProxyUtils.ultimateTargetClass(bean);
            if (clazz == Object.class) {
                log.error("bean not found during scheduled job scanner：{}", name);
                continue;
            }
            for (var method : clazz.getDeclaredMethods()) {
                var ann = AnnotationUtils.findAnnotation(method, JobAnnotation.class);
                if (ann != null) {
                    var jobAddInfo = new JobAddInfo();
                    jobAddInfo.setName(ann.name());
                    jobAddInfo.setDesc(ann.desc());
                    jobAddInfo.setCron(ann.cron());
                    jobAddInfo.setEnabled(ann.enabled());
                    jobAddInfo.setBean(bean);
                    jobAddInfo.setParam(ann.param());
                    jobAddInfo.setMethod(method);
                    jobAddInfos.add(jobAddInfo);
                }
            }
        }
        scanned = true;
        log.info("scanner completed,the number of tasks to be added is {}", jobAddInfos.size());
        return jobAddInfos;
    }
}
