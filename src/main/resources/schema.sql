DROP TABLE REFERENCE;
DROP TABLE USER_NOTE;

CREATE TABLE REFERENCE (

  id                BIGINT          NOT NULL GENERATED ALWAYS AS IDENTITY,
  title             VARCHAR(150)    NOT NULL,
  authors_names     VARCHAR(50)     NOT NULL,
  keywords          VARCHAR(250)    NOT NULL,
  file_path         VARCHAR(150)    NOT NULL,
  PRIMARY KEY (id)

);

CREATE TABLE USER_NOTE (
  id              BIGINT        NOT NULL GENERATED ALWAYS AS IDENTITY,
  reference_id    BIGINT        NOT NULL,
  title           VARCHAR(150)  NOT NULL,
  text            VARCHAR(2000) NOT NULL,

  FOREIGN KEY(reference_id) REFERENCES REFERENCE(id),
  PRIMARY KEY (id)
);