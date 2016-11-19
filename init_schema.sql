CREATE DATABASE QUICK_ACCESS_BOOKMARK;
CREATE USER QUICK_ACCESS_BOOKMARK;
GRANT ALL ON QUICK_ACCESS_BOOKMARK.* TO 'QUICK_ACCESS_BOOKMARK'@'%' identified by 'TheProject24680!';
use QUICK_ACCESS_BOOKMARK;

-- Create schema
alter table authorities drop foreign key FKhjuy9y4fd8v5m3klig05ktofg;
alter table bookmark drop foreign key FKikd1yj4vseep6u45d61rru0po;
alter table bookmark drop foreign key FKm6smfotv78f22li4bn0m0mw0a;
alter table bookmark drop foreign key FKawn4amrgn7c17r18lqimlfthu;
alter table bookmark_category drop foreign key FKtcuocqjyptgvhnic7d175t13w;
alter table bookmark_category drop foreign key FK11v7g465c41bydj40ayf0f4eg;
drop table if exists authorities;
drop table if exists bookmark;
drop table if exists bookmark_category;
drop table if exists users;
create table authorities (id bigint not null auto_increment, authority varchar(255) not null, username varchar(255) not null, primary key (id));
create table bookmark (id bigint not null auto_increment, description varchar(4000), last_validated datetime not null, name varchar(255), date_created datetime not null, date_modified datetime not null, status varchar(255), url varchar(2000) not null, bookmark_category_id bigint not null, created_by_username varchar(255), subcategory_id bigint not null, primary key (id));
create table bookmark_category (id bigint not null auto_increment, name varchar(255) not null, date_created datetime not null, date_modified datetime not null, parent_id bigint, created_by_username varchar(255), primary key (id));
create table users (username varchar(255) not null, enabled bit not null, password varchar(255) not null, registration_date datetime not null, primary key (username));
alter table authorities add constraint UK2uf74smucdwf9qal2n67m2343 unique (authority, username);
alter table bookmark_category add constraint UK_3yv99dpihgs01btct0ib6hhh8 unique (name);
alter table authorities add constraint FKhjuy9y4fd8v5m3klig05ktofg foreign key (username) references users (username);
alter table bookmark add constraint FKikd1yj4vseep6u45d61rru0po foreign key (bookmark_category_id) references bookmark_category (id);
alter table bookmark add constraint FKm6smfotv78f22li4bn0m0mw0a foreign key (created_by_username) references users (username);
alter table bookmark add constraint FKawn4amrgn7c17r18lqimlfthu foreign key (subcategory_id) references bookmark_category (id);
alter table bookmark_category add constraint FKtcuocqjyptgvhnic7d175t13w foreign key (parent_id) references bookmark_category (id);
alter table bookmark_category add constraint FK11v7g465c41bydj40ayf0f4eg foreign key (created_by_username) references users (username);

-- Init default user (Username: admin, Password: password) and category

-- Users
INSERT INTO users (password, registration_date, username, enabled) VALUES
  ('$2a$10$VUbNDU1YVuXPOlizhc8K6u0FZFDipJaIIGSSbpMNeGDyQ2IEcaily', NOW(), 'admin', true);

-- Roles
INSERT INTO authorities (username, authority) VALUES
  ('admin', 'admin');

-- Bookmark Categories
INSERT INTO bookmark_category (created_by_username, date_created, date_modified, name, parent_id) VALUES
  ('admin', NOW(), NOW(), 'None', null);