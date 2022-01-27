# 又创建了一个项目



项目创建参考：

> https://github.com/SymonLin/demo
>
> https://github.com/qqxx6661/miaosha





```mysql
-- ----------------------------
-- Table structure for stock_CAS
-- ----------------------------
DROP TABLE IF EXISTS `stock_CAS`;
CREATE TABLE `stock_CAS` (
  `id`      int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name`    varchar(50)      NOT NULL DEFAULT ''     COMMENT '名称',
  `count`   int(11)          NOT NULL                COMMENT '库存',
  `sale`    int(11)          NOT NULL 				 COMMENT '已售',
  `version` int(11)          NOT NULL  				 COMMENT '乐观锁，版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通过乐观锁解决超卖问题';
```



```mysql
-- ----------------------------
-- Table structure for stock_order
-- ----------------------------
DROP TABLE IF EXISTS `stock_order`;
CREATE TABLE `stock_order` (
  `id`          int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sid`         int(11)          NOT NULL COMMENT '库存ID',
  `name`        varchar(30)      NOT NULL DEFAULT '' COMMENT '商品名称',
  `create_time` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
```

