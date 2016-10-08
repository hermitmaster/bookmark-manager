CREATE DATABASE QUICK_ACCESS_BOOKMARK;
CREATE USER QUICK_ACCESS_BOOKMARK;
GRANT ALL ON QUICK_ACCESS_BOOKMARK.* TO 'QUICK_ACCESS_BOOKMARK'@'%' identified by 'db_P@ssw0rd!';
USE QUICK_ACCESS_BOOKMARK;

CREATE TABLE bookmark
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    description VARCHAR(255),
    name VARCHAR(255),
    created_by VARCHAR(255) NOT NULL,
    date_created DATETIME NOT NULL,
    date_modified DATETIME,
    status VARCHAR(255),
    url VARCHAR(255) NOT NULL,
    bookmark_category_id BIGINT(20) NOT NULL,
    subcategory_id BIGINT(20) NOT NULL
);

CREATE TABLE bookmark_category
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    date_created DATETIME NOT NULL,
    date_modified DATETIME,
    parent_id BIGINT(20)
);

CREATE TABLE bookmark_category_bookmarks
(
    bookmark_category_id BIGINT(20) NOT NULL,
    bookmarks_id BIGINT(20) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (bookmark_category_id, bookmarks_id)
);
