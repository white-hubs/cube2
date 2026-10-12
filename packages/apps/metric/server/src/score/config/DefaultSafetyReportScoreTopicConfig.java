package com.guhecloud.cube.biz.metric.server.score.config;

import com.guhecloud.cube.biz.metric.server.score.config.dto.ScoreItems;
import com.guhecloud.cube.biz.metric.server.score.config.dto.ScoreRules;
import com.guhecloud.cube.biz.metric.server.score.param.CalculateParam;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * 默认的获取记分主题配置
 *
 * @author lanchun
 * @date 2025/6/3 16:59
 */
@Slf4j
public abstract class DefaultSafetyReportScoreTopicConfig implements SafetyReportScoreTopicConfig {

    private final SafetyReportConfigService safetyReportConfigService;

    public DefaultSafetyReportScoreTopicConfig(SafetyReportConfigService safetyReportConfigService) {
        this.safetyReportConfigService = safetyReportConfigService;
    }

    private Optional<ScoreRules> getDefaultConfig() {
        return safetyReportConfigService.getScoreRules();
    }

    public Optional<ScoreItems> getTopicScoreConfig(CalculateParam param) {
        Optional<ScoreRules> scoreRulesOpt = getDefaultConfig();
        return scoreRulesOpt.map(rules -> findItemByConcatCode(rules.getScoreItems(), param.getItemCodeConcat()));
    }

    @Override
    public Optional<ScoreItems> getTopicScoreConfig(CalculateParam param, ScoreRules scoreRules) {
        return Optional.ofNullable(findItemByConcatCode(scoreRules.getScoreItems(), param.getItemCodeConcat()));
    }

    public List<ScoreItems> findAllParentItems(String itemCodeConcat) {
        Optional<ScoreRules> defaultConfig = getDefaultConfig();
        if (!defaultConfig.isPresent()) {
            return new ArrayList<>();
        }
        List<ScoreItems> resultScoreItems = new ArrayList<>();
        List<ScoreItems> scoreItems = flattenTree(defaultConfig.get().getScoreItems());
        String[] itemCodes = itemCodeConcat.split("::");
        StringBuilder itemConcat = new StringBuilder();
        for (int i = 0; i < itemCodes.length; i++) {
            if (i == itemCodes.length - 1) {
                break;
            }
            if (i == 0) {
                itemConcat.append(itemCodes[i]);
            } else {
                itemConcat.append("::").append(itemCodes[i]);
            }
            List<ScoreItems> scoreItemsList = scoreItems.stream().filter(item -> itemConcat.toString().equals(item.getItemCodeConcat()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(scoreItemsList)) {
                resultScoreItems.addAll(scoreItemsList);
            }
        }
        return resultScoreItems;
    }

    private List<ScoreItems> flattenTree(List<ScoreItems> roots) {
        List<ScoreItems> flatList = new ArrayList<>();

        if (roots == null || roots.isEmpty()) {
            return flatList;
        }

        // 使用队列进行BFS遍历
        Queue<ScoreItems> queue = new LinkedList<>(roots);

        while (!queue.isEmpty()) {
            ScoreItems current = queue.poll();

            // 将当前节点添加到扁平化列表
            flatList.add(current);

            // 将子节点加入队列
            List<ScoreItems> subItems = current.getChildren();
            if (subItems != null && !subItems.isEmpty()) {
                queue.addAll(subItems);
            }
        }
        return flatList;
    }

    public ScoreItems findItemByConcatCode(List<ScoreItems> roots, String itemCodeConcat) {
        if (roots == null || roots.isEmpty()) {
            return null;
        }
        Queue<ScoreItems> queue = new LinkedList<>(roots);  // 添加所有根节点
        while (!queue.isEmpty()) {
            ScoreItems current = queue.poll();
            // 检查当前节点是否匹配
            if (itemCodeConcat.equals(current.getItemCodeConcat())) {
                return current;
            }
            // 添加子节点到队列
            List<ScoreItems> subItems = current.getChildren();
            if (subItems != null) {
                queue.addAll(subItems);
            }
        }
        return null;
    }
}
