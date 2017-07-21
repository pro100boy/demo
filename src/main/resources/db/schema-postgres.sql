DROP TABLE IF EXISTS contacts;

--
-- Table structure for table `contacts`
--

CREATE TABLE contacts
(
  id SERIAL PRIMARY KEY,
  name character varying(255) NOT NULL
);
