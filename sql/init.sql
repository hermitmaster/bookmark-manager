CREATE DATABASE QUICK_ACCESS_BOOKMARK;
CREATE USER mscougars;
GRANT ALL ON QUICK_ACCESS_BOOKMARK.* TO 'mscougars'@'%' identified by 'TheProject24680!';
USE QUICK_ACCESS_BOOKMARK;

CREATE TABLE bookmark
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    description VARCHAR(255),
    name VARCHAR(255),
    date_created DATETIME NOT NULL,
    date_modified DATETIME,
    status VARCHAR(255),
    url VARCHAR(255) NOT NULL,
    bookmark_category_id BIGINT(20),
    created_by_id BIGINT(20),
    subcategory_id BIGINT(20)
);
CREATE TABLE bookmark_category
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date_created DATETIME NOT NULL,
    date_modified DATETIME,
    parent_id BIGINT(20),
    created_by_id BIGINT(20)
);
CREATE TABLE bookmark_category_bookmarks
(
    bookmark_category_id BIGINT(20) NOT NULL,
    bookmarks_id BIGINT(20) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (bookmark_category_id, bookmarks_id)
);
CREATE TABLE user
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    password VARCHAR(255) NOT NULL,
    registration_date DATETIME NOT NULL,
    username VARCHAR(255)
);
ALTER TABLE bookmark ADD FOREIGN KEY (bookmark_category_id) REFERENCES bookmark_category (id);
ALTER TABLE bookmark ADD FOREIGN KEY (created_by_id) REFERENCES user (id);
ALTER TABLE bookmark ADD FOREIGN KEY (subcategory_id) REFERENCES bookmark_category (id);
CREATE INDEX FKawn4amrgn7c17r18lqimlfthu ON bookmark (subcategory_id);
CREATE INDEX FKikd1yj4vseep6u45d61rru0po ON bookmark (bookmark_category_id);
CREATE INDEX FKoo5ebli3a4ew8jgck8p076vru ON bookmark (created_by_id);
ALTER TABLE bookmark_category ADD FOREIGN KEY (parent_id) REFERENCES bookmark_category (id);
ALTER TABLE bookmark_category ADD FOREIGN KEY (created_by_id) REFERENCES user (id);
CREATE INDEX FK1d3f2mv94fpcfjkhwdx4aobak ON bookmark_category (created_by_id);
CREATE INDEX FKtcuocqjyptgvhnic7d175t13w ON bookmark_category (parent_id);
ALTER TABLE bookmark_category_bookmarks ADD FOREIGN KEY (bookmark_category_id) REFERENCES bookmark_category (id);
ALTER TABLE bookmark_category_bookmarks ADD FOREIGN KEY (bookmarks_id) REFERENCES bookmark (id);
CREATE UNIQUE INDEX UK_ce3i4uj770cfuc9drt4scxhfv ON bookmark_category_bookmarks (bookmarks_id);
CREATE UNIQUE INDEX UK_sb8bbouer5wak8vyiiy4pf2bx ON user (username);