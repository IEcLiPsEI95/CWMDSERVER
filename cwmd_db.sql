-- MySQL dump 10.13  Distrib 5.7.16, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cwmd_db
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
-- Table structure for table `docflows`
--

DROP TABLE IF EXISTS `docflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `docflows` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupOrder` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `docflows`
--

LOCK TABLES `docflows` WRITE;
/*!40000 ALTER TABLE `docflows` DISABLE KEYS */;
/*!40000 ALTER TABLE `docflows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctype`
--

DROP TABLE IF EXISTS `doctype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `doctype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `idFlow` int(11) NOT NULL,
  `docTemplatePath` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_docType_docFlows1_idx` (`idFlow`),
  CONSTRAINT `fk_docType_docFlows1` FOREIGN KEY (`idFlow`) REFERENCES `docflows` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctype`
--

LOCK TABLES `doctype` WRITE;
/*!40000 ALTER TABLE `doctype` DISABLE KEYS */;
/*!40000 ALTER TABLE `doctype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documents`
--

DROP TABLE IF EXISTS `documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(1024) NOT NULL,
  `versionDraftMinor` int(11) NOT NULL,
  `versiuneFinRevMinor` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `idType` int(11) NOT NULL,
  `idStatus` int(11) NOT NULL,
  `nextGroup` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_documents_users1_idx` (`idUser`),
  KEY `fk_documents_groups1_idx` (`nextGroup`),
  KEY `fk_documents_docType1_idx` (`idType`),
  KEY `fk_documents_status1_idx` (`idStatus`),
  CONSTRAINT `fk_documents_docType1` FOREIGN KEY (`idType`) REFERENCES `doctype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_groups1` FOREIGN KEY (`nextGroup`) REFERENCES `groups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_status1` FOREIGN KEY (`idStatus`) REFERENCES `status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_users1` FOREIGN KEY (`idUser`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documents`
--

LOCK TABLES `documents` WRITE;
/*!40000 ALTER TABLE `documents` DISABLE KEYS */;
/*!40000 ALTER TABLE `documents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` (`id`, `name`) VALUES (1,'secretara');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `statusName` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

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
  `idGroup` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `cnp_UNIQUE` (`cnp`),
  UNIQUE KEY `phone_UNIQUE` (`phone`),
  KEY `fk_users_groups1` (`idGroup`),
  CONSTRAINT `fk_users_groups1` FOREIGN KEY (`idGroup`) REFERENCES `groups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `username`, `password`, `permissions`, `lastname`, `firstname`, `cnp`, `phone`, `idGroup`) VALUES (1,'adillon@gmail.com','cbac6d2fd32e58124a25148e56614219',331277003,'Dillon','Avram','1699110983491','0708006240',1),(2,'dfrench@gmail.com','01ca84300cb12160cc9fc6e8c9542fd8',1734279712,'French','David','1640051583996','0791800115',1),(3,'cfulton@gmail.com','65fd2bf436aa22bdac07a6309b944a1d',1914039254,'Fulton','Ciaran','1644021089666','0701350703',1),(4,'bvaughan@gmail.com','b9dff841e80d71f0cdb3b0c3e39f274b',782987625,'Vaughan','Beck','1637092384706','0799799270',1),(5,'gholland@gmail.com','b6e192c8c5e3e99fe84cc9ae7e37d000',749450676,'Holland','Griffin','1659050480928','0797820785',1),(6,'mburns@gmail.com','3be206d9003a2f98e7208010905b7cd2',1982606012,'Burns','Merritt','1630043088629','0793958283',1),(7,'rwilson@gmail.com','d73b362a1798ceadc517bf999a758f7a',704078625,'Wilson','Ross','1620051587702','0763164061',1),(8,'cstokes@gmail.com','0e7524d5e69b759e3de390b1549106c5',1012580166,'Stokes','Carter','1625031084222','0741487635',1),(9,'ohartman@gmail.com','e77ecdb908e331a6598d757f341d3570',1664290991,'Hartman','Orlando','1642072489998','0785966059',1),(10,'lwilson@gmail.com','cf2f7f2a35e039675ded5b62ef29069b',1130842538,'Wilson','Lamar','1618102185739','0799745676',1),(11,'pramirez@gmail.com','181b30d3df1b264cd0991d6767958c42',1659207153,'Ramirez','Plato','1634032688481','0785332621',1),(12,'ecarpenter@gmail.com','d2da21fa33c476c26d1c12a677ee8910',155580790,'Carpenter','Edan','1644022087049','0799054825',1),(13,'odecker@gmail.com','4fd5f4d35d44a36022e56561f335a48f',1437730953,'Decker','Oren','1644041086272','0787263063',1),(14,'tmooney@gmail.com','2002817756bbf5b5756549a7f2797fa1',1379847188,'Mooney','Tiger','1666101982293','0759999514',1),(15,'hkim@gmail.com','38a1ff6aa82f88f670c6f3fb9658e356',1695856512,'Kim','Hasad','1640070787508','0713706356',1),(16,'cdillard@gmail.com','9b1dda6cd318bf0c629a51e8d192fec8',47689053,'Dillard','Cole','1659092285947','0739385134',1),(17,'ddiaz@gmail.com','2a9035d8f559cc3d794539b9aa93ef1b',74374473,'Diaz','Dane','1636043080479','0714201667',1),(18,'bsimon@gmail.com','9a26437085490a546e77a5c2bd34c077',192754778,'Simon','Brandon','1675041081515','0771082292',1),(19,'lcooley@gmail.com','3d2445e2e7e591f6d650303dc7675f2a',1754111500,'Cooley','Lamar','1677101986333','0744039067',1),(20,'jdawson@gmail.com','5061f75a6d9761e967bff959f30604b7',1534327099,'Dawson','Jasper','1607112683764','0736713412',1),(21,'rhead@gmail.com','58798141d3298b49fcfe34b155b5eabc',1695308669,'Head','Raphael','1633061882159','0712593566',1),(22,'payala@gmail.com','0ddc004eaa2a14491567f7cda8c32c67',438461905,'Ayala','Philip','1665052688509','0734037056',1),(23,'ihardin@gmail.com','fc17b9b692d777c18e94db78131390a4',428382545,'Hardin','Ian','1606020183146','0737856975',1),(24,'xfitzgerald@gmail.com','2d3de51d62a84ab3963628afbc9ef9e4',1067807333,'Fitzgerald','Xanthus','1669082489649','0716287774',1),(25,'jblackburn@gmail.com','a72562b0631c439b8a9423c06518fc9c',925618512,'Blackburn','Jared','1637091286464','0754148362',1),(26,'owright@gmail.com','16365bd7a4e42382b23dcc6634a5998b',2027541437,'Wright','Oscar','1616122389809','0797671029',1),(27,'nhorn@gmail.com','feb0b1cc15f72420a2bac0a2536d0359',422557881,'Horn','Nolan','1649102382224','0797337207',1),(28,'phobbs@gmail.com','39cd2710b143ec136a23c548bcd446df',1857341006,'Hobbs','Palmer','1627092681065','0777893977',1),(29,'tsimon@gmail.com','7d752369ed8604506c0ac51c6f702712',1618433180,'Simon','Thomas','1653071889818','0791957223',1),(30,'anunez@gmail.com','1e21ce8fbac7c9344d032ff6cfdd5ac3',303640334,'Nunez','Adrian','1623040486189','0719972787',1),(31,'bpayne@gmail.com','8df4d03c5565fcbb8f65eada9a1864e8',1177349841,'Payne','Brock','1632121180658','0787132451',1),(32,'bcarson@gmail.com','09a2a2ba04d96fcf59dcd376e842bd31',1666608646,'Carson','Bernard','1686072185279','0707929434',1),(33,'odixon@gmail.com','d69934316e97df3d2fe5e77b68d55073',1029525542,'Dixon','Orson','1667010589146','0764316970',1),(34,'ccoleman@gmail.com','c57f05f07f9dd2eabc4049d4c01544ca',926184283,'Coleman','Cameron','1654062584762','0789724762',1),(35,'bbeck@gmail.com','cef12e4044e1653e493043b3bbb1f87f',1151055705,'Beck','Brenden','1641122985045','0707436427',1),(36,'ihayden@gmail.com','445f0837e29aa0dfce7fb3f5963a3eca',813229900,'Hayden','Ira','1681081289551','0762713497',1),(37,'ohuffman@gmail.com','c2d650d2fda9bcf608aeb867ace36f76',2085483734,'Huffman','Omar','1640050181970','0799312353',1),(38,'pbeard@gmail.com','24565f19d4daed0f440b6734cedc65c2',330213196,'Beard','Philip','1647091087809','0794616736',1),(39,'achambers@gmail.com','6fbaf5698f4065553eb04057e42c2057',48368552,'Chambers','Adrian','1686122780379','0713586384',1),(40,'zsnider@gmail.com','e5f5848999c5614797e6aa562bd0ae41',592625741,'Snider','Zane','1688072189738','0777445014',1),(41,'ohood@gmail.com','95f02f27e1e5e293ee19cec1294cd113',651540160,'Hood','Oleg','1605052780538','0702089905',1),(42,'danderson@gmail.com','b284e96147cadc9e9c47e8d0c7c3fb78',749896724,'Anderson','Dennis','1672031286540','0767808811',1),(43,'brice@gmail.com','6cb528d1b00572441b44b2a528183bf4',1654010465,'Rice','Buckminster','1613082486827','0750257371',1),(44,'cshepard@gmail.com','f6abb8c7288f85ddd84096cbbdaa56b8',1584583737,'Shepard','Carlos','1691090182329','0709039437',1),(45,'mcook@gmail.com','5a59bf4a0ffc2591816535c939325545',384156545,'Cook','Melvin','1649082582609','0762067337',1),(46,'ljacobson@gmail.com','6c75a4861fe4b26d31bd07113bfb454d',1622723917,'Jacobson','Logan','1608012682239','0780213838',1),(47,'briddle@gmail.com','2d53b7e85163678f4bc3105ffcc492f3',830317292,'Riddle','Beck','1679082681787','0743188926',1),(48,'rpuckett@gmail.com','d727918f337d814bd719142693201209',869385953,'Puckett','Raja','1673082082579','0786902297',1),(49,'cbuckley@gmail.com','80f0a2517001ecec3a3789e43be9a9a3',1339296164,'Buckley','Cedric','1632110383596','0759405030',1),(50,'kwaters@gmail.com','a9f5d8d46875020e0cdc0d50cf2852f2',1200271044,'Waters','Keane','1603062889929','0766916879',1),(51,'lwest@gmail.com','1f2a6bdbefe46843025a60cd4cd1f86c',1096811789,'West','Levi','1640071886279','0709194486',1),(52,'krandolph@gmail.com','6507cdaf00ea659ac9580a2c373f3f54',1533571107,'Randolph','Keith','1666091189067','0777763001',1),(53,'dmccoy@gmail.com','df1f2e481524ac8fe6192a999140ba29',1227038446,'Mccoy','Dalton','1654051589178','0766848403',1),(54,'tparker@gmail.com','b023779e8235760414fdf765a5dabd46',1681293568,'Parker','Tyrone','1629112686290','0726405178',1),(55,'skinney@gmail.com','7d59f47e041d660377c3c9f0e3a17c6e',1774349135,'Kinney','Sawyer','1602120781302','0768063861',1),(56,'sburris@gmail.com','b30dc781ee9f8d79a687376cb5011a6e',1125556666,'Burris','Samuel','1694071083859','0726193548',1),(57,'drobinson@gmail.com','b6be2882f1dd7b77ca4f57ad211188e5',819459717,'Robinson','Demetrius','1665080388669','0705900935',1),(58,'bpowell@gmail.com','2ed6cd3733ad8cfb5d6357b49e446fa4',348466255,'Powell','Blake','1636110685214','0737712810',1),(59,'pmelton@gmail.com','f3c917c40e6b5a4476570521d7bfe2d6',1277997968,'Melton','Peter','1631112283925','0759745387',1),(60,'crobinson@gmail.com','e014285c81d8aed72c781703435f2592',1579250041,'Robinson','Chandler','1686051783339','0760413892',1),(61,'dbullock@gmail.com','7ea3605cbdaf782a7b33c1f51da8ae5f',1330473780,'Bullock','Duncan','1623111785067','0721327997',1),(62,'tweaver@gmail.com','0ef42bc44ef586983eeea4c4c34c2b21',280387905,'Weaver','Travis','1605051489575','0734062110',1),(63,'rhall@gmail.com','1fe81afccdd4fdab6a554e4a6ca1413f',946517281,'Hall','Rashad','1664102383879','0740849068',1),(64,'wwall@gmail.com','3c1fe1629481af8cff9dcc5b927bcdcf',381238984,'Wall','Walker','1688100289689','0786277935',1),(65,'hspence@gmail.com','5eb8d92e04ad16545acdc22d7c974554',1506730746,'Spence','Hyatt','1667121783217','0723909115',1),(66,'apayne@gmail.com','7b32f2dcd3b4d2c779ab66cf4fc600eb',670027271,'Payne','Alec','1696122785289','0793390417',1),(67,'hcarter@gmail.com','50ae5da7b61c7170122daec3d66ee238',340293771,'Carter','Herman','1659063084505','0738900302',1),(68,'zstanley@gmail.com','8340bd455fc373308f2d844c410bb76a',813345846,'Stanley','Zeus','1662052984157','0793601916',1),(69,'crobertson@gmail.com','4cee3985a71be913300677f839e6c42a',624691819,'Robertson','Christopher','1610010786049','0744292356',1),(70,'mward@gmail.com','fa78d7721dcbf37eb3bc0f6b5830892e',949959441,'Ward','Malachi','1699112283726','0777393334',1),(71,'ktyson@gmail.com','ff1a6b90d738b43c33f60010482c2062',147794202,'Tyson','Keegan','1648072880361','0736145320',1),(72,'hhahn@gmail.com','483605318ce277b113e848efb79df647',1602577520,'Hahn','Hamilton','1624030882369','0713465301',1),(73,'bgamble@gmail.com','11b7c96093c39c4b14336745d5d51cd1',1043208238,'Gamble','Bernard','1615032281556','0710166986',1),(74,'dthomas@gmail.com','5d36dd76fa8b8e4853f22dd8df8ad7df',29488791,'Thomas','Drake','1636020980536','0708234454',1),(75,'therman@gmail.com','eaf0a96207c008ab70e53b45e6814151',1036842712,'Herman','Tarik','1690030183869','0710691500',1),(76,'chutchinson@gmail.com','cbbb8bb6017c6cc47940e3284e550865',1414863188,'Hutchinson','Cairo','1690012983571','0796393368',1),(77,'gvalencia@gmail.com','77e8009ebfaa34ec8f029497d237d2aa',1321565072,'Valencia','Gary','1669101187313','0775406606',1),(78,'rdavis@gmail.com','6176af806ebf34433e50fdf6ad209b64',1316812479,'Davis','Ronan','1650112687340','0737910564',1),(79,'vspencer@gmail.com','b0b04980deae8a6125fd6bbabd7a54a8',971106354,'Spencer','Vernon','1610032181917','0723816726',1),(80,'dmitchell@gmail.com','c6768b93905dcaccbc6005eca6c68bd7',1417575618,'Mitchell','Dante','1662052487072','0756557502',1),(81,'jmckenzie@gmail.com','dc675207497ee11a7c939b054d434499',1616969459,'Mckenzie','Jackson','1601101687301','0789108050',1),(82,'ebennett@gmail.com','c2d6a6dbbf039e96bb28313f922c4ad2',600339752,'Bennett','Elijah','1659041289819','0760376503',1),(83,'qpayne@gmail.com','a6242abd30da01c281267635876c6a8d',600268653,'Payne','Quinlan','1621040283698','0733500440',1),(84,'lwatts@gmail.com','aaa27f864747debf2ad7caa7eebf9775',1983299637,'Watts','Leonard','1653101281489','0746444618',1),(85,'jmcconnell@gmail.com','172525879e8b2bd8c48a63b99c841096',589717271,'Mcconnell','Joshua','1646030880319','0763539689',1),(86,'fmartin@gmail.com','b57a2b99ea8efac591523a3094882552',82380927,'Martin','Fitzgerald','1668091683799','0796636706',1),(87,'ghoward@gmail.com','2b57b0bc517846986cbe382b5f53d9bd',1177470284,'Howard','Garrison','1631040885855','0750853626',1),(88,'fgarner@gmail.com','84c72f29cf5f3cf0d98032e2b83aab4b',381831004,'Garner','Felix','1697011885559','0718849395',1),(89,'psingleton@gmail.com','22ae0427b5ec54183b195c8a0c27b3b8',1029989335,'Singleton','Peter','1686061480109','0791768815',1),(90,'isexton@gmail.com','33268b02c9f29b8be5058319b2cbc4c5',311707811,'Sexton','Ira','1667072581044','0781571140',1),(91,'cblake@gmail.com','cd74fae0a3adf459f73bbf187607ccea',1545668010,'Blake','Channing','1693011882889','0728912229',1),(92,'jlowe@gmail.com','eba53dfd648edfeb6f5128c74751821f',2120472777,'Lowe','Judah','1608061086439','0798650923',1),(93,'rmendoza@gmail.com','2d1b0b4fe1af0426872dfb3eeb1d2329',2090025392,'Mendoza','Rooney','1647092284424','0731168388',1),(94,'bmoon@gmail.com','8465e01cc1efaaae12f1f9af8144c13f',613792401,'Moon','Bernard','1663071788159','0784635166',1),(95,'btorres@gmail.com','5ba753902b3afceb8f900a660c6c673a',1996208904,'Torres','Buckminster','1636112888656','0727487282',1),(96,'testes@gmail.com','6e7906b7fb3f8e1c6366c0910050e595',566210787,'Estes','Tobias','1648121281894','0762086625',1),(97,'xemerson@gmail.com','0e0281b62199308a5ad10c25764bfeee',826847900,'Emerson','Xanthus','1659072882383','0780165483',1),(98,'bjefferson@gmail.com','17c67d2a135858d66fa9c73e1312e281',663679384,'Jefferson','Benedict','1667090681441','0730132205',1),(99,'bmaynard@gmail.com','e6b37fb50ed7d2b8f8ef234178352645',513307550,'Maynard','Brendan','1681021381499','0758864453',1),(100,'iabbott@gmail.com','afdaaa28545f23f28257aee0b0dfda9e',912495089,'Abbott','Isaac','1644110689950','0762476400',1);
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

-- Dump completed on 2017-01-22  0:00:44
