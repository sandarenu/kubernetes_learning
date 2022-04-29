-- Enable LOAD FILE LOCAL support
SET GLOBAL local_infile = 'ON';

CREATE TABLE enterprise
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(100),
    contact_email     VARCHAR(255),
    contact_no        VARCHAR(15),
    address           VARCHAR(255),
    website           VARCHAR(255),
    logo              VARCHAR(255),
    status            TINYINT,
    created_date      DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_updated_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = `UTF8MB4`;


