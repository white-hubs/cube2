package com.guhecloud.cube.servone.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.alibaba.nacos.api.config.listener.AbstractSharedListener;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MultiserverNacosConfigurer implements ApplicationRunner, ApplicationListener<RefreshEvent> {

    private static final String BOOTSTRAP_YML_PATH = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/bootstrap.yaml";

    private final ApplicationContext applicationContext;
    private final NacosServiceRegistry serviceRegistry;
    private final NacosDiscoveryProperties discoveryProperties;

    private Resource[] getApplicationResources() {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            return resourcePatternResolver.getResources(BOOTSTRAP_YML_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getApplicationServiceName(Resource... resources) {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(resources);
        Properties properties = Objects.requireNonNull(yaml.getObject());
        return properties.getProperty(ApplicationContextHolder.APPLICATION_NAME_PROPERTY);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.registerServices();
    }

    @Override
    public void onApplicationEvent(RefreshEvent event) {
        String desc = event.getEventDesc();
        log.warn("RefreshEvent received, description: " + desc);
        if (event.getSource() != null) {
            Class<?> clazz = event.getSource().getClass().getSuperclass();
            if (clazz != null && AbstractSharedListener.class.isAssignableFrom(clazz)) {
                this.registerServices();
            }
        }
    }

    private void registerServices() {
        for (Resource resource : this.getApplicationResources()) {
            String service = this.getApplicationServiceName(resource);
            this.discoveryProperties.setService(service);
            NacosRegistration registration = new NacosRegistration(null, this.discoveryProperties, this.applicationContext);
            this.serviceRegistry.register(registration);
        }
    }

}
