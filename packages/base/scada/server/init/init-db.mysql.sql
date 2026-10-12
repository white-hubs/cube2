CREATE DATABASE IF NOT EXISTS `kaihe-scada` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `kaihe-scada`;
-- `kaihe-scada`.ghd_category definition

CREATE TABLE IF NOT EXISTS `ghd_category` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `ParentID` int(11) NOT NULL,
  `projectId` int(11) DEFAULT '0',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='组态设计图分类';


-- `kaihe-scada`.ghd_diagram definition

CREATE TABLE IF NOT EXISTS `ghd_diagram` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) NOT NULL,
  `Description` varchar(255) NOT NULL,
  `Public` bit(1) NOT NULL,
  `CreatedDate` datetime NOT NULL,
  `LastUpdate` datetime NOT NULL,
  `Size` int(10) unsigned DEFAULT NULL,
  `Category` int(11) NOT NULL COMMENT '图纸分类',
  `projectId` int(11) DEFAULT NULL,
  `menuId` int(11) DEFAULT NULL,
  `areaId` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='WEB设计器专用';


-- `kaihe-scada`.ghd_diagramdata definition

CREATE TABLE IF NOT EXISTS `ghd_diagramdata` (
  `DiagramID` int(11) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `FileName` varchar(255) DEFAULT NULL,
  `FileSize` int(10) unsigned DEFAULT NULL,
  `LastUpdate` datetime NOT NULL,
  `projectId` int(11) DEFAULT NULL,
  PRIMARY KEY (`DiagramID`,`TYPE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='WEB设计器专用';


-- `kaihe-scada`.ghd_equipment definition

CREATE TABLE IF NOT EXISTS `ghd_equipment` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GUID` varchar(50) DEFAULT NULL,
  `EquipmentName` varchar(100) DEFAULT NULL COMMENT '设备名称',
  `Address` varchar(200) DEFAULT NULL COMMENT '安装地址',
  `Period` int(11) DEFAULT NULL COMMENT '周期',
  `Producer` varchar(100) DEFAULT NULL COMMENT '生产商',
  `ProduceDate` datetime DEFAULT NULL COMMENT '生产日期',
  `InstallDate` datetime DEFAULT NULL COMMENT '安装时间',
  `Unit` varchar(10) DEFAULT NULL COMMENT '设备单位',
  `Quantity` int(11) DEFAULT NULL COMMENT '设备数量',
  `Model` varchar(50) DEFAULT NULL COMMENT '设备型号',
  `CategoryID` int(11) DEFAULT NULL COMMENT '分类id',
  `Description` varchar(255) DEFAULT NULL,
  `AreaID` int(11) DEFAULT NULL COMMENT '区域id',
  `Status` smallint(6) DEFAULT NULL COMMENT '设备状态',
  `DeptID` int(11) DEFAULT NULL COMMENT '部门id',
  `Operator` varchar(20) DEFAULT '' COMMENT '操作人员',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `设备插入唯一性` (`EquipmentName`,`Address`,`Model`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='设备表';


-- `kaihe-scada`.ghd_figurefunction definition

CREATE TABLE IF NOT EXISTS `ghd_figurefunction` (
  `ID` int(11) NOT NULL,
  `FunctionName` varchar(255) DEFAULT NULL,
  `FunctionDesc` varchar(255) DEFAULT NULL,
  `projectId` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;


-- `kaihe-scada`.ghd_figurekind definition

CREATE TABLE IF NOT EXISTS `ghd_figurekind` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `KindName` varchar(255) NOT NULL,
  `FolderName` varchar(255) NOT NULL,
  `ParentID` int(11) DEFAULT NULL,
  `Sort` int(11) unsigned zerofill DEFAULT NULL,
  `CreateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `projectId` int(11) DEFAULT NULL,
  `Public` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;


-- `kaihe-scada`.ghd_figures definition

CREATE TABLE IF NOT EXISTS `ghd_figures` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FigureKindID` int(11) NOT NULL COMMENT '图元的分类ID(与图形的文件夹对应)',
  `FigureName` varchar(255) DEFAULT NULL COMMENT '图元的名称',
  `FigureFunctionID` int(11) DEFAULT NULL COMMENT '图元的功能ID（与创建图元的函数名对应）',
  `FileName` varchar(255) NOT NULL COMMENT '图元的文件名称',
  `ImageName` varchar(255) DEFAULT NULL COMMENT '图元缩略图文件名',
  `Sort` int(11) unsigned zerofill DEFAULT NULL COMMENT '排序',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Public` bit(1) DEFAULT NULL,
  `projectId` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1072 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;


-- `kaihe-scada`.ghops_project definition

CREATE TABLE IF NOT EXISTS `ghops_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `projectAdminId` int(11) NOT NULL,
  `code` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `area` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `buildingType` int(11) DEFAULT NULL,
  `dataFlag` int(11) DEFAULT NULL,
  `imgPath` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `coordinate` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `areaSize` float(10,2) DEFAULT NULL,
  `remove` bit(1) DEFAULT NULL,
  `title` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `video` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;


-- `kaihe-scada`.ghvideo_cam definition

CREATE TABLE IF NOT EXISTS `ghvideo_cam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `camToken` varchar(50) NOT NULL,
  `serverId` int(11) NOT NULL,
  `areaId` int(11) DEFAULT NULL,
  `strName` varchar(100) DEFAULT NULL,
  `strUrl` varchar(500) DEFAULT NULL,
  `strUser` varchar(20) DEFAULT NULL,
  `strPasswd` varchar(20) DEFAULT NULL,
  `strSrcIpAddress` varchar(20) DEFAULT NULL,
  `strSrcPort` int(11) DEFAULT NULL,
  `nType` varchar(20) DEFAULT NULL,
  `lating` varchar(255) DEFAULT NULL,
  `ptz` bit(1) DEFAULT NULL,
  `menuId` int(11) DEFAULT NULL,
  `deviceType` int(11) DEFAULT NULL,
  `projectId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='摄像机';


-- `kaihe-scada`.ghvideo_server definition

CREATE TABLE IF NOT EXISTS `ghvideo_server` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serverName` varchar(25) NOT NULL,
  `port` varchar(10) NOT NULL,
  `ip` varchar(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `session` varchar(50) DEFAULT NULL,
  `projectId` int(11) NOT NULL,
  `deviceType` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;


-- `kaihe-scada`.sys_config definition

CREATE TABLE IF NOT EXISTS `sys_config` (
  `config_id` varchar(32) NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(1024) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(32) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='参数配置表';


INSERT IGNORE INTO `kaihe-scada`.ghd_figurefunction (ID, FunctionName, FunctionDesc, projectId) VALUES(1, 'SvgFrame', 'SVG图元', NULL);
INSERT IGNORE INTO `kaihe-scada`.ghd_figurefunction (ID, FunctionName, FunctionDesc, projectId) VALUES(2, 'ImageFrame', '图片图元', NULL);

INSERT INTO `kaihe-scada`.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) VALUES('iotappkeypath', 'iot平台key', 'iot.app.key.path', 'YWRtaW4=', 'N', '1', '2020-12-11 11:11:35', '1', '2020-12-11 11:11:40', '1')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `kaihe-scada`.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) VALUES('iotappsecretpath', 'iot平台密钥', 'iot.app.secret.path', '561bda082d5fe7397c36668289cfb53ac91d0b0b', 'N', '1', '2020-12-11 11:11:35', '1', '2020-12-11 11:11:40', '1')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `kaihe-scada`.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) VALUES('iotbaseurl', 'iot平台基础地址', 'iot.base.url', 'http://127.0.0.1:9094', 'N', '1', '2020-12-11 11:11:35', '1', '2020-12-11 11:11:40', '1')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `kaihe-scada`.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) VALUES('iotbaseurlw', 'iot平台基础地址-无协议', 'iot.base.url.w', 'http://127.0.0.1:9094', 'N', '1', '2020-02-23 11:11:35', '1', '2020-02-23 11:11:35', '1')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `kaihe-scada`.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) VALUES('iotdevicelistpath', 'iot设备列表路径', 'iot.device.list.path', '/Iot/getDeviceListByProductModel', 'N', '1', '2020-12-11 15:24:35', '1', '2020-12-11 15:24:40', '1')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `kaihe-scada`.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) VALUES('iotdevicemodel', 'iot平台查询设备型号的可控属性路径', 'iot.device.model.property.path', '/Iot/devices', 'N', '1', '2020-04-23 10:54:49', '', NULL, '')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `kaihe-scada`.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) VALUES('iotdevicepropertypath', 'iot平台查询资产设备多个属性路径', 'iot.device.property.path', '/Iot/devices/properties', 'N', '1', '2020-12-15 10:11:35', '1', '2020-12-15 10:11:40', '1')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `kaihe-scada`.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) VALUES('iotproductlistpath', 'iot平台产品列表路径', 'iot.product.list.path', '/Iot/getProductListByProjectId', 'N', '1', '2020-12-11 11:11:35', '1', '2020-12-11 11:11:40', '1')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `kaihe-scada`.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) VALUES('iotproductmodellistpath', 'iot平台产品型号路径', 'iot.product.model.list.path', '/Iot/getProductModelListByProductId', 'N', '1', '2020-12-11 15:11:35', '1', '2020-12-11 15:11:40', '1')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `kaihe-scada`.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) VALUES('iotprojectid', 'iot平台项目ID', 'iot.project.id', '1597415189647486977', 'N', '1', '2021-01-14 11:59:33', '', '2021-01-14 11:59:35', '11')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `kaihe-scada`.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) VALUES('iotprojectlistpath', 'iot平台项目列表路径', 'iot.project.list.path', '/Iot/getProjectList', 'N', '1', '2020-12-11 14:11:35', '1', '2020-12-11 14:11:40', '1')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
