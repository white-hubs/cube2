package com.guhecloud.cube.iot.mgt.comm.platform;

import com.guhecloud.platform.base.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 无限层级的数字编号
 * <p>
 * 生成的id格式为: Nb-001001002
 * </p>
 * <p>
 * 生成的id的层级为: 1-1-2
 * </p>
 */
public class InfiniteHierarchicalNumbering {
    public static final String DEFAULT_IOT_PREFIX = "C";
    private static final int MAX_LEVEL_VALUE = 999;
    private final String prefix;

    public InfiniteHierarchicalNumbering(String prefix) {
        this.prefix = prefix;
    }

    public static void main(String[] args) {
        InfiniteHierarchicalNumbering h = new InfiniteHierarchicalNumbering("Nb-");
        List<Integer> currentId = new ArrayList<>();
        currentId.add(1);
        currentId.add(1);

        for (int i = 0; i < 10; i++) {
            String id = h.generateId(currentId);
            System.out.println(id);
            currentId = h.getNextId(currentId);
        }

        System.out.println("指定层级+1：");
        currentId = h.getNextId(currentId, 0);
        System.out.println(h.generateId(currentId));

        currentId = new ArrayList<>();
        currentId.add(1);
        System.out.println("\n" + h.get1LevelId(2));
        System.out.println("\n" + h.get2LevelId(1, 2));
        System.out.println("\n" + h.get3LevelId(1, 2, 1));

        System.out.println("\n");
        h.recoveryId("Nb-001002001").forEach(System.out::println);
        System.out.println("\n");
        h.recoveryId("Nb-001002").forEach(System.out::println);
        System.out.println("\n");
        h.recoveryId("Nb-001").forEach(System.out::println);

        System.out.println("c303C042".substring(0, 3));
    }

    /**
     * 生成id
     *
     * @param levels 当前id的层级
     *
     * @return id
     */
    public String generateId(List<Integer> levels) {
        StringBuilder sb = new StringBuilder(prefix);
        for (int level : levels) {
            sb.append(String.format("%03d", level));
        }
        return sb.toString();
    }

    /**
     * 获取下一个id
     *
     * @param levels 当前id的层级
     *
     * @return 下一个id的层级
     */
    public List<Integer> getNextId(List<Integer> levels) {
        return getNextId(levels, levels.size() - 1);
    }

    /**
     * 获取指定层级+1的id
     *
     * @param levels      当前id的层级
     * @param targetLevel 指定层级
     *
     * @return 下一个id的层级
     */
    public List<Integer> getNextId(List<Integer> levels, int targetLevel) {
        if (targetLevel < 0 || targetLevel >= levels.size()) {
            throw new IllegalArgumentException("目标层级不在范围内");
        }

        for (int i = targetLevel; i >= 0; i--) {
            int levelValue = levels.get(i);
            if (levelValue < MAX_LEVEL_VALUE) {
                levels.set(i, levelValue + 1);
                return levels;
            } else {
                levels.set(i, 0);
                if (i == 0) {
                    levels.add(0, 1);
                }
            }
        }
        return levels;
    }

    /**
     * 将ID恢复为层级list
     *
     * @param id id eg: Nb-001001002
     *
     * @return 层级list eg: [1, 1, 2]
     */
    public List<Integer> recoveryId(String id) {
        List<Integer> levels = new ArrayList<>();
        String[] levelStrArray = id.substring(prefix.length()).split("(?<=\\G...)");
        for (String levelStr : levelStrArray) {
            levels.add(Integer.parseInt(levelStr));
        }
        return levels;
    }

    public String[] recoveryStrId(String id, boolean needReturnPrefix) {
        if (needReturnPrefix) {
            List<String> list = new ArrayList<>(3);

            String first = id.substring(0, prefix.length() + 3);
            list.add(first);

            String remainder = id.substring(prefix.length() + 3);
            if (StringUtils.isNotBlank(remainder)) {
                Collections.addAll(list, remainder.split(("(?<=\\G\\d{3})")));
            }

            return list.toArray(new String[]{});
        }

        return id.substring(prefix.length()).split("(?<=\\G...)");
    }

    /**
     * 生成下一级的id<br/>
     * 例如: 当前id为: Nb-001001, 则下一级的id为: Nb-001001001
     *
     * @param levels 当前id的层级
     *
     * @return 下一级的id
     */
    public String nextLevelId(List<Integer> levels) {
        levels.add(1);//下一级从1开始

        return generateId(levels);
    }

    public String get1LevelId(int level1) {
        List<Integer> levels = new ArrayList<>();
        levels.add(level1);

        return generateId(levels);
    }

    public String get2LevelId(int level1, int level2) {
        List<Integer> levels = new ArrayList<>();
        levels.add(level1);
        levels.add(level2);

        return generateId(levels);
    }

    public String get3LevelId(int level1, int level2, int level3) {
        List<Integer> levels = new ArrayList<>();
        levels.add(level1);
        levels.add(level2);
        levels.add(level3);

        return generateId(levels);
    }

    public String findTopLevel(String itemLevel) {
        return itemLevel.substring(0, 4);
    }

}
