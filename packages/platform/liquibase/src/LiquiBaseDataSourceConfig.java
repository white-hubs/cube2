package com.guhecloud.platform.liquibase;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LiquiBaseDataSourceConfig {
    /**
     * 瀚高数据库
     */
    private static String HG = "hg";
    @Value(value = "${mybatis-plus.global-config.db-config.db-type:mysql}")
    private String dbType;
    @Autowired
    private DataSourceProperties dataSourceProperties;
    @Value(value = "${liqui-base.execute:true}")
    private Boolean execute;

    public List<SpringLiquibase> getSpringLiquibase(Environment environment, String datasourceName) throws LiquibaseException, IOException {
        log.info("liquibase-读取配置文件开始：{}", datasourceName);
        Map<String, DataSourceProperty> datasourceMap = this.dataSourceProperties.getDatasource();
        log.info("datasourceMap:{}", JSON.toJSONString(datasourceMap));
        DriverManagerDataSource data = new DriverManagerDataSource();
        if (Objects.nonNull(datasourceMap) && Objects.nonNull(datasourceMap.get(datasourceName))) {
            log.info("liquibase-serveone配置读取...");
            data.setDriverClassName("com.mysql.cj.jdbc.Driver");
            data.setUrl(datasourceMap.get(datasourceName).getUrl());
            data.setUsername(datasourceMap.get(datasourceName).getUsername());
            data.setPassword(datasourceMap.get(datasourceName).getPassword());
        } else {
            log.info("liquibase-本地服务配置读取...");
            data.setDriverClassName(Objects.requireNonNull(environment.getProperty("spring.datasource.driver-class-name")));
            data.setUrl(environment.getProperty("spring.datasource.url"));
            data.setUsername(environment.getProperty("spring.datasource.username"));
            data.setPassword(environment.getProperty("spring.datasource.password"));
        }
        return getLiquiBaseList(datasourceName, data);
    }

    private List<SpringLiquibase> getLiquiBaseList(String datasourceName,
                                                   DriverManagerDataSource data
                                                  ) throws LiquibaseException, IOException {
        if (ObjectUtils.isNotEmpty(dbType) && HG.equals(dbType)) {
            datasourceName = datasourceName + "/hg";
        }
        List<SpringLiquibase> list = new ArrayList<>();
        if (execute) {
            // 读取指定文件的sql文件名称
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:db/" + datasourceName + "/*.sql");
            final List<Resource> collect = Arrays.stream(resources).sorted((o1, o2) -> {
                try {
                    return sortFile(o1.getFilename(), o2.getFilename());
                } catch (Exception e) {
                    log.error("获取文件更新时间异常:", e);
                }
                return 0;
            }).collect(Collectors.toList());
            for (Resource resource : collect) {
                String filename = resource.getFilename();
                log.info("liquibase-读取增量sql文件：{},url:{},username:{},password:{}",
                         filename, data.getUrl(), data.getUsername(), data.getPassword());
                if (isNeedAutoExecute(filename, data)) {
                    SpringLiquibase liquibase = new SpringLiquibase();
                    liquibase.setDataSource(data);
                    liquibase.setChangeLog("classpath:db/" + datasourceName + "/" + filename);
                    liquibase.setClearCheckSums(false);
                    liquibase.afterPropertiesSet();
                    list.add(liquibase);
                }
            }
        }
        return list;
    }

    /**
     * 处理执行的sql文件顺序
     *
     * @param file1 增量sql文件名
     * @param file2 增量sql文件名
     *
     * @return 排序结果
     */
    private int sortFile(String file1, String file2) {
        String pattern = "\\d+";
        Pattern regex = Pattern.compile(pattern);
        List<Integer> t1 = new ArrayList<>();
        List<Integer> t2 = new ArrayList<>();
        Matcher matcher1 = regex.matcher(file1);
        while (matcher1.find()) {
            t1.add(Integer.parseInt(matcher1.group()));
        }
        Matcher matcher2 = regex.matcher(file2);
        while (matcher2.find()) {
            t2.add(Integer.parseInt(matcher2.group()));
        }
        int i = Math.min(t1.size(), t2.size());
        int result = 0;
        for (int j = 0; j < i; j++) {
            result = t1.get(j) - t2.get(j);
            if (result != 0) {
                break;
            }
        }
        if (result == 0) {
            result = t1.size() - t2.size();
        }
        return result;
    }

    /**
     * @Author wql
     * @Description 判断增量sql是否需要自动执行
     * 已经执行过一次的增量sql文件，就算发生了变更也不会再重新执行。
     * dev环境的增量sql文件都不需要自动执行
     * @Date 14:09 2023/7/24
     **/
    private boolean isNeedAutoExecute(String filename, DriverManagerDataSource data) {
        String url = Objects.requireNonNull(data.getUrl());
        //排除dev环境的增量sql自动执行
        if ("changelog-master.xml".equals(filename) || url.contains("192.168.0.221")) {
            return false;
        }
        List<String> list = queryExecutedRecord(data, url);
        for (String file : list) {
            log.info("liquibase-filePath:{},fileName:{}", file, filename);
            if (StringUtils.isNotBlank(file) && file.endsWith(filename)) {
                return false;
            }
        }
        return true;
    }

    private List<String> queryExecutedRecord(DriverManagerDataSource data, String url) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<String> list = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url, data.getUsername(), data.getPassword());
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select FILENAME from databasechangelog");
            while (resultSet.next()) {
                list.add(resultSet.getString("FILENAME"));
            }
        } catch (SQLException e) {
            log.error("增量sql执行记录查询失败:", e);
        } finally {
            // 关闭连接和Statement对象
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("关闭连接和Statement对象异常:", e);
            }
        }
        return list;
    }
}
