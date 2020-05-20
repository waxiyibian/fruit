-- MySQL dump 10.16  Distrib 10.1.38-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: fruit
-- ------------------------------------------------------
-- Server version	10.1.38-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `content` varchar(512) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `store_id` bigint(20) NOT NULL COMMENT '店铺ID',
  PRIMARY KEY (`id`),
  KEY `FKgftr17kf5cy5590wj4r4taats` (`user_id`),
  KEY `FKb3wx3nlqxq395vlyqyxm6o20p` (`order_id`),
  CONSTRAINT `FKb3wx3nlqxq395vlyqyxm6o20p` FOREIGN KEY (`order_id`) REFERENCES `order_master` (`order_id`),
  CONSTRAINT `FKgftr17kf5cy5590wj4r4taats` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (54,2,'123','2020-05-07 20:55:16','2020-05-07 20:55:16',48,1);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_store`
--

DROP TABLE IF EXISTS `delivery_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delivery_store` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id，用户类型为配送员',
  `store_id` bigint(20) NOT NULL COMMENT '商户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `store_info_id` bigint(20) DEFAULT NULL,
  `sys_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`store_id`),
  KEY `FKq70dmxc0d27kso86oxnugqvmy` (`store_info_id`),
  KEY `FKsasqk23dhi50u0to07taiegfk` (`sys_user_id`),
  CONSTRAINT `FKq70dmxc0d27kso86oxnugqvmy` FOREIGN KEY (`store_info_id`) REFERENCES `store_info` (`id`),
  CONSTRAINT `FKsasqk23dhi50u0to07taiegfk` FOREIGN KEY (`sys_user_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_store`
--

LOCK TABLES `delivery_store` WRITE;
/*!40000 ALTER TABLE `delivery_store` DISABLE KEYS */;
INSERT INTO `delivery_store` VALUES (21,1,NULL,1,21),(21,58,'2020-05-09 09:49:20',58,21);
/*!40000 ALTER TABLE `delivery_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (64),(64);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_detail` (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `product_name` varchar(64) DEFAULT NULL,
  `product_price` decimal(8,2) DEFAULT NULL,
  `product_quantity` int(11) DEFAULT NULL,
  `product_icon` varchar(512) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`detail_id`),
  KEY `FK4qtlmqkm0mvtqu3cy4x9reobx` (`order_id`),
  CONSTRAINT `FK4qtlmqkm0mvtqu3cy4x9reobx` FOREIGN KEY (`order_id`) REFERENCES `order_master` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (1,1,16,'皇冠密梨',10.00,2,'http://localhost:8080/upload/d953da35-4283-4e19-a6fc-b532c1eb76f6code.png','2020-03-22 23:49:42','2020-03-22 23:49:42'),(2,1,19,'迷hotel',23.00,1,'http://localhost:8080/upload/77f694b4-9a3c-46aa-9ff4-791c6968965bt2.jpg','2020-03-22 23:50:22','2020-03-22 23:50:22'),(44,44,16,'皇冠密梨',10.00,2,'http://localhost:8080/upload/d953da35-4283-4e19-a6fc-b532c1eb76f6code.png','2020-05-06 20:53:02','2020-05-06 20:53:02'),(45,45,16,'皇冠密梨',10.00,1,'http://localhost:8080/upload/d953da35-4283-4e19-a6fc-b532c1eb76f6code.png','2020-05-06 21:00:08','2020-05-06 21:00:08'),(46,45,19,'迷hotel',34.00,2,'http://localhost:8080/upload/77f694b4-9a3c-46aa-9ff4-791c6968965bt2.jpg','2020-05-06 21:00:08','2020-05-06 21:00:08'),(47,46,16,'皇冠密梨',10.00,1,'http://localhost:8080/upload/d953da35-4283-4e19-a6fc-b532c1eb76f6code.png','2020-05-06 21:01:17','2020-05-06 21:01:17'),(48,46,19,'迷hotel',34.00,1,'http://localhost:8080/upload/77f694b4-9a3c-46aa-9ff4-791c6968965bt2.jpg','2020-05-06 21:01:17','2020-05-06 21:01:17'),(49,47,19,'迷hotel',34.00,3,'http://localhost:8080/upload/77f694b4-9a3c-46aa-9ff4-791c6968965bt2.jpg','2020-05-06 21:03:22','2020-05-06 21:03:22'),(50,48,16,'皇冠密梨',10.00,3,'http://localhost:8080/upload/d953da35-4283-4e19-a6fc-b532c1eb76f6code.png','2020-05-06 21:03:34','2020-05-06 21:03:34'),(51,48,19,'迷hotel',34.00,3,'http://localhost:8080/upload/77f694b4-9a3c-46aa-9ff4-791c6968965bt2.jpg','2020-05-06 21:03:34','2020-05-06 21:03:34'),(52,49,16,'皇冠密梨',10.00,2,'http://localhost:8080/upload/d953da35-4283-4e19-a6fc-b532c1eb76f6code.png','2020-05-06 21:03:43','2020-05-06 21:03:43'),(53,49,19,'迷hotel',34.00,1,'http://localhost:8080/upload/77f694b4-9a3c-46aa-9ff4-791c6968965bt2.jpg','2020-05-06 21:03:43','2020-05-06 21:03:43');
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_master`
--

DROP TABLE IF EXISTS `order_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_master` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `buyer_name` varchar(32) NOT NULL COMMENT '订单中的收货人',
  `buyer_phone` varchar(32) NOT NULL COMMENT '订单中的联系电话',
  `buyer_address` varchar(255) NOT NULL COMMENT '订单中的收货地址',
  `buyer_id` bigint(20) NOT NULL COMMENT '购买账号的id',
  `order_amount` decimal(8,2) NOT NULL COMMENT '订单金额',
  `order_status` int(11) DEFAULT '0' COMMENT '0新建1支付2接单3配送4完成5评价6取消',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `store_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_master`
--

LOCK TABLES `order_master` WRITE;
/*!40000 ALTER TABLE `order_master` DISABLE KEYS */;
INSERT INTO `order_master` VALUES (1,'test','1235342324','test',2,10.00,4,'2020-03-22 23:48:17','2020-05-07 21:44:33',1),(44,'李嘉仪','13724854516','广州市黄浦区红山三路101号',2,20.00,0,'2020-05-06 20:52:58','2020-05-06 20:52:58',1),(45,'李嘉仪','13724854516','广州市黄浦区红山三路101号',2,78.00,0,'2020-05-06 21:00:02','2020-05-06 21:00:02',1),(46,'李嘉仪','13724854516','广州市黄浦区红山三路101号',2,44.00,0,'2020-05-06 21:01:17','2020-05-06 21:01:17',1),(47,'李嘉仪','13724854516','广州市黄浦区红山三路101号',2,102.00,0,'2020-05-06 21:03:22','2020-05-06 21:03:22',1),(48,'李嘉仪','13724854516','广州市黄浦区红山三路101号',2,132.00,5,'2020-05-06 21:03:34','2020-05-07 20:55:16',1),(49,'李嘉仪','13724854516','广州市黄浦区红山三路101号',2,54.00,3,'2020-05-06 21:03:43','2020-05-09 09:49:34',1);
/*!40000 ALTER TABLE `order_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_category`
--

DROP TABLE IF EXISTS `product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `store_id` bigint(20) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_category`
--

LOCK TABLES `product_category` WRITE;
/*!40000 ALTER TABLE `product_category` DISABLE KEYS */;
INSERT INTO `product_category` VALUES (5,1,'进口果蔬','2020-03-22 20:16:12','2020-03-22 20:16:12'),(9,1,'国产水果','2020-03-22 20:26:30','2020-03-22 20:26:30');
/*!40000 ALTER TABLE `product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_info`
--

DROP TABLE IF EXISTS `product_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `store_id` bigint(20) NOT NULL,
  `product_name` varchar(30) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `stock` bigint(20) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0正常 1下架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_info`
--

LOCK TABLES `product_info` WRITE;
/*!40000 ALTER TABLE `product_info` DISABLE KEYS */;
INSERT INTO `product_info` VALUES (16,1,'皇冠密梨','皇冠的密梨',100,10,'http://localhost:8080/upload/d953da35-4283-4e19-a6fc-b532c1eb76f6code.png',1,'2020-03-22 21:14:53','2020-03-22 21:52:46',5),(19,1,'迷hotel','尝一下好吃嘛，尝一下甜不甜',234,34,'http://localhost:8080/upload/77f694b4-9a3c-46aa-9ff4-791c6968965bt2.jpg',0,'2020-03-22 21:56:10','2020-03-22 21:56:10',9);
/*!40000 ALTER TABLE `product_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recommend_info`
--

DROP TABLE IF EXISTS `recommend_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recommend_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK6ebcy2s0ibei53ov2vf5riq5s` (`product_id`),
  CONSTRAINT `FK6ebcy2s0ibei53ov2vf5riq5s` FOREIGN KEY (`product_id`) REFERENCES `product_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recommend_info`
--

LOCK TABLES `recommend_info` WRITE;
/*!40000 ALTER TABLE `recommend_info` DISABLE KEYS */;
INSERT INTO `recommend_info` VALUES (1,1,16,'2020-03-23 20:42:37','2020-03-23 20:42:37'),(2,1,19,'2020-03-23 20:42:37','2020-03-23 20:42:37');
/*!40000 ALTER TABLE `recommend_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recommend_type`
--

DROP TABLE IF EXISTS `recommend_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recommend_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recommend_type`
--

LOCK TABLES `recommend_type` WRITE;
/*!40000 ALTER TABLE `recommend_type` DISABLE KEYS */;
INSERT INTO `recommend_type` VALUES (1,'今日爆款','2020-03-23 20:42:03','2020-03-23 22:08:21');
/*!40000 ALTER TABLE `recommend_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_info`
--

DROP TABLE IF EXISTS `store_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `store_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner_id` bigint(20) NOT NULL COMMENT 'owner user id',
  `store_name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(30) DEFAULT NULL COMMENT '联系方式',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `logo` varchar(512) DEFAULT NULL,
  `min_cost` decimal(10,0) NOT NULL DEFAULT '0',
  `post_fee` decimal(10,0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_info`
--

LOCK TABLES `store_info` WRITE;
/*!40000 ALTER TABLE `store_info` DISABLE KEYS */;
INSERT INTO `store_info` VALUES (1,2,'天生水果店','天生的水果店','广州','13724854516','2020-05-02 00:00:00','2020-05-06 00:00:00','http://localhost:8080/upload/d953da35-4283-4e19-a6fc-b532c1eb76f6code.png',10,0),(58,21,'小水果店','一个小水果店','在你的心中','123435345','2020-05-08 22:42:18','2020-05-08 22:42:18','http://localhost:8080/upload/930706cd-59a3-4b25-a258-1564f1dc5a6fScenery_in_Plateau_by_Arto_Marttinen.jpg',0,0);
/*!40000 ALTER TABLE `store_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_request`
--

DROP TABLE IF EXISTS `store_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `store_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone` varchar(30) NOT NULL,
  `store_name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0新建1已阅',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_request`
--

LOCK TABLES `store_request` WRITE;
/*!40000 ALTER TABLE `store_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `store_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'admin'),(2,'store'),(3,'customer'),(4,'delivery');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL COMMENT '邮箱用户名',
  `nickname` varchar(100) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `phone` varchar(30) DEFAULT NULL COMMENT '手机号码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (2,'$2a$10$jGZN.Q218xJsH79qIZIAuuCwmUYn87RXf9hCDEs4PrX9AUuLDI.Ta','13724854516','ljy1','2020-03-01 00:00:00','2020-05-06 13:00:51','13724854516'),(21,'$2a$10$zh9cG3mLJIrKF61.j.fNcedy5m5zZX.xkYtEWQPSDiKj0.hfuEwuG','1220966771@qq.com','wb','2020-05-06 11:52:17','2020-05-08 11:04:58','19849010351');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_roles`
--

DROP TABLE IF EXISTS `sys_user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_roles` (
  `sys_user_id` bigint(20) NOT NULL,
  `roles_id` bigint(20) NOT NULL,
  KEY `FKdpvc6d7xqpqr43dfuk1s27cqh` (`roles_id`),
  KEY `FKd0ut7sloes191bygyf7a3pk52` (`sys_user_id`),
  CONSTRAINT `FKd0ut7sloes191bygyf7a3pk52` FOREIGN KEY (`sys_user_id`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `FKdpvc6d7xqpqr43dfuk1s27cqh` FOREIGN KEY (`roles_id`) REFERENCES `sys_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_roles`
--

LOCK TABLES `sys_user_roles` WRITE;
/*!40000 ALTER TABLE `sys_user_roles` DISABLE KEYS */;
INSERT INTO `sys_user_roles` VALUES (2,1),(2,2),(2,3),(2,4),(21,2),(21,4);
/*!40000 ALTER TABLE `sys_user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_address`
--

DROP TABLE IF EXISTS `user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(30) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_address`
--

LOCK TABLES `user_address` WRITE;
/*!40000 ALTER TABLE `user_address` DISABLE KEYS */;
INSERT INTO `user_address` VALUES (23,'李嘉仪','广州市黄浦区红山三路101号','13724854516',2,'2020-05-06 19:03:23','2020-05-06 19:03:23');
/*!40000 ALTER TABLE `user_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'fruit'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-09 16:26:04
