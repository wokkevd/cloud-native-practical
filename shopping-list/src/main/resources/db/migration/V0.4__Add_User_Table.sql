create table SHOP_USER
(
  ID         UUID PRIMARY KEY,
  FIRST_NAME TEXT,
  LAST_NAME  TEXT,
  EMAIL      TEXT,
  PASSWORD   TEXT,
  VERIFIED   BOOLEAN DEFAULT FALSE
)