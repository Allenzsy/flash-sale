-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` 		int(11) unsigned	NOT NULL AUTO_INCREMENT	COMMENT '自增id',
  `name` 	varchar(50) 		NOT NULL DEFAULT '' 	COMMENT '名称',
  `count` 	int(11) 			NOT NULL 				COMMENT '库存, 乐观锁可复用此字段避免新增版本号字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通过乐观锁解决超卖问题';

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id`          int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pid`         int(11)          NOT NULL COMMENT '商品ID',
  `name`        varchar(30)      NOT NULL DEFAULT '' COMMENT '商品名称',
  `create_time` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;