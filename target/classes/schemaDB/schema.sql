
create table if not exists student_reactive (student_id int auto_increment primary key,student_email varchar(255)  unique not null, student_name varchar(255), student_batch varchar(255), student_roll_num varchar(255),student_address varchar(255));
