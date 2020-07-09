-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 09, 2020 at 03:55 AM
-- Server version: 10.4.8-MariaDB
-- PHP Version: 7.3.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `basic_android`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertproduct` (IN `Pdno` VARCHAR(10), IN `Pdid` VARCHAR(10), IN `Price` DOUBLE, IN `Qty` INT(10))  NO SQL
BEGIN
	INSERT INTO productin VALUES (Pdno,Pdid,CURRENT_DATE(),Qty,Price);
    UPDATE product SET price = Price,qty = qty + Qty WHERE product_id = Pdid;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `maps`
--

CREATE TABLE `maps` (
  `map_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `maps`
--

INSERT INTO `maps` (`map_id`, `user_id`, `latitude`, `longitude`) VALUES
(1, 1, 13.5222867, 100.7460083),
(2, 1, 13.5265234, 100.7490471),
(3, 1, 13.5260234, 100.7495471);

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `product_id` varchar(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `price` double NOT NULL,
  `qty` int(10) NOT NULL,
  `image` varchar(100) NOT NULL,
  `ProductTypeID` varchar(10) NOT NULL,
  `Unit_id` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`product_id`, `name`, `price`, `qty`, `image`, `ProductTypeID`, `Unit_id`) VALUES
('P0001', 'เสื้อคอกลม', 2000, 36, '2a443e6a68684e668dbc66552b1b7db5.jpg', 'T001', 'U0001'),
('P0002', 'กางเกงยีนส์', 5544, 31, 'YMLKH-005-1695.-B.jpg', 'T003', 'U0001'),
('P0003', 'กางเกงขาสั้น', 2000, 20, 'aa.jpg', 'T001', 'U0001'),
('P0004', 'รองเท้าผ้าใบ', 2000, 20, 'adidas-yezy-boost-350-v2-yecheil-5.jpg', 'T003', 'U0001'),
('P0005', 'หมวก', 2000, 80, 'dhx5sv.jpg', 'T001', 'U0001'),
('P0007', 'abc', 2000, 65, 'ABC-pinkfong-01.png', 'T001', 'U0001'),
('P0008', 'Notebook', 2000, 20, 'fccd1d5121f740b8991fc4dfee4d2b9e.jpg', 'T001', 'U0001'),
('P0013', 'Yeezy', 123, 26, '16171cd504c441728343e6972f933722.jpg', 'T004', 'U0001'),
('P0014', 'Toyota', 2000, 240, '37deed960a0341359a7f2b4cd93ea6d7.jpg', 'T009', 'U0001');

-- --------------------------------------------------------

--
-- Table structure for table `productin`
--

CREATE TABLE `productin` (
  `ProductInNo` varchar(10) NOT NULL,
  `product_id` varchar(10) NOT NULL,
  `DateIn` date NOT NULL,
  `Quantity` int(10) NOT NULL,
  `Price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `productin`
--

INSERT INTO `productin` (`ProductInNo`, `product_id`, `DateIn`, `Quantity`, `Price`) VALUES
('I0001', 'P0002', '2020-02-14', 10, 2500),
('I0002', 'P0002', '2020-02-16', 6, 5544),
('I0003', 'P0007', '2020-02-16', 10, 2000);

-- --------------------------------------------------------

--
-- Table structure for table `productout`
--

CREATE TABLE `productout` (
  `ProductOutNo` varchar(10) NOT NULL,
  `product_id` varchar(10) NOT NULL,
  `DateOut` date NOT NULL,
  `Quantity` int(10) NOT NULL,
  `Price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `productout`
--

INSERT INTO `productout` (`ProductOutNo`, `product_id`, `DateOut`, `Quantity`, `Price`) VALUES
('O0001', 'P0001', '2020-02-13', 5, 1000),
('O0002', 'P0014', '2020-02-16', 20, 2000),
('O0003', 'P0013', '2020-02-13', 2, 2000),
('O0004', 'P0014', '2020-02-16', 10, 2000);

-- --------------------------------------------------------

--
-- Table structure for table `producttype`
--

CREATE TABLE `producttype` (
  `ProductTypeID` varchar(10) NOT NULL,
  `ProductTypeName` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `producttype`
--

INSERT INTO `producttype` (`ProductTypeID`, `ProductTypeName`) VALUES
('T001', 'Food'),
('T002', 'AAA'),
('T003', 'BBB'),
('T004', 'Dru'),
('T005', 'Computer'),
('T006', 'My library'),
('T007', 'Bank'),
('T008', 'RB6'),
('T009', 'car'),
('T010', 'cheery');

-- --------------------------------------------------------

--
-- Table structure for table `unit`
--

CREATE TABLE `unit` (
  `Unit_id` varchar(10) NOT NULL,
  `Unit_name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `unit`
--

INSERT INTO `unit` (`Unit_id`, `Unit_name`) VALUES
('U0001', 'ชิ้น'),
('U0002', 'โหล'),
('U0003', 'แพ็ค');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(10) NOT NULL,
  `password` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`) VALUES
(1, 'Bank', '1234'),
(2, 'kane', '1234'),
(3, 'Dong', '1234'),
(5, '1234', '1234'),
(6, 'khiw', '256');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `maps`
--
ALTER TABLE `maps`
  ADD PRIMARY KEY (`map_id`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`product_id`);

--
-- Indexes for table `productin`
--
ALTER TABLE `productin`
  ADD PRIMARY KEY (`ProductInNo`);

--
-- Indexes for table `productout`
--
ALTER TABLE `productout`
  ADD PRIMARY KEY (`ProductOutNo`);

--
-- Indexes for table `producttype`
--
ALTER TABLE `producttype`
  ADD PRIMARY KEY (`ProductTypeID`);

--
-- Indexes for table `unit`
--
ALTER TABLE `unit`
  ADD PRIMARY KEY (`Unit_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `maps`
--
ALTER TABLE `maps`
  MODIFY `map_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
