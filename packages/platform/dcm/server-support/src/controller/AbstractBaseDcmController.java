package com.guhecloud.platform.dcm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.dcm.entity.DcmDeclaration;
import com.guhecloud.platform.dcm.entity.DcmRequirement;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.exception.DcmRuntimeException;
import com.guhecloud.platform.dcm.param.DcmDeclarationParam;
import com.guhecloud.platform.dcm.param.DcmRequirementParam;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;
import com.guhecloud.platform.dcm.support.DcmSupport;
import com.guhecloud.platform.dcm.support.IRciApplier;
import com.guhecloud.platform.dcm.support.IRciComparator;
import com.guhecloud.platform.dcm.support.ITenantedRciApplier;
import com.guhecloud.platform.dcm.utils.DcmTools;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class AbstractBaseDcmController {

    private static final String MEDIA_TYPE_YAML = "application/yaml";

    private static final DcmCap DUMMY_DCM_CAP = new DcmCap(Collections::emptyList,
                                                           (to, from, isPartial) -> Collections.emptyList(),
                                                           (rcis) -> Collections.emptyList());

    private static final TenantedDcmCap DUMMY_TDCM_CAP = new TenantedDcmCap(
            (tenant) -> Collections.emptyList(),
            (to, from, isPartial) -> Collections.emptyList(),
            (rcis, tenant) -> Collections.emptyList());

    private final Map<String, Map<String, DcmCap>> dcmCapRegistry = new HashMap<>();

    private final Map<String, Map<String, TenantedDcmCap>> tenantedDcmCapRegistry = new HashMap<>();

    private final Map<String, List<String>> supportedProviderTypes = new HashMap<>();

    private boolean capRegistryValid;

    @Value("${dcm.server.enable-non-partial-mode:false}")
    protected boolean enableNonPartialMode;

    @Value("${dcm.server.enable-removal:false}")
    protected boolean enableRemoval;

    /**
     * 获取指定参数对应的运行时Dcm配置申明
     *
     * @param isSystem 是否系统级配置
     * @param tenant   当获取非系统级配置时，指定的租户
     *
     * @return 以完整的配置申明格式表达的的配置。
     */
    @GetMapping(params = "runtime=true",
                produces = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_YAML }
    )
    public DcmDeclarationParam getRuntimeDcmDeclaration(
            @RequestParam(value = "system", required = false) Boolean isSystem,
            @RequestParam(value = "tenant", required = false) String tenant,
            @RequestParam(value = "types", required = false) Collection<String> types
                                                       ) {
        types = commonConfigTypeFilterProcessing(types);
        Map<String, List<RequestedConfigItem>> requestedConfigItems = fetchRuntimeConfigInner(isSystem, tenant, types);
        List<DcmRequirementParam> dcmRequirementParams = new ArrayList<>();

        for (Map.Entry<String, List<RequestedConfigItem>> entry : requestedConfigItems.entrySet()) {
            DcmRequirementParam dcmRequirements = new DcmRequirementParam();
            dcmRequirements.setProvider(entry.getKey());
            dcmRequirements.setRequirements(entry.getValue().stream()
                                                    .map(RequestedConfigItemParam::fromEntity)
                                                    .collect(Collectors.toList()));
            dcmRequirementParams.add(dcmRequirements);
        }
        return DcmDeclarationParam.builder()
                .schemaVersion("1.0")
                .requests(dcmRequirementParams)
                .build();
    }

    /**
     * 获取内建的DCM配置申明
     *
     * @return 以完整的配置申明格式表达的的配置。
     *
     * @throws IOException
     */
    @GetMapping(params = "runtime=false",
                produces = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_YAML }
    )
    public DcmDeclarationParam getBuiltinDcmDeclaration() throws IOException {
        Optional<Resource> resource = getBuiltinDcmDeclarationAsResource();
        if (!resource.isPresent()) {
            throw new ResourceNotFoundException("builtin dcm resources not found");
        }
        return DcmTools.Parser
                .declarationParamFromYamlContent(resource.get().getInputStream());
    }

    private Optional<Resource> getBuiltinDcmDeclarationAsResource() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(getBuiltinDcmResourcePath());
        if (resources.length > 0) {
            return Optional.of(resources[0]);
        } else {
            return Optional.empty();
        }
    }

    /**
     * 本模块内嵌的申明与当前Runtime配置的差异。
     *
     * @param isSystem 是否系统级配置
     * @param tenant   当处理非系统级配置时，指定的租户
     *
     * @return 以配置差异列表形式表达的配置差异
     */
    @GetMapping(value = "/diff",
                produces = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_YAML }
    )
    public List<RequestedConfigItem> getInternalDcmDiff(
            @RequestParam(value = "system", required = false) Boolean isSystem,
            @RequestParam(value = "tenant", required = false) String tenant
                                                       ) {

        if (isSystem == null) {
            isSystem = StringUtils.isEmpty(tenant);
        }
        if (Boolean.TRUE.equals(isSystem)) {
            tenant = null;
        }

        return getInternalDcmDiffInner(isSystem, tenant);
    }

    private List<RequestedConfigItem> getInternalDcmDiffInner(Boolean isSystem, String tenant) {
        Optional<Resource> resource;
        try {
            resource = getBuiltinDcmDeclarationAsResource();
        } catch (IOException e) {
            log.error("load built-in dcm declaration failed. error message: {}", e.getMessage());
            throw new ResourceNotFoundException("Could not load built-in Dcm declaration");
        }
        if (!resource.isPresent()) {
            return Collections.emptyList();
        }
        DcmDeclaration declaration;
        try {
            declaration = DcmTools.Parser
                    .declarationParamFromYamlContent(resource.get().getInputStream())
                    .toEntity();
        } catch (IOException e) {
            log.error("load/parse builtin dcm configuration failed, error message: {}", e.getMessage());
            throw new DcmRuntimeException("load/parse dcm configuration failed", e);
        }

        // filter requirements for current module & specified tenant
        List<DcmRequirement> requirements = declaration.getRequests().stream()
                .filter(request -> isProviderSupportedInner(request.getProvider()))
                .map(x -> x.filterRequirementsByTenant(tenant))
                .collect(Collectors.toList());

        for (DcmRequirement requirement : requirements) {
            enforcePartialModeEnableRules(requirement);
        }

        // diff with runtime configurations.
        Map<String, List<RequestedConfigItem>> existedConfigs = fetchRuntimeConfigInner(isSystem, tenant, Collections.emptyList());
        // 所有取回来的配置都是自身所支持的，就简单合并了传递给比较方法。
        return compare(requirements, existedConfigs.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(x -> x.getConfigType().toUpperCase(), LinkedHashMap::new,
                                               Collectors.toList())), isSystem, tenant, Collections.emptySet());
    }

    /**
     * 获取指定的Dcm配置申明与对应的Runtime配置间的差异。
     *
     * @param isSystem            是否系统级配置
     * @param tenant              当处理非系统级配置时，指定的租户
     * @param types               (可选）指定的配置类型
     * @param dcmDeclarationParam 待比较的配置申明
     *
     * @return 以差异列表形态表达的配置差异。
     */
    @PostMapping(value = "/diff",
                 produces = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_YAML },
                 consumes = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_YAML }
    )
    public List<RequestedConfigItem> getExternalDcmDiff(
            @RequestParam(value = "system", required = false) Boolean isSystem,
            @RequestParam(value = "tenant", required = false) String tenant,
            @RequestParam(value = "types", required = false) Collection<String> types,
            @RequestBody @Valid DcmDeclarationParam dcmDeclarationParam
                                                       ) throws DcmRuntimeException {
        types = commonConfigTypeFilterProcessing(types);
        if (isSystem == null) {
            isSystem = StringUtils.isEmpty(tenant);
        }
        if (Boolean.TRUE.equals(isSystem) && StringUtils.isNotEmpty(tenant)) {
            log.warn("getExternalDcmDiff param conflicts: system: True while tenant: {}", tenant);
            tenant = null;
        }
        List<RequestedConfigItem> diffs = new ArrayList<>();
        for (DcmRequirementParam requirementParam : dcmDeclarationParam.getRequests()) {
            // 一种Provider的配置分拆成多个部分后结果将不确定.
            if (isProviderSupportedInner(requirementParam.getProvider())) {
                // convert & filter out requirements supported by current module &
                diffs.addAll(getExternalDcmDiffInner(isSystem, tenant, requirementParam, types));
            }
        }
        return diffs;
    }

    private Collection<String> commonConfigTypeFilterProcessing(Collection<String> types) {
        if (types == null) {
            return Collections.emptySet();
        } else {
            Set<String> typesSet = types.stream().
                    flatMap(x -> StringUtils.isNotEmpty(x) ?
                                 Stream.of(x.split(","))
                                         .filter(Objects::nonNull)
                                         .filter(s -> !s.isEmpty())
                                                           : Stream.empty())
                    .map(String::toUpperCase)
                    .collect(Collectors.toSet());
            if (typesSet.contains("ALL")) {
                log.info("filter [{}] convert to empty for 'all' specified as item ", types);
                typesSet = Collections.emptySet();
            }
            return typesSet;
        }
    }

    /**
     * 判定是否非增量模式
     */
    private boolean isNonPartialMode(DcmRequirementParam dcmRequirementParam) {
        return enableNonPartialMode && Boolean.FALSE.equals(dcmRequirementParam.getIsPartialMode());
    }

    private List<RequestedConfigItem> getExternalDcmDiffInner(Boolean isSystem, String tenant, DcmRequirementParam dcmRequirementParam,
                                                              Collection<String> typeFilter
                                                             ) throws DcmRuntimeException {
        DcmRequirement requirements = dcmRequirementParam.toEntity();
        DcmRequirement filteredRequirements = requirements.filterRequirementsByTenant(tenant);

        enforcePartialModeEnableRules(filteredRequirements);

        if (!isNonPartialMode(dcmRequirementParam) && filteredRequirements.getRequirements().isEmpty()) {
            log.info("requested configurations not matched with isSystem: {}, tenant:{}",
                     isSystem, tenant);
            return Collections.emptyList();
        }
        // 非partial mode,且有实际请求， 执行比较逻辑。
        if (filteredRequirements.getRequirements().isEmpty()) {
            log.warn("DANGEROUS: non partial mode but requested config is empty");
        }
        Map<String, List<RequestedConfigItem>> existedConfigs = fetchRuntimeConfigInner(isSystem, tenant, typeFilter);
        return compare(Collections.singletonList(filteredRequirements),
                       existedConfigs.values().stream()
                               .flatMap(Collection::stream)
                               .collect(Collectors.groupingBy(x -> x.getConfigType().toUpperCase())),
                       isSystem, tenant, typeFilter);
    }

    private void enforcePartialModeEnableRules(DcmRequirement filteredRequirements) {
        if (!enableNonPartialMode && !Boolean.TRUE.equals(filteredRequirements.getIsPartialMode())) {
            log.info("non partial mode is disabled, force partial mode be TRUE for request to {}",
                     filteredRequirements.getProvider());
            filteredRequirements.setIsPartialMode(Boolean.TRUE);
        }
    }

    /**
     * 应用申明式配置。
     * dcmDeclarationParam可能包含系统配置与租户配置， 本次请求处理仅处理与isSystem/tenant匹配的部分。
     *
     * @param isSystem            本请求处理系统部分或租户部分
     * @param tenant              处理租户部分时有效，指定具体的租户。
     * @param dcmDeclarationParam 请求执行的配置申明
     *
     * @return 实际被应用的配置项
     */
    @PostMapping(value = "/apply",
                 produces = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_YAML },
                 consumes = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_YAML }
    )
    public List<RequestedConfigItem> applyDcmDeclaration(
            @RequestParam(value = "system", required = false) Boolean isSystem,
            @RequestParam(value = "tenant", required = false) String tenant,
            @RequestParam(value = "types", required = false) Collection<String> types,
            @RequestBody DcmDeclarationParam dcmDeclarationParam
                                                        ) {
        log.info("applyDcmDeclaration: isSystem: {}, tenant: {}, requests:\n{}", isSystem, tenant, dcmDeclarationParam);
        if (isSystem == null) {
            isSystem = StringUtils.isEmpty(tenant);
        }
        if (Boolean.TRUE.equals(isSystem) && StringUtils.isNotEmpty(tenant)) {
            log.warn("getExternalDcmDiff param conflicts: system: True while tenant: {}", tenant);
            tenant = null;
        }
        types = commonConfigTypeFilterProcessing(types);
        List<DcmRequirementParam> dcmRequirementParams = new ArrayList<>();
        for (DcmRequirementParam requirementsParam : dcmDeclarationParam.getRequests()) {
            if (isProviderSupportedInner(requirementsParam.getProvider())) {
                dcmRequirementParams.add(requirementsParam);
            }
        }
        if (dcmRequirementParams.isEmpty()) {
            throw new ResourcePreconditionFailedException("dcmDeclarationParam", "no supported requirements found");
        }
        List<RequestedConfigItem> ret = new ArrayList<>();
        for (DcmRequirementParam requirementsParam : dcmRequirementParams) {
            List<RequestedConfigItem> diffs;
            if (!enableNonPartialMode && !Boolean.TRUE.equals(requirementsParam.getIsPartialMode())) {
                log.info("non-partial-mode is disabled, force partial-mode be TRUE for request to {}",
                         requirementsParam.getProvider());
                requirementsParam.setIsPartialMode(Boolean.TRUE);
            }

            if (Boolean.TRUE.equals(requirementsParam.getUseBuiltinConfig())) {
                diffs = getInternalDcmDiffInner(isSystem, tenant);
            } else {
                diffs = getExternalDcmDiffInner(isSystem, tenant, requirementsParam, types);
            }
            if (diffs.isEmpty()) {
                log.info("not difference found to apply for {}.", requirementsParam.getProvider());
                continue;
            }
            int diffCnt = diffs.size();
            log.info("Found {} differences to apply.", diffCnt);
            try {
                log.info("Diffs: {}", new YAMLMapper().writeValueAsString(diffs));
            } catch (JsonProcessingException e) {
                log.debug("something wrong in dump diff results", e);
            }

            if (!enableRemoval) {
                diffs = diffs.stream().filter(x -> !Boolean.TRUE.equals(x.getIsRemoval()))
                        .collect(Collectors.toList());
            }
            if (diffs.size() != diffCnt) {
                log.warn("REMOVAL disabled but {} removal requests found and will be ignored.",
                         diffCnt - diffs.size());
            }
            ret.addAll(applyRequestConfigDiffsInner(requirementsParam.getProvider().toUpperCase(), isSystem, tenant,
                                                    diffs.stream().collect(Collectors.groupingBy(x -> x.getConfigType().toUpperCase(),
                                                                                                 LinkedHashMap::new, Collectors.toList()))));
        }
        List<Object> errors = ret.stream().filter(x -> x.getErrors() != null && !x.getErrors().isEmpty())
                .flatMap(x -> x.getErrors().stream())
                .collect(Collectors.toList());
        if (!errors.isEmpty()) {
            log.error("apply {} differences finished, {} errors raised", ret.size(), errors.size());
            throw new DcmRuntimeException("Failure in apply dcm configurations ", (Serializable) errors);
        }
        log.info("apply {} differences finished.", ret.size());
        return ret;
    }

    private Map<String, List<RequestedConfigItem>> fetchRuntimeConfigInner(Boolean isSystem, String tenant, Collection<String> types) {
        if (isSystem == null) {
            isSystem = StringUtils.isEmpty(tenant);
        }
        if (isSystem) {
            tenant = null;
        } else {
            if (!isValidTenantId(tenant)) {
                throw new RuntimeException("invalid tenant id " + tenant);
            }
        }
        types = commonConfigTypeFilterProcessing(types);

        if (capRegistryValid) {
            return fetchRuntimeConfigViaCapRegistry(isSystem, tenant, types);
        } else {
            return fetchRuntimeConfig(isSystem, tenant);
        }
    }

    private Map<String, List<RequestedConfigItem>> fetchRuntimeConfigViaCapRegistry(
            Boolean isSystem, String tenant, Collection<String> typeFilter
                                                                                   ) {
        Map<String, List<RequestedConfigItem>> retMap = new LinkedHashMap<>();
        // 通过注册的能力获取运行时配置。
        for (String provider : supportedProviderTypes.keySet()) {
            List<RequestedConfigItem> rcis = new ArrayList<>();
            List<String> configTypes = supportedProviderTypes.get(provider);
            if (isSystem) {
                Map<String, DcmCap> capMap = dcmCapRegistry.getOrDefault(provider, Collections.emptyMap());
                for (String configType : configTypes) {
                    if (!isConfigTypeRequested(typeFilter, configType)) {
                        continue;
                    }
                    try {
                        rcis.addAll(capMap.getOrDefault(configType, DUMMY_DCM_CAP)
                                            .rciFetcher.get());
                    } catch (Exception e) {
                        log.warn("exception fetch rcis for {}:{}", provider, configType, e);
                    }
                }
            } else {
                Map<String, TenantedDcmCap> capMap = tenantedDcmCapRegistry.getOrDefault(provider, Collections.emptyMap());
                DcmSupport.executeWithTenant(Long.valueOf(tenant), () -> {
                    for (String configType : configTypes) {
                        if (!isConfigTypeRequested(typeFilter, configType)) {
                            continue;
                        }
                        try {
                            rcis.addAll(capMap
                                                .getOrDefault(configType, DUMMY_TDCM_CAP)
                                                .rciFetcher.apply(Long.valueOf(tenant)));
                        } catch (Exception e) {
                            log.warn("exception fetch rcis for {}:{}", provider, configType, e);
                        }
                    }
                });
            }
            retMap.put(provider, rcis);
        }
        return retMap;
    }

    /**
     * 判定String形态的租户Id是否有效。
     */
    protected final boolean isValidTenantId(String tenant) {
        try {
            Long.parseUnsignedLong(tenant);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    /**
     * 获取运行时配置数据。如果本模块支持多种配置数据，返回数据需要可以明确标志出来是哪一类配置数据。
     * 返回的数据以最小表示形式呈现（即：同一个项目在同级别中出现且仅出现一次）
     * 如果支持了registerDcmCaps, 不再需要实现本方法。
     *
     * @param isSystem 处理系统配置或租户配置
     * @param tenant   适用于租户配置时获取哪一个租户的配置。
     *
     * @return 以Provider->List of RequestConfigItem形式表达的运行时配置数据。
     *
     * @see #registerDcmCaps()
     * @deprecated 建议以 registerDcmCap形态组织代码。
     */
    protected Map<String, List<RequestedConfigItem>> fetchRuntimeConfig(
            Boolean isSystem, String tenant) {
        throw new UnsupportedOperationException("override fetchRuntimeConfig or register dcm capabilities");
    }

    /**
     * 获取内置的DCM声明文件路径（样式）。
     *
     * @return 内置DCM申明配置资源路径
     */
    protected abstract String getBuiltinDcmResourcePath();

    /**
     * 判定是否支持指定的provider.
     * 如果实现了 getSupportedProviderAndTypes, 不需要再实现本方法。
     *
     * @param provider DCM配置提供者标识
     *
     * @return True if supported.
     *
     * @see #getSupportedProviderAndTypes()
     */
    protected boolean isProviderSupported(String provider) {
        return supportedProviderTypes.containsKey(provider.trim().toUpperCase());
    }

    /**
     * 以provider -> configTypes 形式提供支持的Provider和配置类型。
     * 注意不同的config type是有序的, 需要注意保持其逻辑依赖的偏序关系, 更基础的在前.
     * <p>
     * 内部实现会将provider和configType均存储为大写形态。
     *
     * @see #isProviderSupported(String)
     */
    protected abstract Map<String, List<String>> getSupportedProviderAndTypes();

    private boolean isProviderSupportedInner(String provider) {
        return provider != null && (supportedProviderTypes.isEmpty() && isProviderSupported(provider)
                                    ||
                                    !supportedProviderTypes.isEmpty() && supportedProviderTypes.containsKey(provider.trim().toUpperCase()));
    }

    /**
     * 在指定的是否系统配置/是否指定租户配置意义下， 比较请求的配置内容与当前的配置项。
     *
     * @param requirements   请求项, 均已过滤过。
     * @param existedConfigs 当前存在的配置项, 以以configType(大写形态) 到 changes列表的映射Map形式提供
     * @param isSystem       True 仅处理系统级配置
     * @param tenant         当isSystem为False时有效， 为指定的租户id
     * @param typeFilter     指定待处理的类型（以大写形态提供），为空时表示处理所有类型，否则仅处理指定类型
     *
     * @return 以RequestedConfigItem列表形态返回的差异
     *
     * @deprecated 子类建议实现compareByConfigType的接口, 或更进一步用 registerDcmCaps 替换
     */
    protected List<RequestedConfigItem> compare(List<DcmRequirement> requirements, Map<String,
                                                        List<RequestedConfigItem>> existedConfigs, boolean isSystem, String tenant,
                                                Collection<String> typeFilter
                                               ) {
        List<RequestedConfigItem> diffs = new ArrayList<>();
        for (DcmRequirement dcmRequirement : requirements) {
            Map<String, List<RequestedConfigItem>> rcisByConfigType = dcmRequirement.getRequirements()
                    .stream()
                    .filter(rci -> isConfigTypeRequested(typeFilter, rci.getConfigType()))
                    .collect(Collectors.groupingBy(x -> x.getConfigType().toUpperCase()));
            boolean isPartial = !Boolean.FALSE.equals(dcmRequirement.getIsPartialMode());
            for (String configType : supportedProviderTypes.getOrDefault(
                    dcmRequirement.getProvider().toUpperCase(), Collections.emptyList())) {
                if (isConfigTypeRequested(typeFilter, configType)) {
                    try {
                        diffs.addAll(compareByConfigTypeInner(dcmRequirement.getProvider().toUpperCase(), configType,
                                                              rcisByConfigType.getOrDefault(configType, Collections.emptyList()),
                                                              existedConfigs.getOrDefault(configType, Collections.emptyList()),
                                                              isSystem, isPartial));
                    } catch (RuntimeException re) {
                        log.warn("exception in comparing {}:{}, error: {} ",
                                 dcmRequirement.getProvider(), configType, re, re);
                    }
                }
            }
        }
        return diffs;
    }

    private Collection<RequestedConfigItem> compareByConfigTypeInner(String provider, String configType, List<RequestedConfigItem> toState,
                                                                     List<RequestedConfigItem> fromState, boolean isSystem,
                                                                     boolean isPartial
                                                                    ) {
        if (enableRemoval && enableNonPartialMode && isPartial) {
            // 遍历所有的RequestConfigItem, 如果有任意的isPartial显式设置为False, 将isPartial设置为False
            RequestedConfigItem rci = toState.stream()
                    .filter(x -> Boolean.FALSE.equals(x.getIsPartial()))
                    .findAny()
                    .orElse(null);

            if (rci != null) {
                log.warn("DCM Comparing of {}:{} turn into NON-PARTIAL MODE for records[{}..]",
                         provider, configType, rci.briefInfo());
                isPartial = false;
            }
        }
        if (!capRegistryValid) {
            return compareByConfigType(provider, configType, toState, fromState, isSystem, isPartial);
        }
        if (isSystem) {
            return dcmCapRegistry
                    .getOrDefault(provider, Collections.emptyMap())
                    .getOrDefault(configType.toUpperCase(), DUMMY_DCM_CAP)
                    .rciComparator.compare(toState, fromState, isPartial);
        } else {
            return tenantedDcmCapRegistry
                    .getOrDefault(provider, Collections.emptyMap())
                    .getOrDefault(configType.toUpperCase(), DUMMY_TDCM_CAP)
                    .rciComparator.compare(toState, fromState, isPartial);
        }
    }

    private boolean isConfigTypeRequested(Collection<String> requestedConfigTypes, String configType) {
        return requestedConfigTypes.isEmpty() || requestedConfigTypes.contains(configType.toUpperCase());
    }

    /**
     * 比较指定ConfigType的请求配置和已存在配置。 除configType/requestConfigItems外，其他参数同 #compare
     *
     * @param provider             当前比较适用的Provider
     * @param configType           当前比较适用的配置类型
     * @param requestedConfigItems 请求的目标配置项目
     * @param existedEntities      当前项目下的已存在配置
     *
     * @see IRciComparator#compare(List, List, boolean)
     * @see #compare(List, Map, boolean, String, Collection)
     * @deprecated 使用{@link #registerDcmCaps } 替换分散的实现
     */
    protected Collection<RequestedConfigItem> compareByConfigType(
            String provider, String configType,
            List<RequestedConfigItem> requestedConfigItems, List<RequestedConfigItem> existedEntities,
            boolean isSystem, boolean isPartial
                                                                 ) {
        throw new UnsupportedOperationException();
    }

    /**
     * 执行差异应用动作。
     *
     * @param isSystem 差异是针对系统级配置还是非系统级配置
     * @param tenant   非系统级配置是差异需要应用到的租户
     * @param diffs    经过滤后待执行的配置差异。以configType(大写形态) 到 changes列表的映射Map形式提供
     *
     * @return 实际被应用成功的配置差异
     *
     * @see #registerDcmCaps()
     * @deprecated 对于简单模型，推荐使用registryDcmCaps的方式支持。
     */
    protected List<RequestedConfigItem> applyRequestConfigDiffs(Boolean isSystem, String tenant,
                                                                Map<String, List<RequestedConfigItem>> diffs
                                                               ) {
        throw new UnsupportedOperationException();
    }

    private List<RequestedConfigItem> applyRequestConfigDiffsInner(String provider, Boolean isSystem, String tenant,
                                                                   Map<String, List<RequestedConfigItem>> diffs
                                                                  ) {

        if (!capRegistryValid) {
            return applyRequestConfigDiffs(isSystem, tenant, diffs);
        }
        if (CollectionUtils.isEmpty(supportedProviderTypes.get(provider.toUpperCase()))
            || (isSystem ? dcmCapRegistry : tenantedDcmCapRegistry).get(provider.toUpperCase()) == null) {
            return Collections.emptyList();
        }

        // 分别过滤添加更新类型记录和删除类型记录
        LinkedHashMap<String, List<RequestedConfigItem>> addOrUpdateChanges = new LinkedHashMap<>();
        LinkedHashMap<String, List<RequestedConfigItem>> removeChanges = new LinkedHashMap<>();

        for (Map.Entry<String, List<RequestedConfigItem>> entry : diffs.entrySet()) {
            addOrUpdateChanges.put(entry.getKey(),
                                   entry.getValue().stream().filter(x -> Boolean.FALSE.equals(x.getIsRemoval()))
                                           .collect(Collectors.toList()));
            List<RequestedConfigItem> changes =
                    entry.getValue().stream().filter(x -> !Boolean.FALSE.equals(x.getIsRemoval())).collect(Collectors.toList());
            if (!changes.isEmpty()) {
                removeChanges.put(entry.getKey(), changes);
            }
        }
        List<Object> errors = new ArrayList<>();
        List<RequestedConfigItem> rcis = new ArrayList<>();
        List<String> supportedChangeTypes = supportedProviderTypes.get(provider.toUpperCase());
        ListIterator<String> it = supportedChangeTypes.listIterator();
        if (isSystem) {
            Map<String, DcmCap> caps = dcmCapRegistry.get(provider);
            while (it.hasNext()) {
                String changeType = it.next();
                try {
                    if (CollectionUtils.isNotEmpty(addOrUpdateChanges.get(changeType)) && caps.get(changeType) != null) {
                        rcis.addAll(caps.get(changeType).rciApplier.apply(addOrUpdateChanges.get(changeType)));
                    }
                } catch (RuntimeException re) {
                    String errorMessage = String.format("apply addOrUpdate of %s:%s failed, error: %s", provider, changeType, re);
                    log.warn("{}", errorMessage, re);
                    errors.add(errorMessage);
                }
            }
            // revert to order of supportedChangeTypes for remove
            while (enableRemoval && it.hasPrevious()) {
                String changeType = it.previous();
                try {
                    if (CollectionUtils.isNotEmpty(removeChanges.get(changeType)) && caps.get(changeType) != null) {
                        rcis.addAll(caps.get(changeType).rciApplier.apply(removeChanges.get(changeType)));
                    }
                } catch (RuntimeException re) {
                    String errorMessage = String.format("apply removal of %s:%s failed, error: %s", provider, changeType, re.getMessage());
                    log.warn("{}", errorMessage, re);
                    errors.add(errorMessage);
                }
            }
        } else {
            Long tenantId = Long.valueOf(tenant);
            DcmSupport.executeWithTenant(tenantId, () -> {
                Map<String, TenantedDcmCap> caps = tenantedDcmCapRegistry.getOrDefault(provider, Collections.emptyMap());
                while (it.hasNext()) {
                    String changeType = it.next();
                    try {
                        if (CollectionUtils.isNotEmpty(addOrUpdateChanges.get(changeType)) && caps.get(changeType) != null) {
                            rcis.addAll(caps.get(changeType).rciApplier.apply(addOrUpdateChanges.get(changeType), tenantId));
                        }
                    } catch (RuntimeException re) {
                        String errorMessage = String.format("apply addOrUpdate of %s:%s to tenant: %s failed, error: %s",
                                                            provider, changeType, tenantId, re);
                        log.warn("{}", errorMessage, re);
                        errors.add(errorMessage);
                    }
                }
                while (enableRemoval && it.hasPrevious()) {
                    String changeType = it.previous();
                    try {
                        if (CollectionUtils.isNotEmpty(removeChanges.get(changeType)) && caps.get(changeType) != null) {
                            rcis.addAll(caps.get(changeType).rciApplier.apply(removeChanges.get(changeType), tenantId));
                        }
                    } catch (RuntimeException re) {
                        String errorMessage = String.format("apply removal of %s:%s to tenant: %s failed, error: %s",
                                                            provider, changeType, tenantId, re);
                        log.warn("{}", errorMessage, re);
                        errors.add(errorMessage);
                    }
                }
            });
        }
        if (!errors.isEmpty()) {
            rcis.add(RequestedConfigItem.builder().errors(errors).build());
        }
        return rcis;
    }

    /**
     * 以下用于分解实现
     */
    @AllArgsConstructor
    private static class DcmCap {
        Supplier<List<RequestedConfigItem>> rciFetcher;
        IRciComparator rciComparator;
        IRciApplier rciApplier;
    }

    @AllArgsConstructor
    private static class TenantedDcmCap {
        Function<Long, List<RequestedConfigItem>> rciFetcher;
        IRciComparator rciComparator;
        ITenantedRciApplier rciApplier;
    }

    @PostConstruct
    void afterPropertiesSet() {
        initDcmHelpers();
    }

    private void initDcmHelpers() {
        registerDcmCaps();
        capRegistryValid = !dcmCapRegistry.isEmpty() || !tenantedDcmCapRegistry.isEmpty();
        Map<String, List<String>> typesByProvider = getSupportedProviderAndTypes();
        for (String provider : typesByProvider.keySet()) {
            supportedProviderTypes.put(provider.toUpperCase(), typesByProvider.get(provider).stream()
                                               .map(String::toUpperCase).collect(Collectors.toList())
                                      );
        }
    }

    /**
     * 注册系统配置Dcm能力集
     *
     * @param provider      本能力针对的provider
     * @param configType    本能力针对的配置类型
     * @param rciSupplier   获取当前配置的实现
     * @param rciComparator 比较请求的配置项与当前配置项的实现
     * @param rciApplier    应用一组差异配置项的实现
     *
     * @see IRciComparator   差异比较器
     * @see IRciApplier      差异应用器
     */
    protected final void registerSystemDcmCaps(
            String provider, String configType,
            Supplier<List<RequestedConfigItem>> rciSupplier,
            IRciComparator rciComparator,
            IRciApplier rciApplier
                                              ) {
        Map<String, DcmCap> caps = dcmCapRegistry.computeIfAbsent(provider.toUpperCase(), k -> new HashMap<>());
        caps.put(configType.toUpperCase(), new DcmCap(rciSupplier, rciComparator, rciApplier));
    }

    /**
     * 注册租户级配置能力集
     *
     * @param provider      本能力针对的provider
     * @param configType    本能力针对的配置类型
     * @param rciSupplier   获取当前配置的实现
     * @param rciComparator 比较请求的配置项与当前配置项的实现
     * @param rciApplier    应用一组差异配置项的实现
     *
     * @see IRciComparator   差异比较器
     * @see ITenantedRciApplier  差异应用器
     */
    protected final void registerTenantDcmCaps(
            String provider, String configType,
            Function<Long, List<RequestedConfigItem>> rciSupplier,
            IRciComparator rciComparator,
            ITenantedRciApplier rciApplier
                                              ) {
        Map<String, TenantedDcmCap> caps = tenantedDcmCapRegistry.computeIfAbsent(provider.toUpperCase(), k -> new HashMap<>());
        caps.put(configType.toUpperCase(), new TenantedDcmCap(rciSupplier, rciComparator, rciApplier));
    }

    /**
     * 使用DcmCaps 注册模式的实现需要覆盖本方法， 然后调用 registerSystem/TenantedDcmCaps 添加能力集合。
     *
     * @see #registerTenantDcmCaps(String, String, Function, IRciComparator, ITenantedRciApplier)
     * @see #registerSystemDcmCaps(String, String, Supplier, IRciComparator, IRciApplier)
     */
    protected void registerDcmCaps() {
        // leave empty.
    }

}
