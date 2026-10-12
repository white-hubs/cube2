package com.guhecloud.platform.base.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/31 11:58
 * @Description:
 */
public class JsonUtilsTests {

    public static class User {

        private Integer id;
        private String name;
        private User user;

        public User() {

        }

        public User(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        //        public User(Integer id, String name, User user) {
        //            this.id = id;
        //            this.name = name;
        //            this.user = user;
        //        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void toJsonString() {
        User user = new User(1, "user1");
        User user2 = new User(2, "user2");
        user.setUser(user2);
        //单个对象序列化
        String singleJson = JsonUtils.serialize(user2);
        //集合对象序列化
        String listJson = JsonUtils.serialize(Collections.singletonList(user2));
        //序列化为Object
        Object objectJson = JsonUtils.serialize(user2);
        //反序列化为指定类
        User userObject = JsonUtils.deserialize(singleJson, User.class);
        //反序列化为指定类集合
        List<User> users = JsonUtils.deserializeToList(listJson, User.class);
        //反序列化复杂对象
        String complexJson = JsonUtils.serialize(Collections.singletonList(user));
        List<User> objects = JsonUtils.deserialize(complexJson, new TypeReference<List<User>>() {});
        //指定SerializerFeature序列化
        String jsonString = JsonUtils.serialize(user);

        //反序列化为ObjectMap
        JsonNode deserialize = JsonUtils.deserialize(jsonString);

        //获取指定json中key的值
        JsonNode name = JsonUtils.getNodeValue(jsonString, "name");
        //获取指定key值并转化为指定对象
        User user1 = JsonUtils.getNodeValue(jsonString, "user", User.class);
        //listObject转化为ListJavaObject
        List<User> users1 = JsonUtils.deserializeToList(complexJson, User.class);
    }

    // 测试基础对象序列化/反序列化
    @Test
    public void testBasicObjectSerialization() {
        User user = new User(1, "user1");
        String json = JsonUtils.serialize(user);
        User parsed = JsonUtils.deserialize(json, User.class);
        assertEquals(user.getId(), parsed.getId());
        assertEquals(user.getName(), parsed.getName());
    }

    // 测试对象嵌套序列化
    @Test
    public void testNestedObjectSerialization() {
        User user = new User(1, "parent");
        User child = new User(2, "child");
        user.setUser(child);

        String json = JsonUtils.serialize(user);
        User parsed = JsonUtils.deserialize(json, new TypeReference<User>() {});
        assertNotNull(parsed.getUser());
        assertEquals("child", parsed.getUser().getName());
    }

    // 测试集合序列化
    @Test
    public void testCollectionSerialization() {
        List<User> users = Arrays.asList(
                new User(1, "user1"),
                new User(2, "user2")
                                        );
        String json = JsonUtils.serialize(users);
        List<User> parsed = JsonUtils.deserializeToList(json, User.class);
        assertEquals(2, parsed.size());
    }

    // 测试复杂对象解析
    @Test
    public void testComplexObjectParsing() {
        User user = new User(1, "user1");
        user.setUser(new User(2, "user2"));
        String json = JsonUtils.serialize(Collections.singletonList(user));

        List<User> result = JsonUtils.deserialize(json, new TypeReference<List<User>>() {});
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getUser());
    }

    // 测试JSON路径取值
    @Test
    public void testJsonPathAccess() {
        String json = "{\"name\":\"test\",\"age\":\"20\"}";
        assertEquals("test", JsonUtils.getNodeStringValue(json, "name"));
        assertEquals("20", JsonUtils.getNodeStringValue(json, "age"));
    }

    // 测试json取值(包含子集)
    @Test
    public void testJsonNodeChildAccess() {
        String json = "{\"meta\":{\"user\":{\"name\":\"test\",\"profile\":{\"city\":\"shanghai\"}}},\"items\":[{\"id\":1},{\"detail\":{\"code\":\"A100\"}}]}";
        JsonNode root = JsonUtils.deserialize(json);

        JsonNode userNode = JsonUtils.getNodeLoopValue(root, "user");
        JsonNode profileNode = JsonUtils.getNodeLoopValue(userNode, "profile");
        JsonNode detailNode = JsonUtils.getNodeLoopValue(root, "detail");

        assertNotNull(userNode);
        assertNotNull(profileNode);
        assertNotNull(detailNode);
        assertEquals("test", JsonUtils.getNodeValue(userNode, "name"));
        assertEquals("shanghai", JsonUtils.getNodeValue(profileNode, "city"));
        assertEquals("A100", JsonUtils.getNodeLoopValue(root, "code").asText());
    }

