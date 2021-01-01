drop table IF EXISTS Amazonitem;

create TABLE Amazonitem(id INT PRIMARY KEY
                    , title VARCHAR(250)  NOT NULL
                    , price DOUBLE  NOT NULL
                    , link VARCHAR(3000)  NOT NULL);

commit;