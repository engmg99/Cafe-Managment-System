/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 5.1.53-community-log : Database - cafemangement
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cafemangement` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `cafemangement`;

/*Table structure for table `cafe_user` */

DROP TABLE IF EXISTS `cafe_user`;

CREATE TABLE `cafe_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contact_number` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

/*Data for the table `cafe_user` */

insert  into `cafe_user`(`user_id`,`contact_number`,`email`,`name`,`password`,`role`,`status`,`category_id`) values 
(1,'1234567890','danielage1999@gmail.com','Test','$2a$10$2pHsFSuPoOfYpJg7KPcJm.yK5Q8zcI6kAZ2TtKhIXksGMw2iHAydm','user','true',0),
(3,'1598764230','gaurav@gmail.com','Gaurav','$2a$10$dGeJXa9YbGo4n3L5IaRwOeix6sxODZPo5iypdo.RmOfyzldxys8vS','user','false',0),
(4,'1598764230','cafemgmnt@mailinator.com','Mailinator','$2a$10$lkrGEzCQArqidRPxUDkB5ufc0xeiOFi7HDdTfBUJI1mAj97kq2146','admin','true',0),
(6,'8437227606','goyalmunish4306@gmail.com','Munish Goyal','$2a$10$v2IyGkAdbc3.yiCSPc0zfOMlePV1dNSA/mElLGmS2drKFnCsGLc2a','admin','true',0);

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `category` */

insert  into `category`(`category_id`,`name`) values 
(1,'Pizza'),
(2,'Dosaa'),
(3,'Shakes');

/*Table structure for table `customer_bill` */

DROP TABLE IF EXISTS `customer_bill`;

CREATE TABLE `customer_bill` (
  `bill_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contact_number` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `product_details` varchar(255) DEFAULT NULL,
  `total_amount` bigint(20) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`bill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `customer_bill` */

insert  into `customer_bill`(`bill_id`,`contact_number`,`created_by`,`email`,`name`,`payment_method`,`product_details`,`total_amount`,`uuid`) values 
(1,'1234567890','goyalmunish4306@gmail.com','test@gmail.com','test','Cash',NULL,279,'BILL-1701248343635');

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` bigint(20) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `category_fk_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `FK2bdcfpvvr64wsjb85mchhdg9j` (`category_fk_id`),
  CONSTRAINT `FK2bdcfpvvr64wsjb85mchhdg9j` FOREIGN KEY (`category_fk_id`) REFERENCES `category` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `product` */

insert  into `product`(`product_id`,`description`,`name`,`price`,`status`,`category_fk_id`) values 
(1,'Nice Pizza with Corn','Corn Pizza',500,'true',1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