    //流引擎复杂表单解析
    @Test
    public void testFormEngine() {
        String json = "{\"children\":{\"children\":{\"branchs\":[{\"children\":{\"children\":{\"children\":{},\"id\":\"node_378826587326\",\"incoming\":{},\"name\":\"维修结果确认\",\"parentId\":\"node_370266654836\",\"props\":{\"assignedType\":\"ROLE\",\"assignedUser\":[{\"product\":{\"name\":\"数智建筑综合运维管理系统\",\"id\":\"1528567156063617026\"},\"code\":\"\",\"name\":\"客服中心\",\"remark\":\"\",\"id\":\"1815256657590550530\",\"enabled\":true,\"selected\":false}],\"countersign\":true,\"deptPosition\":{\"position\":[],\"dept\":null},\"deptPost\":{\"post\":[],\"dept\":null},\"designatedApprover\":true,\"formPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"R\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"R\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"R\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"R\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"R\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"R\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"R\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"R\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"R\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"R\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"R\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"R\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"E\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"R\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"E\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"E\",\"required\":false,\"title\":\"评分\"}],\"formUser\":\"\",\"leader\":{\"level\":1},\"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\"mode\":\"OR\",\"nobody\":{\"handler\":\"TO_ADMIN\",\"assignedUser\":[]},\"operateAlias\":[{\"alias\":\"\",\"code\":\"PASS\",\"name\":\"同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"BACK\",\"name\":\"退回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"TRANSFER\",\"name\":\"转交\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REJECT\",\"name\":\"不同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"WITHDRAW\",\"name\":\"撤回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"COMMENT\",\"name\":\"评论\",\"perm\":\"S\"}],\"opinion\":false,\"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"role\":[{\"product\":{\"name\":\"数智建筑综合运维管理系统\",\"id\":\"1528567156063617026\"},\"code\":\"\",\"name\":\"客服中心\",\"remark\":\"\",\"id\":\"1815256657590550530\",\"enabled\":true,\"selected\":false}],\"selfSelect\":{\"typeForSelectRange\":{\"ROLE\":[],\"ALL\":[],\"ASSIGN_USER\":[]},\"type\":\"ALL\"},\"sign\":false,\"taskOperations\":[],\"timeLimit\":{\"handler\":{\"type\":\"REFUSE\",\"notify\":{\"hour\":1,\"once\":true}},\"timeout\":{\"unit\":\"H\",\"value\":0}},\"rejectStrategy\":\"TO_REJECT\",\"rejectStrategyNode\":[],\"outerFormPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"E\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"R\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"E\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"E\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"E\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"E\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"E\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"E\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"E\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"E\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"E\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"E\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"E\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"E\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"E\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"E\",\"required\":false,\"title\":\"评分\"}],\"post\":[],\"appointment\":[],\"single_appointment\":[],\"single_post\":[],\"signConfig\":{\"useLastSign\":false},\"limitTimeApproval\":false,\"limitTimeApprovalConfig\":{\"limitTimeNotify\":{\"checked\":false,\"approvalReminder\":[{\"unit\":\"DAY\",\"value\":null,\"notifyType\":\"currentApproval\",\"notifyId\":\"\"}]},\"autoPass\":{\"checked\":false,\"unit\":\"DAY\",\"value\":null}}},\"type\":\"APPROVAL\"},\"id\":\"node_370266654836\",\"incoming\":{},\"name\":\"工程维修\",\"parentId\":\"node_369769136737\",\"props\":{\"assignedType\":\"FORM_USER\",\"assignedUser\":[],\"countersign\":true,\"deptPosition\":{\"position\":[],\"dept\":null},\"deptPost\":{\"post\":[],\"dept\":null},\"designatedApprover\":true,\"formPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"R\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"R\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"R\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"R\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"R\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"E\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"R\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"R\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"R\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"E\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"E\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"E\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"E\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"E\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"H\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"H\",\"required\":false,\"title\":\"评分\"}],\"formUser\":\"field3684536643773\",\"leader\":{\"level\":1},\"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\"mode\":\"OR\",\"nobody\":{\"handler\":\"TO_ADMIN\",\"assignedUser\":[]},\"operateAlias\":[{\"alias\":\"\",\"code\":\"PASS\",\"name\":\"同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"BACK\",\"name\":\"退回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"TRANSFER\",\"name\":\"转交\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REJECT\",\"name\":\"不同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"WITHDRAW\",\"name\":\"撤回\",\"perm\":\"S\"},{\"alias\":\"反馈\",\"code\":\"COMMENT\",\"name\":\"评论\",\"perm\":\"S\"}],\"opinion\":false,\"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"role\":[],\"selfSelect\":{\"typeForSelectRange\":{\"ROLE\":[],\"ALL\":[],\"ASSIGN_USER\":[]},\"type\":\"ALL\"},\"sign\":false,\"taskOperations\":[],\"timeLimit\":{\"handler\":{\"type\":\"REFUSE\",\"notify\":{\"hour\":1,\"once\":true}},\"timeout\":{\"unit\":\"H\",\"value\":0}},\"rejectStrategy\":\"TO_REJECT\",\"rejectStrategyNode\":[],\"outerFormPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"E\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"R\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"E\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"E\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"E\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"E\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"E\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"E\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"E\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"E\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"E\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"E\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"E\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"E\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"E\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"E\",\"required\":false,\"title\":\"评分\"}],\"post\":[],\"appointment\":[],\"single_appointment\":[],\"single_post\":[],\"signConfig\":{\"useLastSign\":false},\"limitTimeApproval\":false,\"limitTimeApprovalConfig\":{\"limitTimeNotify\":{\"checked\":false,\"approvalReminder\":[{\"unit\":\"DAY\",\"value\":null,\"notifyType\":\"currentApproval\",\"notifyId\":\"\"}]},\"autoPass\":{\"checked\":false,\"unit\":\"DAY\",\"value\":null}}},\"type\":\"APPROVAL\"},\"id\":\"node_369769136737\",\"incoming\":{},\"name\":\"条件1\",\"parentId\":\"node_369769135402\",\"props\":{\"expression\":\"\",\"groups\":[{\"cids\":[\"field9997436570957\"],\"conditions\":[{\"compare\":\"IN\",\"id\":\"field9997436570957\",\"title\":\"故障类型\",\"value\":[\"工程-水电表维修\",\"工程-给排水\",\"工程-办公维修\",\"工程-区域维修\",\"工程-其他\",\"工程-维修\"],\"valueType\":\"String\"}],\"groupType\":\"OR\"}],\"groupsType\":\"OR\",\"leader\":{},\"leaderTop\":{},\"operateAlias\":[{\"alias\":\"\",\"code\":\"PASS\",\"name\":\"同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"BACK\",\"name\":\"退回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"TRANSFER\",\"name\":\"转交\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REJECT\",\"name\":\"驳回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"WITHDRAW\",\"name\":\"撤回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"COMMENT\",\"name\":\"评论\",\"perm\":\"H\"}]},\"type\":\"CONDITION\"},{\"children\":{\"children\":{\"children\":{},\"id\":\"node_246688584455\",\"incoming\":{},\"name\":\"抄送人\",\"parentId\":\"node_373533803034\",\"props\":{\"assignedType\":\"ROLE\",\"assignedUser\":[{\"product\":{\"name\":\"数智建筑综合运维管理系统\",\"id\":\"1528567156063617026\"},\"code\":\"\",\"name\":\"客服中心\",\"remark\":\"\",\"id\":\"1815256657590550530\",\"enabled\":true,\"selected\":false}],\"formPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"R\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"R\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field3673431574064\",\"perm\":\"R\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field4511365733185\",\"perm\":\"R\",\"required\":false,\"title\":\"回访备注\"}],\"leader\":{},\"leaderTop\":{},\"operateAlias\":[{\"alias\":\"\",\"code\":\"PASS\",\"name\":\"同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"BACK\",\"name\":\"退回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"TRANSFER\",\"name\":\"转交\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REJECT\",\"name\":\"不同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"WITHDRAW\",\"name\":\"撤回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"COMMENT\",\"name\":\"评论\",\"perm\":\"H\"}],\"role\":[{\"product\":{\"name\":\"数智建筑综合运维管理系统\",\"id\":\"1528567156063617026\"},\"code\":\"\",\"name\":\"客服中心\",\"remark\":\"\",\"id\":\"1815256657590550530\",\"enabled\":true,\"selected\":false}],\"shouldAdd\":false},\"type\":\"CC\"},\"id\":\"node_373533803034\",\"incoming\":{},\"name\":\"上级主管处理\",\"parentId\":\"node_369769134685\",\"props\":{\"assignedType\":\"FORM_USER\",\"assignedUser\":[],\"countersign\":true,\"deptPosition\":{\"position\":[],\"dept\":null},\"deptPost\":{\"post\":[],\"dept\":null},\"designatedApprover\":true,\"formPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"R\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"R\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"R\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"R\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"R\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"R\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"R\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"R\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"R\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"H\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"H\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"E\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"E\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"E\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"H\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"H\",\"required\":false,\"title\":\"评分\"}],\"formUser\":\"field3684536643773\",\"leader\":{\"level\":1},\"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\"mode\":\"AND\",\"nobody\":{\"handler\":\"TO_ADMIN\",\"assignedUser\":[]},\"operateAlias\":[{\"alias\":\"\",\"code\":\"PASS\",\"name\":\"同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"BACK\",\"name\":\"退回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"TRANSFER\",\"name\":\"转交\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REJECT\",\"name\":\"不同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"WITHDRAW\",\"name\":\"撤回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"COMMENT\",\"name\":\"评论\",\"perm\":\"S\"}],\"opinion\":true,\"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"role\":[],\"selfSelect\":{\"typeForSelectRange\":{\"ROLE\":[],\"ALL\":[],\"ASSIGN_USER\":[]},\"type\":\"ALL\"},\"sign\":false,\"taskOperations\":[],\"timeLimit\":{\"handler\":{\"type\":\"REFUSE\",\"notify\":{\"hour\":1,\"once\":true}},\"timeout\":{\"unit\":\"H\",\"value\":0}},\"rejectStrategy\":\"TO_REJECT\",\"rejectStrategyNode\":[],\"outerFormPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"E\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"R\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"E\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"E\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"E\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"E\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"E\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"E\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"E\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"E\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"E\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"E\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"E\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"E\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"E\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"E\",\"required\":false,\"title\":\"评分\"}],\"post\":[],\"appointment\":[],\"single_appointment\":[],\"single_post\":[],\"signConfig\":{\"useLastSign\":false},\"limitTimeApproval\":false,\"limitTimeApprovalConfig\":{\"limitTimeNotify\":{\"checked\":false,\"approvalReminder\":[{\"unit\":\"DAY\",\"value\":null,\"notifyType\":\"currentApproval\",\"notifyId\":\"\"}]},\"autoPass\":{\"checked\":false,\"unit\":\"DAY\",\"value\":null}}},\"type\":\"APPROVAL\"},\"id\":\"node_369769134685\",\"incoming\":{},\"name\":\"条件2\",\"parentId\":\"node_369769135402\",\"props\":{\"expression\":\"\",\"groups\":[{\"cids\":[\"field9997436570957\"],\"conditions\":[{\"compare\":\"=\",\"id\":\"field9997436570957\",\"title\":\"故障类型\",\"value\":[\"其他\"],\"valueType\":\"String\"}],\"groupType\":\"AND\"}],\"groupsType\":\"OR\",\"leader\":{},\"leaderTop\":{},\"operateAlias\":[{\"alias\":\"\",\"code\":\"PASS\",\"name\":\"同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"BACK\",\"name\":\"退回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"TRANSFER\",\"name\":\"转交\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REJECT\",\"name\":\"驳回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"WITHDRAW\",\"name\":\"撤回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"COMMENT\",\"name\":\"评论\",\"perm\":\"H\"}]},\"type\":\"CONDITION\"},{\"children\":{\"children\":{\"children\":{},\"id\":\"node_962272724421\",\"incoming\":{},\"name\":\"维修结果确认\",\"parentId\":\"node_957918654378\",\"props\":{\"assignedType\":\"ROLE\",\"assignedUser\":[{\"product\":{\"name\":\"数智建筑综合运维管理系统\",\"id\":\"1528567156063617026\"},\"code\":\"\",\"name\":\"客服中心\",\"remark\":\"报修或投诉工单处理和查看\",\"id\":\"1815256657590550530\",\"enabled\":true,\"selected\":false}],\"countersign\":true,\"deptPosition\":{\"position\":[],\"dept\":null},\"deptPost\":{\"post\":[],\"dept\":null},\"designatedApprover\":true,\"formPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"E\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"E\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field1898536488775\",\"perm\":\"E\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"E\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"E\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"E\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"E\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"E\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"E\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"E\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"E\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"E\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"E\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"E\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"E\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"E\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"E\",\"required\":false,\"title\":\"评分\"}],\"formUser\":\"\",\"leader\":{\"level\":1},\"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\"mode\":\"OR\",\"nobody\":{\"handler\":\"TO_ADMIN\",\"assignedUser\":[]},\"operateAlias\":[{\"alias\":\"\",\"code\":\"PASS\",\"name\":\"同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"BACK\",\"name\":\"退回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"TRANSFER\",\"name\":\"转交\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REJECT\",\"name\":\"不同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"WITHDRAW\",\"name\":\"撤回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"COMMENT\",\"name\":\"评论\",\"perm\":\"S\"}],\"opinion\":false,\"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"role\":[{\"product\":{\"name\":\"数智建筑综合运维管理系统\",\"id\":\"1528567156063617026\"},\"code\":\"\",\"name\":\"客服中心\",\"remark\":\"报修或投诉工单处理和查看\",\"id\":\"1815256657590550530\",\"enabled\":true,\"selected\":false}],\"selfSelect\":{\"typeForSelectRange\":{\"ROLE\":[],\"ALL\":[],\"ASSIGN_USER\":[]},\"type\":\"ALL\"},\"sign\":false,\"taskOperations\":[],\"timeLimit\":{\"handler\":{\"type\":\"REFUSE\",\"notify\":{\"hour\":1,\"once\":true}},\"timeout\":{\"unit\":\"H\",\"value\":0}},\"rejectStrategy\":\"TO_REJECT\",\"rejectStrategyNode\":[],\"outerFormPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"E\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"E\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field1898536488775\",\"perm\":\"E\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"E\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"E\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"E\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"E\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"E\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"E\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"E\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"E\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"E\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"E\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"E\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"E\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"E\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"E\",\"required\":false,\"title\":\"评分\"}],\"post\":[],\"appointment\":[],\"single_appointment\":[],\"single_post\":[],\"signConfig\":{\"useLastSign\":false},\"limitTimeApproval\":false,\"limitTimeApprovalConfig\":{\"limitTimeNotify\":{\"checked\":false,\"approvalReminder\":[{\"unit\":\"DAY\",\"value\":null,\"notifyType\":\"currentApproval\",\"notifyId\":\"\"}]},\"autoPass\":{\"checked\":false,\"unit\":\"DAY\",\"value\":null}}},\"type\":\"APPROVAL\"},\"id\":\"node_957918654378\",\"incoming\":{},\"name\":\"技术处理中\",\"parentId\":\"node_957769785326\",\"props\":{\"assignedType\":\"ASSIGN_USER\",\"assignedUser\":[{\"gender\":\"MALE\",\"idCard\":\"\",\"roles\":[{\"name\":\"第三方运维巡检\",\"id\":\"1888789022614552577\"},{\"name\":\"部门考勤管理员\",\"id\":\"1813474344623996930\"},{\"name\":\"员工\",\"id\":\"1797526231077896193\"}],\"appointmentList\":[{\"enable\":true,\"name\":\"信息维护\",\"remark\":\"\",\"updateTime\":1735521392000,\"id\":\"1872549033104437250\"}],\"admin\":false,\"remark\":\"\",\"enabled\":true,\"photographList\":[],\"password\":\"{bcrypt}$2a$10$qCVl0UAYBhIvCRuXjgXP..nj432slKJDXelkouu.GHscvVmJUv6nC\",\"id\":\"1889565453573488641\",\"email\":\"\",\"managedOrganizations\":[],\"selected\":false,\"workStatus\":\"WORKING\",\"tenants\":[],\"mobile\":\"19828960736\",\"positions\":[{\"name\":\"信息专员\",\"id\":\"1796032402810224642\"}],\"appointmentIds\":[],\"lastLoginTime\":1748702270000,\"createTime\":1739342485000,\"organization\":{\"name\":\"信息弱电维保\",\"id\":\"1795771658495606786\"},\"name\":\"张韵\",\"organizations\":[{\"name\":\"信息弱电维保\",\"id\":\"1795771658495606786\"}],\"username\":\"19828960736\"}],\"countersign\":true,\"deptPosition\":{\"position\":[],\"dept\":null},\"deptPost\":{\"post\":[],\"dept\":null},\"designatedApprover\":true,\"formPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"E\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"E\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field1898536488775\",\"perm\":\"E\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"E\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"E\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"E\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"E\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"E\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"E\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"E\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"E\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"E\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"E\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"E\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"E\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"E\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"E\",\"required\":false,\"title\":\"评分\"}],\"formUser\":\"field3684536643773\",\"leader\":{\"level\":1},\"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\"mode\":\"OR\",\"nobody\":{\"handler\":\"TO_ADMIN\",\"assignedUser\":[]},\"operateAlias\":[{\"alias\":\"\",\"code\":\"PASS\",\"name\":\"同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"BACK\",\"name\":\"退回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"TRANSFER\",\"name\":\"转交\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REJECT\",\"name\":\"不同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"WITHDRAW\",\"name\":\"撤回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"COMMENT\",\"name\":\"评论\",\"perm\":\"S\"}],\"opinion\":false,\"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"role\":[],\"selfSelect\":{\"typeForSelectRange\":{\"ROLE\":[],\"ALL\":[],\"ASSIGN_USER\":[]},\"type\":\"ALL\"},\"sign\":false,\"taskOperations\":[],\"timeLimit\":{\"handler\":{\"type\":\"REFUSE\",\"notify\":{\"hour\":1,\"once\":true}},\"timeout\":{\"unit\":\"H\",\"value\":0}},\"rejectStrategy\":\"TO_REJECT\",\"rejectStrategyNode\":[],\"outerFormPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"E\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"E\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field1898536488775\",\"perm\":\"E\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"E\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"E\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"E\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"E\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"E\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"E\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"E\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"E\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"E\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"E\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"E\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"E\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"E\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"E\",\"required\":false,\"title\":\"评分\"}],\"post\":[],\"appointment\":[],\"single_appointment\":[],\"single_post\":[],\"signConfig\":{\"useLastSign\":false},\"limitTimeApproval\":false,\"limitTimeApprovalConfig\":{\"limitTimeNotify\":{\"checked\":false,\"approvalReminder\":[{\"unit\":\"DAY\",\"value\":null,\"notifyType\":\"currentApproval\",\"notifyId\":\"\"}]},\"autoPass\":{\"checked\":false,\"unit\":\"DAY\",\"value\":null}}},\"type\":\"APPROVAL\"},\"id\":\"node_957769785326\",\"incoming\":{},\"name\":\"条件3\",\"parentId\":\"node_369769135402\",\"props\":{\"expression\":\"\",\"groups\":[{\"cids\":[\"field9997436570957\"],\"conditions\":[{\"compare\":\"IN\",\"id\":\"field9997436570957\",\"title\":\"故障类型\",\"value\":[\"技术-网络软硬件\"],\"valueType\":\"String\"}],\"groupType\":\"AND\"}],\"groupsType\":\"OR\",\"leader\":{},\"leaderTop\":{},\"operateAlias\":[{\"alias\":\"\",\"code\":\"PASS\",\"name\":\"同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"BACK\",\"name\":\"退回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"TRANSFER\",\"name\":\"转交\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REJECT\",\"name\":\"驳回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"WITHDRAW\",\"name\":\"撤回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"COMMENT\",\"name\":\"评论\",\"perm\":\"H\"}]},\"type\":\"CONDITION\"}],\"children\":{\"children\":{},\"id\":\"node_369769133464\",\"incoming\":{},\"parentId\":\"node_369769135402\",\"type\":\"EMPTY\"},\"id\":\"node_369769135402\",\"incoming\":{},\"name\":\"条件分支\",\"parentId\":\"node_370361947870\",\"props\":{\"leader\":{},\"leaderTop\":{},\"operateAlias\":[{\"alias\":\"\",\"code\":\"PASS\",\"name\":\"同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"BACK\",\"name\":\"退回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"TRANSFER\",\"name\":\"转交\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REJECT\",\"name\":\"驳回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"WITHDRAW\",\"name\":\"撤回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"COMMENT\",\"name\":\"评论\",\"perm\":\"H\"}]},\"type\":\"CONDITIONS\"},\"id\":\"node_370361947870\",\"incoming\":{},\"name\":\"客服确认\",\"parentId\":\"root\",\"props\":{\"assignedType\":\"ROLE\",\"assignedUser\":[{\"product\":{\"name\":\"数智建筑综合运维管理系统\",\"id\":\"1528567156063617026\"},\"code\":\"\",\"name\":\"客服中心\",\"remark\":\"\",\"id\":\"1815256657590550530\",\"enabled\":true,\"selected\":false}],\"countersign\":true,\"deptPosition\":{\"position\":[],\"dept\":null},\"deptPost\":{\"post\":[],\"dept\":null},\"designatedApprover\":true,\"formPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"R\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"R\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"R\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"R\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"R\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"E\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"E\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"E\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"E\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"H\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"H\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"H\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"H\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"H\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"H\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"H\",\"required\":false,\"title\":\"评分\"}],\"formUser\":\"\",\"leader\":{\"level\":1},\"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\"mode\":\"OR\",\"nobody\":{\"handler\":\"TO_ADMIN\",\"assignedUser\":[]},\"operateAlias\":[{\"alias\":\"\",\"code\":\"PASS\",\"name\":\"同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"BACK\",\"name\":\"退回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"TRANSFER\",\"name\":\"转交\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REJECT\",\"name\":\"不同意\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"WITHDRAW\",\"name\":\"撤回\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"COMMENT\",\"name\":\"评论\",\"perm\":\"S\"}],\"opinion\":false,\"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"role\":[{\"product\":{\"name\":\"数智建筑综合运维管理系统\",\"id\":\"1528567156063617026\"},\"code\":\"\",\"name\":\"客服中心\",\"remark\":\"\",\"id\":\"1815256657590550530\",\"enabled\":true,\"selected\":false}],\"selfSelect\":{\"typeForSelectRange\":{\"ROLE\":[],\"ALL\":[],\"ASSIGN_USER\":[]},\"type\":\"ALL\"},\"sign\":false,\"taskOperations\":[],\"timeLimit\":{\"handler\":{\"type\":\"REFUSE\",\"notify\":{\"hour\":1,\"once\":true}},\"timeout\":{\"unit\":\"H\",\"value\":0}},\"rejectStrategy\":\"TO_REJECT\",\"rejectStrategyNode\":[],\"outerFormPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"E\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"R\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"E\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"E\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"E\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"E\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"E\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"E\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"E\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"E\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"E\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"E\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"E\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"E\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"E\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"E\",\"required\":false,\"title\":\"评分\"}],\"post\":[],\"appointment\":[],\"single_appointment\":[],\"single_post\":[],\"signConfig\":{\"useLastSign\":false},\"limitTimeApproval\":false,\"limitTimeApprovalConfig\":{\"limitTimeNotify\":{\"checked\":false,\"approvalReminder\":[{\"unit\":\"DAY\",\"value\":null,\"notifyType\":\"currentApproval\",\"notifyId\":\"\"}]},\"autoPass\":{\"checked\":false,\"unit\":\"DAY\",\"value\":null}}},\"type\":\"APPROVAL\"},\"desc\":\"任何人\",\"id\":\"root\",\"incoming\":{},\"name\":\"发起人\",\"props\":{\"assignedUser\":[],\"formPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"H\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"E\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"E\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"E\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"E\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"H\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"H\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"H\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"H\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"H\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"H\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"H\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"H\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"H\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"H\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"H\",\"required\":false,\"title\":\"评分\"}],\"leader\":{},\"leaderTop\":{},\"operateAlias\":[{\"alias\":\"\",\"code\":\"ADD\",\"name\":\"发起申请\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"REPEAT\",\"name\":\"再次发起\",\"perm\":\"S\"},{\"alias\":\"\",\"code\":\"UNDO\",\"name\":\"撤销\",\"perm\":\"S\"}],\"outerFormPerms\":[{\"id\":\"field4047818707720\",\"perm\":\"E\",\"required\":true,\"title\":\"信息\"},{\"id\":\"field2116570946737\",\"perm\":\"E\",\"required\":true,\"title\":\"维修内容\"},{\"id\":\"field5443436520692\",\"perm\":\"E\",\"required\":true,\"title\":\"区域或位置\"},{\"id\":\"field3860136579123\",\"perm\":\"E\",\"required\":false,\"title\":\"照片或视频\"},{\"id\":\"field3673431574064\",\"perm\":\"E\",\"required\":false,\"title\":\"说明文字\"},{\"id\":\"field9997436570957\",\"perm\":\"H\",\"required\":true,\"title\":\"故障类型\"},{\"id\":\"field3035136574223\",\"perm\":\"H\",\"required\":true,\"title\":\"紧急程度\"},{\"id\":\"field2299736574925\",\"perm\":\"H\",\"required\":true,\"title\":\"故障确认\"},{\"id\":\"field3684536643773\",\"perm\":\"H\",\"required\":true,\"title\":\"处理人\"},{\"id\":\"field2070036667971\",\"perm\":\"H\",\"required\":true,\"title\":\"用料类型\"},{\"id\":\"field2052336804440\",\"perm\":\"H\",\"required\":false,\"title\":\"用料金额\"},{\"id\":\"field9775537572256\",\"perm\":\"H\",\"required\":false,\"title\":\"维修说明\"},{\"id\":\"field2150536575874\",\"perm\":\"H\",\"required\":true,\"title\":\"解决结果\"},{\"id\":\"field3140660054632\",\"perm\":\"H\",\"required\":false,\"title\":\"解决后照片或视频\"},{\"id\":\"field4511365733185\",\"perm\":\"H\",\"required\":false,\"title\":\"回访备注\"},{\"id\":\"field5185138428859\",\"perm\":\"H\",\"required\":false,\"title\":\"评分\"}]},\"type\":\"ROOT\"}";
        JsonNode jsonNode = JsonUtils.deserialize(json);
        JsonNode jsonNode1 = jsonNode.get("children");
        JsonNode jsonNode2 = jsonNode1.get("props");
        JsonNode id = jsonNode1.get("id");
        String string = id.toString();
        JsonNode jsonNode3 = jsonNode2.get("formPerms");
        JsonNode jsonNode5 = jsonNode3.get(jsonNode3.size() - 1);
        String serialize = JsonUtils.serialize(jsonNode5);
        //获取jasonArry中的值
        JsonNode jsonNode4 = jsonNode3.get(1);
        //jsonMap转指定Java对象
        FormPermsDTO formPermsDTO = JsonUtils.deserialize(jsonNode4, FormPermsDTO.class);
        Map<String, Object> formPermsDTO2 = JsonUtils.deserialize(jsonNode1, "props", new TypeReference<Map<String, Object>>() {});
        assertNotNull(formPermsDTO);
        //jsonArry转指定Java对象
        List<FormPermsDTO> formPermsDTOS = JsonUtils.deserializeToList(jsonNode2, "formPerms", FormPermsDTO.class);
        assertNotNull(formPermsDTOS);

    }

    public static class FormPermsDTO {
        private String id;
        /**
         * 是否必填：E=编辑
         */
        private String perm;

        public FormPermsDTO() {
        }

        public FormPermsDTO(String id, String perm, Boolean required, String title) {
            this.id = id;
            this.perm = perm;
            this.required = required;
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPerm() {
            return perm;
        }

        public void setPerm(String perm) {
            this.perm = perm;
        }

        public Boolean getRequired() {
            return required;
        }

        public void setRequired(Boolean required) {
            this.required = required;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * 是否必填
         */
        private Boolean required;
        private String title;
    }

}
