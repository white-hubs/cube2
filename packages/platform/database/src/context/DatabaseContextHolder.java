package com.guhecloud.platform.database.context;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.AESUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.annotation.Confidential;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.config.DatabaseSecurityProperties;
import com.guhecloud.platform.database.entity.Creatable;
import com.guhecloud.platform.database.entity.Deletable;
import com.guhecloud.platform.database.entity.Entity;
import com.guhecloud.platform.database.entity.Isolatable;
import com.guhecloud.platform.database.entity.TreeEntity;
import com.guhecloud.platform.database.entity.Updatable;
import com.guhecloud.platform.database.entity.enhanced.EnhancedCreatable;
import com.guhecloud.platform.database.entity.enhanced.EnhancedUpdatable;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.database.mapper.BatchSoftDeleteMapper;
import com.guhecloud.platform.database.util.EntityUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据库操作相关上下文工具类
 *
 * @author Woody
 * @date 2021/7/5
 */
@Slf4j
@Configuration
@Order(1)
public class DatabaseContextHolder implements ApplicationRunner {

    /**
     * 类对象/业务操作实例映射表
     */
    private static final Map<Class<?>, IService<Object>> CLASS_SERVICE_MAPPING = new ConcurrentHashMap<>();

    /**
     * 事务处理器
     */
    private static Transactor TRANSACTOR = null;

    /**
     * 全局SqlSessionTemplate实例
     */
    private static SqlSessionTemplate SESSION_TEMPLATE = null;

    /**
     * 数据实体对象列表
     */
    private static List<Class<?>> ENTITY_CLASSES = null;

    /**
     * 可删除的数据实体表名称集合
     */
    private static Set<String> DELETABLE_TABLE_NAMES = null;

    /**
     * 可隔离的数据实体表名称集合
     */
    private static Set<String> ISOLATABLE_TABLE_NAMES = null;

    private static Set<String> SUPPORT_DATA_PERMISSION_NAMES = null;
    /**
     * 数据安全配置属性
     */
    private static DatabaseSecurityProperties.Codec CODEC = null;

    /**
     * 对象机密数据字段映射表
     */
    private static Map<Class<?>, List<Field>> CONFIDENTIAL_CLASS_FIELD_MAPPING = null;

    /**
     * 获取事务处理器
     *
     * @return 事务处理器
     */
    public static Transactor getTransactor() {
        return Objects.requireNonNull(TRANSACTOR, "Transactor has not been initialized");
    }

    /**
     * 获取SqlSessionTemplate
     *
     * @return SqlSessionTemplate实例
     */
    public static SqlSessionTemplate getSessionTemplate() {
        return Objects.requireNonNull(SESSION_TEMPLATE, "Session template has not been initialized");
    }

    /**
     * 获取数据编解码配置
     *
     * @return 数据编解码配置
     */
    public static DatabaseSecurityProperties.Codec getCodec() {
        return Objects.requireNonNull(CODEC, "Database codec has not been initialized");
    }

    /**
     * 获取原生的数据库连接
     *
     * @return 数据库连接对象
     */
    public static Connection getConnection() {
        return getSessionTemplate().getConnection();
    }

    /**
     * 执行原生SQL
     *
     * @param sql SQL语句
     *
     * @return true/false
     *
     * @throws Exception 执行异常
     */
    public static boolean executeNative(String sql) throws Exception {
        try (Statement statement = getConnection().createStatement()) {
            return statement.execute(sql);
        }
    }

    /**
     * 事务执行方法
     *
     * @param executor 回调方法
     *
     * @return 执行结果
     *
     * @throws Exception 执行异常
     */
    public static Object transactional(Executor executor) throws Exception {
        return getTransactor().execute(executor);
    }

    /**
     * 事务执行方法
     *
     * @param executors 回调方法数组
     *
     * @throws Exception 执行异常
     */
    public static void transactional(Executor[] executors) throws Exception {
        if (executors != null && executors.length > 0) {
            transactional(Arrays.asList(executors));
        }
    }

    /**
     * 事务执行方法
     *
     * @param executors 回调方法集合
     *
     * @throws Exception 执行异常
     */
    public static void transactional(Collection<Executor> executors) throws Exception {
        getTransactor().execute(executors);
    }

