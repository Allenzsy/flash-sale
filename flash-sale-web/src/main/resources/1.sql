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


-- ----------------------------
-- Table structure for trade_log
-- ----------------------------
DROP TABLE IF EXISTS `trade_log1kw`;
CREATE TABLE `trade_log1kw` (
  `nid`         varchar(20)     NOT NULL,
  `transtime`   varchar(14)     NOT NULL COMMENT '创建时间',
  `actionname`  varchar(100)    NOT NULL DEFAULT '' COMMENT '交易url',
  `userid`      varchar(17)     NOT NULL COMMENT '用户id',
  `storecode`   varchar(20)     NOT NULL COMMENT '门店编号',
  `bizcode`     varchar(20)     NOT NULL COMMENT '交易代码',
  `t1561code`   varchar(20)     NOT NULL COMMENT '商户编号',
  `deptcode`    varchar(20)     NOT NULL COMMENT '部门编号',
  `usertype`    varchar(2)      NOT NULL COMMENT '用户类型',
  `clientip`    varchar(60)     NOT NULL COMMENT '客户端ip',
  `mac`         varchar(60)     NOT NULL COMMENT 'mac地址',
  `zoneno`      varchar(100)    NOT NULL COMMENT '地区号',
  `appserverip` varchar(60)     NOT NULL COMMENT '服务器ip',
  `dateflag`    varchar(2)      NOT NULL COMMENT '数据分区',
  `errormsg`    varchar(400)    COMMENT '异常信息',
  `errorcode`   varchar(80)     COMMENT '异常代码',
  PRIMARY KEY (`nid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE tmp_table (
  `nid`         varchar(20)     NOT NULL,
  `transtime`   varchar(14)
);

/*
中括号[]中的内容可以省略，花括号{}中的内容是任选其一的，不同选项以竖线|分隔
*/

-- 以逗号分隔为一条数据插入到对应的字段
LOAD DATA LOCAL INFILE 'E:/python/data.txt' INTO TABLE tmp_table
FIELDS TERMINATED BY ',' (`nid`,`transtime`)
LINES TERMINATED BY '/r/n';

SELECT nid FROM tmp_table GROUP BY nid HAVING COUNT(1) > 1;


INSERT INTO
    trade_log
    SELECT
      `nid`,
      '00002022041224',
      'ownermsg/firstPageMsgs',
      '02006615265',
      '02006615265',
      '02006615265',
      '02006615265',
      '02006615265',
      '33',
      '122.147.13.63',
      '00-01-6C-06-A6-29',
      '0200',
      '127.0.0.1',
      '20',
      'success!',
      '0000'
    FROM
      tmp_table;


SELECT *
FROM trade_log d
WHERE d.nid > '202204x0000000968229'
-- AND d.order_time>'2020-8-5 00:00:00'
-- ORDER BY d.order_time LIMIT 6;
LIMIT 500;

SELECT `nid`,
    CONCAT(
    RPAD(`nid`,         20,     ' '),
    RPAD(`transtime`,   14,     ' '),
    RPAD(`actionname`,  100,    ' '),
    RPAD(`userid`,      17,     ' '),
    RPAD(`storecode`,   20,     ' '),
    RPAD(`bizcode`,     20,     ' '),
    RPAD(`t1561code`,   20,     ' '),
    RPAD(`deptcode`,    20,     ' '),
    RPAD(`usertype`,    2,      ' '),
    RPAD(`clientip`,    60,     ' '),
    RPAD(`mac`,         60,     ' '),
    RPAD(`zoneno`,      100,    ' '),
    RPAD(`appserverip`, 60,     ' '),
    RPAD(`dateflag`,    2,      ' '),
    RPAD(`errormsg`,    400,    ' '),
    RPAD(`errorcode`,   80,     ' '))
FROM trade_log t
WHERE `nid` > #{primaryKey}
LIMIT #{pageSize}



DROP TABLE IF EXISTS `trade_log1kw`;
CREATE TABLE `trade_log1kw` (
  `nid`         varchar(20)     NOT NULL,
  `transtime`   varchar(14)     NOT NULL COMMENT '创建时间',
  `actionname`  varchar(100)    NOT NULL DEFAULT '' COMMENT '交易url',
  `userid`      varchar(17)     NOT NULL COMMENT '用户id',
  `storecode`   varchar(20)     NOT NULL COMMENT '门店编号',
  `bizcode`     varchar(20)     NOT NULL COMMENT '交易代码',
  `t1561code`   varchar(20)     NOT NULL COMMENT '商户编号',
  `deptcode`    varchar(20)     NOT NULL COMMENT '部门编号',
  `usertype`    varchar(2)      NOT NULL COMMENT '用户类型',
  `clientip`    varchar(60)     NOT NULL COMMENT '客户端ip',
  `mac`         varchar(60)     NOT NULL COMMENT 'mac地址',
  `zoneno`      varchar(100)    NOT NULL COMMENT '地区号',
  `appserverip` varchar(60)     NOT NULL COMMENT '服务器ip',
  `dateflag`    varchar(2)      NOT NULL COMMENT '数据分区',
  `errormsg`    varchar(400)    COMMENT '异常信息',
  `errorcode`   varchar(80)     COMMENT '异常代码',
  PRIMARY KEY (`nid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 以逗号分隔为一条数据插入到对应的字段
LOAD DATA LOCAL INFILE 'E:/python/data.txt' INTO TABLE tmp_table;

INSERT INTO
    trade_log1kw
    SELECT
      `nid`,
      '00002022041224',
      'ownermsg/firstPageMsgs',
      '02006615265',
      '02006615265',
      '02006615265',
      '02006615265',
      '02006615265',
      '33',
      '122.147.13.63',
      '00-01-6C-06-A6-29',
      '0200',
      '127.0.0.1',
      '20',
      'success!',
      '0000'
    FROM
      tmp_table;


SELECT *
FROM trade_log d
WHERE d.nid > '202204x0000000968229'
-- AND d.order_time>'2020-8-5 00:00:00'
-- ORDER BY d.order_time LIMIT 6;
LIMIT 500;

SELECT *
FROM trade_log1kw d
WHERE d.nid > ''
-- AND d.order_time>'2020-8-5 00:00:00'
-- ORDER BY d.order_time LIMIT 6;
LIMIT 500;

SELECT *
FROM trade_log1kw d
LIMIT 9999999,1



SELECT *
FROM trade_log1KW d
WHERE d.nid > '202204x0000005007500' AND d.nid <= '202204x0000005008000'
-- AND d.order_time>'2020-8-5 00:00:00'
-- ORDER BY d.order_time LIMIT 6;
LIMIT 1000;