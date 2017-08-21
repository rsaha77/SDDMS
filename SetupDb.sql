-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sddmsdb
-- ------------------------------------------------------
-- Server version	5.5.46

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
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client` (
  `Id` int(11) NOT NULL DEFAULT '0',
  `Email` varchar(45) DEFAULT NULL,
  `Name` varchar(45) DEFAULT NULL,
  `Type` varchar(45) DEFAULT NULL,
  `Address` varchar(100) DEFAULT NULL,
  `Contact` varchar(45) DEFAULT NULL,
  `Transport` varchar(45) DEFAULT NULL,
  `Next_Order_Date` varchar(45) DEFAULT NULL,
  `Important_Note` varchar(45) DEFAULT NULL,
  `Region_Id` int(11) DEFAULT NULL,
  `Region` varchar(45) DEFAULT 'PleaseAssignRegion',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (1,'kfc_wdl@gmail.com','KFC Woodlands','Fast Food','4A Woodlands Centre Rd, Singapore 731005','6269-2918','Motorbike, Bus, Cab, Metro','2015-12-27','Increase the number of orders by conforming',2,'Woodlands'),(2,'wap_sg@worksap.co.jp','WORKS APPLICATIONS SINGAPORE PTE.LTD','Workplace ',' 1 Fusionopolis Walk, #04--1/02, Solaris South Tower ','6467-7120','Metro, Bus, Cab, Motorbike','2015-12-22','Increase the supply',5,'OneNorth'),(3,'kfc_onr@outlook.com','KFC One North','Fast Food','238 Thomson Road, #01-17 Novena Square Shopping Mall, 307683','6255-6372','Cab, Motorbike, Bus','2015-12-23','New restaurant, regular email',5,'OneNorth'),(4,'gnc_jpt@outlookcom','GNC JPT','Health Drinks','1 Jurong West Central 2, 648886','6847-9857','Metro, Cab, Bus, Motorbike','2015-12-29','More health Products',4,'JurongWest'),(5,'dbbr_jbr@gmail.com','Dragon Brand Bird\'s Nest','Health Drinks',' 50 #B1-36 JEM, Jurong Gateway Rd','6538-2288','Cab, Motorbike, Bus','2015-12-24','Visit the website: http://dragonbrand.com.sg/',4,'JurongWest'),(6,'kfc_jpt@outlook.com','KFC Jurong Point','Fast Food','1 Jurong West Central 2, 648886','6793-5501','Metro, Cab, Bus, Motorbike','2015-12-22','High Demand',4,'JurongWest'),(7,'fpx_jpt@gmail.com','FairPrice Xtra - Jurong West','All','1 Jurong West Central 2, Jurong Point, Singapore 648331','6456-0233','Cab, Motorbike, Bus','2015-12-28','Highest number of orders',4,'JurongWest'),(8,'kfc_clm@gmail.com','KFC Clementi','Fast Food','451 Clementi Ave 3, 120451','6778-4389','Bus, Cab, Metro','2015-12-21','Early response required',1,'Clementi'),(9,'kfc_bgs@gmail.com','KFC Boogis','Fast Food','230 Victoria St, Bugis Junction, 188624','6334-5543','Cab, Metro, Bus','2015-12-25','Ping early',3,'Boogis'),(10,'jun_onr@gmail.com','JUNEDO\'s Goji-C Antioxidant Drink','Health Drinks','1 Fusionopolis Way Tower 2 #B2-10A Connexis','6784-9857','Cab, Motorbike, Bus','2015-12-24','Less demand',5,'OneNorth'),(11,'fpc_onr@outlook.com','FPC One North','All','238 Thomson Road, #03-12 Novena Square Shopping Mall, 307683','6418-7987','Metro, Bus, Cab, Motorbike','2015-12-28','Highest number of orders',5,'OneNorth'),(12,'mcd_onr@outlook.com','MCD OneNorth','Fast Food','238 Thomson Road, #02-12 Novena Square','6574-9587','Cab, Motorbike, Bus','2015-12-22','High Demand',5,'OneNorth'),(13,'grd_onr@outlook.com','Guardian','Health Food','293 Holland Rd, Singapore 278628','6469-0700','Cab, Motorbike, Bus','2015-12-24','High Demand',5,'OneNorth'),(14,'ssh_onr@gmail.com','Singapore Sports Hub','Health Drinks','123 Sample Address, Singapore 541254','6874-5457','Cab, Motorbike, Bus','2015-12-28','See specific products only',5,'OneNorth'),(15,'ssa_jws@gmail.com','SSA Jurong West','Music Arena','3 Jurong West Central 4, 648886','6847-4878','Metro, Bus, Cab, Motorbike','2015-12-21','Early response required',4,'JurongWest'),(16,'check@chk.com','check_name','check_type','check_address','check_contact','check_transport','check_next_order','check_important_note',1,'check_region');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `offers`
--

DROP TABLE IF EXISTS `offers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `offers` (
  `ProductId` int(11) NOT NULL,
  `Offers` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ProductId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offers`
--

LOCK TABLES `offers` WRITE;
/*!40000 ALTER TABLE `offers` DISABLE KEYS */;
INSERT INTO `offers` VALUES (1,'5% off for 100 Items, 10% off on 1000 Items, 7% off (100 Items + 50 Items of Pid-4)'),(2,'10 Items of Pid 4 free with 100 Items, 5% off on 100 Items');
/*!40000 ALTER TABLE `offers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `ProductId` int(11) NOT NULL,
  `Price` varchar(45) DEFAULT NULL,
  `Type` varchar(45) DEFAULT NULL,
  `ProductName` varchar(45) NOT NULL,
  PRIMARY KEY (`ProductId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'2.5$','Fast Food','Coka Cola');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `regions`
--

DROP TABLE IF EXISTS `regions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `regions` (
  `Id` int(11) NOT NULL,
  `Name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regions`
--

LOCK TABLES `regions` WRITE;
/*!40000 ALTER TABLE `regions` DISABLE KEYS */;
INSERT INTO `regions` VALUES (1,'Clementi'),(2,'Woodlands'),(3,'Boogis'),(4,'JurongWest'),(5,'OneNorth');
/*!40000 ALTER TABLE `regions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `name` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('USER'),('ADMIN');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account`
--

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_account` (
  `id` varchar(25) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `firstname` varchar(25) DEFAULT NULL,
  `lastname` varchar(25) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `office_id` int(11) DEFAULT NULL,
  `language` varchar(20) DEFAULT 'en',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account`
--

LOCK TABLES `user_account` WRITE;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
INSERT INTO `user_account` VALUES ('ranasaha','827ccb0eea8a706c4c34a16891f84e7b','Rana','Saha','rsaha.nitdgp@gmail.com',1,'en');
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_account_id` varchar(25) NOT NULL,
  `role` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES ('ranasaha','ADMIN'),('ranasaha','USER');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-06 18:41:11