    /**
     * 清理数据表名称
     *
     * @param table 数据表名称
     *
     * @return 清理后的数据表名称
     */
    public static String cleanTableName(String table) {
        // 去掉数据表名称前后的"`"号
        if (StringUtils.isNotEmpty(table) && table.charAt(0) == '`' && table.charAt(table.length() - 1) == '`') {
            return table.substring(1, table.length() - 1);
        }
        return table;
    }

    /**
     * 获取对象的机密字段列表
     *
     * @param clazz 对象类型
     *
     * @return 字段列表
     */
    private static List<Field> getConfidentialFields(Class<?> clazz) {
        Map<Class<?>, List<Field>> fields = Objects.requireNonNull(CONFIDENTIAL_CLASS_FIELD_MAPPING,
                                                                   "Confidential fields has not been initialized");
        return fields.get(clazz);
    }

    /**
     * 获取指定对象对应的业务操作实例
     *
     * @param clazz 数据实体对象
     * @param <T>   数据对象类型
     *
     * @return 业务操作实例
     */
    @SuppressWarnings("unchecked")
    public static <T> IService<T> getService(Class<T> clazz) {
        return (IService<T>) CLASS_SERVICE_MAPPING.computeIfAbsent(clazz, c -> {
            ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
            IService<Object> service = applicationContext.getBeansOfType(IService.class).values().stream()
                    .filter(s -> Objects.equals(s.getEntityClass(), clazz)).findAny().orElse(null);
            return Objects.requireNonNull(service, "Service not found for class: " + clazz.getName());
        });
    }

    /**
     * 获取数据模型对象列表
     *
     * @return 数据模型对象列表
     */
    public static List<Class<?>> getEntityClasses() {
        List<Class<?>> classes = Objects.requireNonNull(ENTITY_CLASSES, "Entity classes has not been initialized");
        return Collections.unmodifiableList(classes);
    }

