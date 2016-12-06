CREATE DATABASE  IF NOT EXISTS `cwmd_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `cwmd_db`;
-- MySQL dump 10.13  Distrib 5.7.16, for Win64 (x86_64)
--
-- Host: localhost    Database: cwmd_db
-- ------------------------------------------------------
-- Server version	5.7.16-log

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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `permissions` bigint(10) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `cnp` varchar(13) NOT NULL,
  `phone` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `cnp_UNIQUE` (`cnp`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `username`, `password`, `permissions`, `lastname`, `firstname`, `cnp`, `phone`) VALUES 
(1,'adillon@gmail.com','adillon',331277003,'Dillon','Avram','1699110983491','0708006240'),
(2,'dfrench@gmail.com','dfrench',1734279712,'French','David','1640051583996','0791800115'),
(3,'cfulton@gmail.com','cfulton',1914039254,'Fulton','Ciaran','1644021089666','0701350703'),
(4,'bvaughan@gmail.com','bvaughan',782987625,'Vaughan','Beck','1637092384706','0799799270'),
(5,'gholland@gmail.com','gholland',749450676,'Holland','Griffin','1659050480928','0797820785'),
(6,'mburns@gmail.com','mburns',1982606012,'Burns','Merritt','1630043088629','0793958283'),
(7,'rwilson@gmail.com','rwilson',704078625,'Wilson','Ross','1620051587702','0763164061'),
(8,'cstokes@gmail.com','cstokes',1012580166,'Stokes','Carter','1625031084222','0741487635'),
(9,'ohartman@gmail.com','ohartman',1664290991,'Hartman','Orlando','1642072489998','0785966059'),
(10,'lwilson@gmail.com','lwilson',1130842538,'Wilson','Lamar','1618102185739','0799745676'),
(11,'pramirez@gmail.com','pramirez',1659207153,'Ramirez','Plato','1634032688481','0785332621'),
(12,'ecarpenter@gmail.com','ecarpenter',155580790,'Carpenter','Edan','1644022087049','0799054825'),
(13,'odecker@gmail.com','odecker',1437730953,'Decker','Oren','1644041086272','0787263063'),
(14,'tmooney@gmail.com','tmooney',1379847188,'Mooney','Tiger','1666101982293','0759999514'),
(15,'hkim@gmail.com','hkim',1695856512,'Kim','Hasad','1640070787508','0713706356'),
(16,'cdillard@gmail.com','cdillard',47689053,'Dillard','Cole','1659092285947','0739385134'),
(17,'ddiaz@gmail.com','ddiaz',74374473,'Diaz','Dane','1636043080479','0714201667'),
(18,'bsimon@gmail.com','bsimon',192754778,'Simon','Brandon','1675041081515','0771082292'),
(19,'lcooley@gmail.com','lcooley',1754111500,'Cooley','Lamar','1677101986333','0744039067'),
(20,'jdawson@gmail.com','jdawson',1534327099,'Dawson','Jasper','1607112683764','0736713412'),
(21,'rhead@gmail.com','rhead',1695308669,'Head','Raphael','1633061882159','0712593566'),
(22,'payala@gmail.com','payala',438461905,'Ayala','Philip','1665052688509','0734037056'),
(23,'ihardin@gmail.com','ihardin',428382545,'Hardin','Ian','1606020183146','0737856975'),
(24,'xfitzgerald@gmail.com','xfitzgerald',1067807333,'Fitzgerald','Xanthus','1669082489649','0716287774'),
(25,'jblackburn@gmail.com','jblackburn',925618512,'Blackburn','Jared','1637091286464','0754148362'),
(26,'owright@gmail.com','owright',2027541437,'Wright','Oscar','1616122389809','0797671029'),
(27,'nhorn@gmail.com','nhorn',422557881,'Horn','Nolan','1649102382224','0797337207'),
(28,'phobbs@gmail.com','phobbs',1857341006,'Hobbs','Palmer','1627092681065','0777893977'),
(29,'tsimon@gmail.com','tsimon',1618433180,'Simon','Thomas','1653071889818','0791957223'),
(30,'anunez@gmail.com','anunez',303640334,'Nunez','Adrian','1623040486189','0719972787'),
(31,'bpayne@gmail.com','bpayne',1177349841,'Payne','Brock','1632121180658','0787132451'),
(32,'bcarson@gmail.com','bcarson',1666608646,'Carson','Bernard','1686072185279','0707929434'),
(33,'odixon@gmail.com','odixon',1029525542,'Dixon','Orson','1667010589146','0764316970'),
(34,'ccoleman@gmail.com','ccoleman',926184283,'Coleman','Cameron','1654062584762','0789724762'),
(35,'bbeck@gmail.com','bbeck',1151055705,'Beck','Brenden','1641122985045','0707436427'),
(36,'ihayden@gmail.com','ihayden',813229900,'Hayden','Ira','1681081289551','0762713497'),
(37,'ohuffman@gmail.com','ohuffman',2085483734,'Huffman','Omar','1640050181970','0799312353'),
(38,'pbeard@gmail.com','pbeard',330213196,'Beard','Philip','1647091087809','0794616736'),
(39,'achambers@gmail.com','achambers',48368552,'Chambers','Adrian','1686122780379','0713586384'),
(40,'zsnider@gmail.com','zsnider',592625741,'Snider','Zane','1688072189738','0777445014'),
(41,'ohood@gmail.com','ohood',651540160,'Hood','Oleg','1605052780538','0702089905'),
(42,'danderson@gmail.com','danderson',749896724,'Anderson','Dennis','1672031286540','0767808811'),
(43,'brice@gmail.com','brice',1654010465,'Rice','Buckminster','1613082486827','0750257371'),
(44,'cshepard@gmail.com','cshepard',1584583737,'Shepard','Carlos','1691090182329','0709039437'),
(45,'mcook@gmail.com','mcook',384156545,'Cook','Melvin','1649082582609','0762067337'),
(46,'ljacobson@gmail.com','ljacobson',1622723917,'Jacobson','Logan','1608012682239','0780213838'),
(47,'briddle@gmail.com','briddle',830317292,'Riddle','Beck','1679082681787','0743188926'),
(48,'rpuckett@gmail.com','rpuckett',869385953,'Puckett','Raja','1673082082579','0786902297'),
(49,'cbuckley@gmail.com','cbuckley',1339296164,'Buckley','Cedric','1632110383596','0759405030'),
(50,'kwaters@gmail.com','kwaters',1200271044,'Waters','Keane','1603062889929','0766916879'),
(51,'lwest@gmail.com','lwest',1096811789,'West','Levi','1640071886279','0709194486'),
(52,'krandolph@gmail.com','krandolph',1533571107,'Randolph','Keith','1666091189067','0777763001'),
(53,'dmccoy@gmail.com','dmccoy',1227038446,'Mccoy','Dalton','1654051589178','0766848403'),
(54,'tparker@gmail.com','tparker',1681293568,'Parker','Tyrone','1629112686290','0726405178'),
(55,'skinney@gmail.com','skinney',1774349135,'Kinney','Sawyer','1602120781302','0768063861'),
(56,'sburris@gmail.com','sburris',1125556666,'Burris','Samuel','1694071083859','0726193548'),
(57,'drobinson@gmail.com','drobinson',819459717,'Robinson','Demetrius','1665080388669','0705900935'),
(58,'bpowell@gmail.com','bpowell',348466255,'Powell','Blake','1636110685214','0737712810'),
(59,'pmelton@gmail.com','pmelton',1277997968,'Melton','Peter','1631112283925','0759745387'),
(60,'crobinson@gmail.com','crobinson',1579250041,'Robinson','Chandler','1686051783339','0760413892'),
(61,'dbullock@gmail.com','dbullock',1330473780,'Bullock','Duncan','1623111785067','0721327997'),
(62,'tweaver@gmail.com','tweaver',280387905,'Weaver','Travis','1605051489575','0734062110'),
(63,'rhall@gmail.com','rhall',946517281,'Hall','Rashad','1664102383879','0740849068'),
(64,'wwall@gmail.com','wwall',381238984,'Wall','Walker','1688100289689','0786277935'),
(65,'hspence@gmail.com','hspence',1506730746,'Spence','Hyatt','1667121783217','0723909115'),
(66,'apayne@gmail.com','apayne',670027271,'Payne','Alec','1696122785289','0793390417'),
(67,'hcarter@gmail.com','hcarter',340293771,'Carter','Herman','1659063084505','0738900302'),
(68,'zstanley@gmail.com','zstanley',813345846,'Stanley','Zeus','1662052984157','0793601916'),
(69,'crobertson@gmail.com','crobertson',624691819,'Robertson','Christopher','1610010786049','0744292356'),
(70,'mward@gmail.com','mward',949959441,'Ward','Malachi','1699112283726','0777393334'),
(71,'ktyson@gmail.com','ktyson',147794202,'Tyson','Keegan','1648072880361','0736145320'),
(72,'hhahn@gmail.com','hhahn',1602577520,'Hahn','Hamilton','1624030882369','0713465301'),
(73,'bgamble@gmail.com','bgamble',1043208238,'Gamble','Bernard','1615032281556','0710166986'),
(74,'dthomas@gmail.com','dthomas',29488791,'Thomas','Drake','1636020980536','0708234454'),
(75,'therman@gmail.com','therman',1036842712,'Herman','Tarik','1690030183869','0710691500'),
(76,'chutchinson@gmail.com','chutchinson',1414863188,'Hutchinson','Cairo','1690012983571','0796393368'),
(77,'gvalencia@gmail.com','gvalencia',1321565072,'Valencia','Gary','1669101187313','0775406606'),
(78,'rdavis@gmail.com','rdavis',1316812479,'Davis','Ronan','1650112687340','0737910564'),
(79,'vspencer@gmail.com','vspencer',971106354,'Spencer','Vernon','1610032181917','0723816726'),
(80,'dmitchell@gmail.com','dmitchell',1417575618,'Mitchell','Dante','1662052487072','0756557502'),
(81,'jmckenzie@gmail.com','jmckenzie',1616969459,'Mckenzie','Jackson','1601101687301','0789108050'),
(82,'ebennett@gmail.com','ebennett',600339752,'Bennett','Elijah','1659041289819','0760376503'),
(83,'qpayne@gmail.com','qpayne',600268653,'Payne','Quinlan','1621040283698','0733500440'),
(84,'lwatts@gmail.com','lwatts',1983299637,'Watts','Leonard','1653101281489','0746444618'),
(85,'jmcconnell@gmail.com','jmcconnell',589717271,'Mcconnell','Joshua','1646030880319','0763539689'),
(86,'fmartin@gmail.com','fmartin',82380927,'Martin','Fitzgerald','1668091683799','0796636706'),
(87,'ghoward@gmail.com','ghoward',1177470284,'Howard','Garrison','1631040885855','0750853626'),
(88,'fgarner@gmail.com','fgarner',381831004,'Garner','Felix','1697011885559','0718849395'),
(89,'psingleton@gmail.com','psingleton',1029989335,'Singleton','Peter','1686061480109','0791768815'),
(90,'isexton@gmail.com','isexton',311707811,'Sexton','Ira','1667072581044','0781571140'),
(91,'cblake@gmail.com','cblake',1545668010,'Blake','Channing','1693011882889','0728912229'),
(92,'jlowe@gmail.com','jlowe',2120472777,'Lowe','Judah','1608061086439','0798650923'),
(93,'rmendoza@gmail.com','rmendoza',2090025392,'Mendoza','Rooney','1647092284424','0731168388'),
(94,'bmoon@gmail.com','bmoon',613792401,'Moon','Bernard','1663071788159','0784635166'),
(95,'btorres@gmail.com','btorres',1996208904,'Torres','Buckminster','1636112888656','0727487282'),
(96,'testes@gmail.com','testes',566210787,'Estes','Tobias','1648121281894','0762086625'),
(97,'xemerson@gmail.com','xemerson',826847900,'Emerson','Xanthus','1659072882383','0780165483'),
(98,'bjefferson@gmail.com','bjefferson',663679384,'Jefferson','Benedict','1667090681441','0730132205'),
(99,'bmaynard@gmail.com','bmaynard',513307550,'Maynard','Brendan','1681021381499','0758864453'),
(100,'iabbott@gmail.com','iabbott',912495089,'Abbott','Isaac','1644110689950','0762476400'),
(101,'a@a.com','a',-1,'A','AA','1234567890123','0712345678'),
(102,'b@b.com','b',3,'B','Bb','1234567809123','0712345678'),
(103,'c@c.com','c',7,'C','Cc','1234567089123','0712345678'),
(104,'d@d.com','d',18,'C','Dd','1234560789123','0712345678');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'cwmd_db'
--

--
-- Dumping routines for database 'cwmd_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-06 13:59:43