    public static List<TableInfo> lookupEntityTablesForDataPermission() {
        return getEntityClasses().stream().filter(clazz -> clazz.isAnnotationPresent(DataPermission.class))
                .map(TableInfoHelper::getTableInfo)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 查找指定类型的数据表信息，延迟判断是为了保障TableInfoHelper.getTableInfo()方法能够获取到表信息
     *
     * @param type 数据实体对象
     *
     * @return 数据表信息列表
     */
    public static List<TableInfo> lookupEntityTables(@NonNull Class<?> type) {
        return getEntityClasses().stream().filter(type::isAssignableFrom).map(TableInfoHelper::getTableInfo)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 判断数据实体表是否支持删除标记
     *
     * @param table 数据实体表名称
     *
     * @return true/fasle
     */
    public static boolean isDeletableTable(String table) {
        Set<String> tables = Objects.requireNonNull(DELETABLE_TABLE_NAMES, "Deletable tables has not been initialized");
        return !StringUtils.isEmpty(table)
               && (tables.contains(table = table.toLowerCase()) || tables.contains(cleanTableName(table)));
    }

    /**
     * 判断数据实体表是否支持隔离标记
     *
     * @param table 数据实体表名称
     *
     * @return true/fasle
     */
    public static boolean isIsolatableTable(String table) {
        Set<String> tables = Objects.requireNonNull(ISOLATABLE_TABLE_NAMES,
                                                    "Isolatable tables has not been initialized");
        return !StringUtils.isEmpty(table)
               && (tables.contains(table = table.toLowerCase()) || tables.contains(cleanTableName(table)));
    }

    public static boolean isDataPermissionTable(String table) {
        Set<String> tables = Objects.requireNonNull(SUPPORT_DATA_PERMISSION_NAMES,
                                                    "DataPermission tables has not been initialized");
        return !StringUtils.isEmpty(table)
               && (tables.contains(table = table.toLowerCase()) || tables.contains(cleanTableName(table)));
    }

    /**
     * 初始化树形实体信息
     *
     * @param entity    数据实体
     * @param operation 操作类型
     */
    public static void initializeTreeProperties(Object entity, @NonNull SqlCommandType operation) {
        if (!(entity instanceof TreeEntity) || operation != SqlCommandType.INSERT) {
            return;
        }
        // 标记层级字段，用于参数占位
        ((TreeEntity<?>) entity).setLevel(1);
        // 标记序列号字段，用于参数占位
        ((TreeEntity<?>) entity).setSequence(com.guhecloud.platform.base.util.StringUtils.EMPTY_STRING);
    }

    /**
     * 初始化创建信息
     *
     * @param entity    数据实体
     * @param operation 操作类型
     */
    public static void initializeCreatableProperties(Object entity, @NonNull SqlCommandType operation) {
        if (!(entity instanceof Creatable<?> creatable) || operation != SqlCommandType.INSERT) {
            return;
        }
        String operator = SecurityContextHolder.getPrincipalProperty(Principal::getAccount);
        if (!StringUtils.isEmpty(operator)) {
            creatable.setCreateBy(operator);
        } else {
            creatable.setCreateBy(ActorTypeEnum.SYSTEM.name());
        }
        if (Objects.isNull(creatable.getCreateTime())) {
            creatable.setCreateTime(LocalDateTime.now());
        }
    }

    /**
     * 初始化扩展的创建信息
     *
     * @param entity    数据实体
     * @param operation 操作类型
     */
    public static void initializeEnhancedCreatableProperties(Object entity, @NonNull SqlCommandType operation) {
        if (!(entity instanceof EnhancedCreatable<?> creatable) || operation != SqlCommandType.INSERT) {
            return;
        }
        var id = SecurityContextHolder.getPrincipalProperty(Principal::getId);
        var type = SecurityContextHolder.getPrincipalProperty(Principal::getType);
        if (Objects.nonNull(id)) {
            creatable.setCreatorId(String.valueOf(id));
            creatable.setCreatorType(ActorTypeEnum.safeValueOf(type.name()));
        } else {
            creatable.setCreatorId(EntityAuditor.INNER_CREATOR_ID);
            creatable.setCreatorType(ActorTypeEnum.SYSTEM);
        }
    }

    /**
     * 初始化更新信息
     *
     * @param entity    数据实体
     * @param operation 操作类型
     */
    public static void initializeUpdatableProperties(Object entity, @NonNull SqlCommandType operation) {
        if (!(entity instanceof Updatable<?> updatable)
            || (operation != SqlCommandType.INSERT && operation != SqlCommandType.UPDATE)) {
            return;
        }
        String operator = SecurityContextHolder.getPrincipalProperty(Principal::getAccount);
        if (!StringUtils.isEmpty(operator)) {
            updatable.setUpdateBy(operator);
        } else {
            updatable.setUpdateBy(ActorTypeEnum.SYSTEM.name());
        }
        updatable.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 初始化扩展的更新信息
     *
     * @param entity    数据实体
     * @param operation 操作类型
     */
    public static void initializeEnhancedUpdatableProperties(Object entity, @NonNull SqlCommandType operation) {
        if (!(entity instanceof EnhancedUpdatable<?> updatable)
            || (operation != SqlCommandType.INSERT && operation != SqlCommandType.UPDATE)) {
            return;
        }
        var id = SecurityContextHolder.getPrincipalProperty(Principal::getId);
        var type = SecurityContextHolder.getPrincipalProperty(Principal::getType);
        if (Objects.nonNull(id)) {
            updatable.setUpdaterId(String.valueOf(id));
            updatable.setUpdaterType(ActorTypeEnum.safeValueOf(type.name()));
        } else {
            updatable.setUpdaterId(EntityAuditor.INNER_CREATOR_ID);
            updatable.setUpdaterType(ActorTypeEnum.SYSTEM);
        }
    }

    /**
     * 初始化租户信息
     *
     * @param entity    数据实体
     * @param operation 操作类型
     */
    public static void initializeIsolatableProperties(Object entity, @NonNull SqlCommandType operation) {
        if (!(entity instanceof Isolatable<?> isolatable)
            || (operation != SqlCommandType.INSERT && operation != SqlCommandType.UPDATE)) {
            return;
        }
        Integer tenant = SessionContextHolder.getRequesterProperty(Requester::getTenant);
        if (isolatable.getTenant() == null) {
            // 默认设置为当前用户的租户编号
            isolatable.setTenant(tenant);
        } else if (isolatable.getTenant() < 0 || !Objects.equals(isolatable.getTenant(), tenant)) {
            throw new IllegalStateException("Invalid tenant");
        }
    }

    /**
     * 初始化删除标记
     *
     * @param entity    数据实体
     * @param operation 操作类型
     */
    public static void initializeDeletableProperties(Object entity, @NonNull SqlCommandType operation) {
        if (!(entity instanceof Deletable<?> deletable) || operation != SqlCommandType.INSERT) {
            return;
        }
        if (deletable.getDeleted() == null) {
            deletable.setDeleted(false);
        }
    }

    /**
     * 编码数据
     *
     * @param original 原始数据
     *
     * @return 数据密文
     */
    public static String encode(String original) {
        DatabaseSecurityProperties.Codec codec = getCodec();
        return AESUtils.encode(original, codec.getSecret(), codec.getIv());
    }

    /**
     * 解密数据
     *
     * @param ciphertext 数据密文
     *
     * @return 数据明文
     */
    public static String decode(String ciphertext) {
        DatabaseSecurityProperties.Codec codec = getCodec();
        return AESUtils.decode(ciphertext, codec.getSecret(), codec.getIv());
    }

    /**
     * 编码敏感数据属性
     *
     * @param entity 数据实体
     * @param <T>    数据类型
     */
    public static <T> void encodeConfidentialProperties(T entity) {
        updateConfidentialProperties(entity, DatabaseContextHolder::encode);
    }

    /**
     * 编码敏感数据属性
     *
     * @param entities 数据实体集合
     * @param <T>      数据类型
     */
    public static <T> void encodeConfidentialProperties(Collection<T> entities) {
        updateConfidentialProperties(entities, DatabaseContextHolder::encode);
    }

    /**
     * 解码敏感数据属性
     *
     * @param entity 数据实体
     * @param <T>    数据类型
     */
    public static <T> void decodeConfidentialProperties(T entity) {
        updateConfidentialProperties(entity, DatabaseContextHolder::decode);
    }

    /**
     * 解码敏感数据属性
     *
     * @param entities 数据实体集合
     * @param <T>      数据类型
     */
    public static <T> void decodeConfidentialProperties(Collection<T> entities) {
        updateConfidentialProperties(entities, DatabaseContextHolder::decode);
    }

    /**
     * 更新敏感属性数据
     *
     * @param entities 数据实体集合
     * @param codec    敏感数据编解码回调方法
     * @param <T>      数据类型
     */
    public static <T> void updateConfidentialProperties(Collection<T> entities,
                                                        @NonNull Function<String, String> codec
                                                       ) {
        if (CollectionUtils.isEmpty(entities) || entities.iterator().next() == null) {
            return;
        }
        Class<?> clazz = entities.iterator().next().getClass();
        List<Field> fields = getConfidentialFields(clazz);
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        entities.forEach(entity -> updateConfidentialProperties(entity, fields, codec));
    }

    /**
     * 更新敏感属性数据
     *
     * @param entity 数据实体
     * @param codec  敏感数据编解码回调方法
     * @param <T>    数据类型
     */
    public static <T> void updateConfidentialProperties(T entity, @NonNull Function<String, String> codec) {
        List<Field> fields = ObjectUtils.ifNull(entity, e -> getConfidentialFields(entity.getClass()));
        updateConfidentialProperties(entity, fields, codec);
    }

    /**
     * 更新敏感属性数据
     *
     * @param entity 数据实体
     * @param fields 敏感字段列表
     * @param codec  敏感数据编解码回调方法
     * @param <T>    数据类型
     */
    private static <T> void updateConfidentialProperties(T entity, List<Field> fields,
                                                         @NonNull Function<String, String> codec
                                                        ) {
        if (entity == null || CollectionUtils.isEmpty(fields)) {
            return;
        }
        for (Field field : fields) {
            String value = (String) ObjectUtils.getObjectProperty(entity, field);
            if (log.isDebugEnabled()) {
                log.debug("Before codec[{}]: {}", field.getName(), value);
            }
            value = codec.apply(value);
            if (log.isDebugEnabled()) {
                log.debug("After codec[{}]: {}", field.getName(), value);
            }
            ObjectUtils.setObjectProperty(entity, field, value);
        }
    }

    /**
     * 获取指定树以及所有子节点列表
     *
     * @param clazz 对象类型
     * @param id    树对象主键
     * @param <T>   对象类型
     *
     * @return 树对象列表
     */
    public static <T extends TreeEntity<? extends Number>> List<T> listTreeFamilies(@NonNull Class<T> clazz,
                                                                                    @NonNull Number id
                                                                                   ) {
        return listTreeFamilies(getService(clazz), id);
    }

    /**
     * 获取指定树以及所有子节点列表
     *
     * @param service 对象业务操作接口
     * @param id      树对象主键
     * @param <T>     对象类型
     *
     * @return 树对象列表
     */
    public static <T extends TreeEntity<? extends Number>> List<T> listTreeFamilies(@NonNull IService<T> service,
                                                                                    @NonNull Number id
                                                                                   ) {
        return service.lambdaQuery().likeRight(TreeEntity::getSequence, EntityUtils.buildTreeSequence(id)).list();
    }

    /**
     * 获取指定树以及所有子节点列表
     *
     * @param clazz 对象类型
     * @param ids   树对象主键集合
     * @param <T>   对象类型
     *
     * @return 树对象列表
     */
    public static <T extends TreeEntity<? extends Number>> List<T>
    listTreeFamilies(@NonNull Class<T> clazz, @NonNull Collection<? extends Number> ids) {
        return listTreeFamilies(getService(clazz), ids);
    }

    /**
     * 获取指定树以及所有子节点列表
     *
     * @param service 对象业务操作接口
     * @param ids     树对象主键集合
     * @param <T>     对象类型
     *
     * @return 树对象列表
     */
    public static <T extends TreeEntity<? extends Number>> List<T>
    listTreeFamilies(@NonNull IService<T> service, @NonNull Collection<? extends Number> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<T> query = Wrappers.lambdaQuery();
        ids.forEach(id -> query.or(c -> c.likeRight(TreeEntity::getSequence, EntityUtils.buildTreeSequence(id))));
        return service.list(query);
    }

    /**
     * 软删除对象实体
     *
     * @param clazz 对象类型
     * @param id    对象主键
     * @param <ID>  对象主键类型
     * @param <T>   对象类型
     *
     * @return true/false
     */
    public static <ID extends Number, T extends Deletable<ID>> boolean softDelete(@NonNull Class<T> clazz, ID id) {
        return softDelete(getService(clazz), id);
    }

    public static <ID extends Number, T extends Deletable<ID>> void softDelete(@NonNull Class<T> clazz, @NotEmpty Set<ID> idList) {
        String tableName = SqlHelper.table(clazz).getTableName();
        BatchSoftDeleteMapper batchSoftDeleteMapper = ApplicationContextHolder.getBean(BatchSoftDeleteMapper.class);
        // 构建sql
        StringBuilder sqlId = new StringBuilder();
        for (ID id : idList) {
            sqlId.append(id.toString()).append(",");
        }
        sqlId = new StringBuilder(sqlId.substring(0, sqlId.length() - 1));
        batchSoftDeleteMapper.softDelete(
                "update " + tableName + " set deleted = " + System.currentTimeMillis() + " where id in (" + sqlId + ")");
    }

    /**
     * 软删除对象实体
     *
     * @param service 对象业务操作接口
     * @param id      对象主键
     * @param <ID>    对象主键类型
     * @param <T>     对象类型
     *
     * @return true/false
     */
    public static <ID extends Number, T extends Deletable<ID>> boolean softDelete(@NonNull IService<T> service, ID id) {
        return softDelete(service, ObjectUtils.ifNull(id, service::getById));
    }

    /**
     * 软删除对象实体
     *
     * @param clazz  对象类型
     * @param entity 可删除对象实体
     * @param <T>    对象类型
     *
     * @return true/false
     */
    public static <T extends Deletable<? extends Number>> boolean softDelete(@NonNull Class<T> clazz, T entity) {
        return softDelete(getService(clazz), entity);
    }

    /**
     * 软删除对象实体
     *
     * @param clazz    对象类型
     * @param entities 可删除对象实体集合
     * @param <T>      对象类型
     *
     * @return true/false
     */
    public static <T extends Deletable<? extends Number>> boolean softDelete(@NonNull Class<T> clazz,
                                                                             Collection<T> entities
                                                                            ) {
        return softDelete(getService(clazz), entities);
    }

    /**
     * 软删除对象实体
     *
     * @param service 对象业务操作接口
     * @param entity  可删除对象实体
     * @param <T>     对象类型
     *
     * @return true/false
     */
    public static <T extends Deletable<? extends Number>> boolean softDelete(@NonNull IService<T> service,
                                                                             T entity
                                                                            ) {
        if (entity != null) {
            entity.setDeleted(true);
            return service.updateById(entity);
        }
        return false;
    }

    /**
     * 软删除对象实体
     *
     * @param service  对象业务操作接口
     * @param entities 可删除对象实体集合
     * @param <T>      对象类型
     *
     * @return true/false
     */
    public static <T extends Deletable<? extends Number>> boolean softDelete(@NonNull IService<T> service,
                                                                             Collection<T> entities
                                                                            ) {
        if (!CollectionUtils.isEmpty(entities)) {
            entities.forEach(entity -> entity.setDeleted(true));
            return service.updateBatchById(entities);
        }
        return false;
    }

    @Bean
    public Transactor transactor() {
        return new Transactor() {
            @Override
            @Transactional(rollbackFor = Exception.class)
            public Object execute(Executor executor) throws Exception {
                return executor == null ? null : executor.execute();
            }

            @Override
            @Transactional(rollbackFor = Exception.class)
            public void execute(Collection<Executor> executors) throws Exception {
                if (!CollectionUtils.isEmpty(executors)) {
                    for (Executor executor : executors) {
                        if (executor != null) {
                            executor.execute();
                        }
                    }
                }
            }
        };
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化事务处理器
        TRANSACTOR = ApplicationContextHolder.getBean(Transactor.class);

        // 初始化Session模版实例
        SESSION_TEMPLATE = ApplicationContextHolder.getBean(SqlSessionTemplate.class);

        // 加载应用中所有数据实体类
        ENTITY_CLASSES = ApplicationContextHolder.lookupApplicationClasses(clazz -> clazz != null
                                                                                    && !Modifier.isInterface(clazz.getModifiers()) &&
                                                                                    !Modifier.isAbstract(clazz.getModifiers())
                                                                                    && Entity.class.isAssignableFrom(clazz)
                                                                          );

        // 初始化对象机密数据字段映射表
        CONFIDENTIAL_CLASS_FIELD_MAPPING = ENTITY_CLASSES.stream().collect(Collectors.toMap(clazz -> clazz, clazz -> {
                    List<Field> fields = new LinkedList<>();
                    Confidential confidential = clazz.getAnnotation(Confidential.class);
                    ObjectUtils.iterateClassFields(clazz, (field, index) -> {
                        Confidential _confidential;
                        if (field.getType() == String.class
                            && ((_confidential = field.getAnnotation(Confidential.class)) != null
                                || (_confidential = confidential) != null) && _confidential.value()) {
                            fields.add(field);
                        }
                    });
                    return fields;
                }, (o, n) -> n)).entrySet().stream().filter(entry -> !CollectionUtils.isEmpty(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 初始化可设置删除标记的数据表名称集合
        DELETABLE_TABLE_NAMES = lookupEntityTables(Deletable.class).stream().map(TableInfo::getTableName)
                .map(String::toLowerCase).collect(Collectors.toSet());

        // 初始化可设置隔离标记的数据表名称集合
        ISOLATABLE_TABLE_NAMES = lookupEntityTables(Isolatable.class).stream().map(TableInfo::getTableName)
                .map(String::toLowerCase).collect(Collectors.toSet());

        SUPPORT_DATA_PERMISSION_NAMES = lookupEntityTablesForDataPermission().stream().map(TableInfo::getTableName)
                .map(String::toLowerCase).collect(Collectors.toSet());

        // 初始化数据编解码配置
        CODEC = ApplicationContextHolder.getBean(DatabaseSecurityProperties.class).getCodec();
        if (!StringUtils.isEmpty(CODEC.getSecret())) {
            CODEC.setSecret(Base64.encodeBase64String(AESUtils.revise(CODEC.getSecret()).getBytes()));
        }
        if (!StringUtils.isEmpty(CODEC.getIv())) {
            CODEC.setIv(Base64.encodeBase64String(AESUtils.revise(CODEC.getIv()).getBytes()));
        }
    }

    /**
     * 事务方法执行器
     */
    public interface Executor {
        /**
         * 执行方法
         *
         * @return 执行结果
         *
         * @throws Exception 操作异常
         */
        Object execute() throws Exception;
    }

    /**
     * 事务处理器
     */
    public interface Transactor {
        /**
         * 事务执行
         *
         * @param executor 事务方法接口
         *
         * @return 执行结果
         *
         * @throws Exception 操作异常
         */
        Object execute(Executor executor) throws Exception;

        /**
         * 事务执行
         *
         * @param executors 事务方法回调集合
         *
         * @throws Exception 操作异常
         */
        void execute(Collection<Executor> executors) throws Exception;
    }

}
