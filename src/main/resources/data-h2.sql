-- Users
INSERT INTO users (password, registration_date, username, enabled) VALUES ('password', '2016-10-16 22:52:58', 'admin', true);

-- Roles
INSERT INTO authorities (username, authority) VALUES ('admin', 'admin');

-- Bookmark Categories
INSERT INTO bookmark_category (name, date_created, date_modified, parent_id, created_by_username) VALUES ('None', '2016-10-16 22:52:58', '2016-10-16 22:52:58', null, 'admin');
INSERT INTO bookmark_category (name, date_created, date_modified, parent_id, created_by_username) VALUES ('Search Engines', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 1, 'admin');
INSERT INTO bookmark_category (name, date_created, date_modified, parent_id, created_by_username) VALUES ('Social Networks', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 1, 'admin');
INSERT INTO bookmark_category (name, date_created, date_modified, parent_id, created_by_username) VALUES ('Technology', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 1, 'admin');
INSERT INTO bookmark_category (name, date_created, date_modified, parent_id, created_by_username) VALUES ('Science', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 1, 'admin');
INSERT INTO bookmark_category (name, date_created, date_modified, parent_id, created_by_username) VALUES ('Education', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 1, 'admin');

-- Bookmarks
INSERT INTO bookmark (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_username, subcategory_id) VALUES ('A popular search engine.', 'Google', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 'ACTIVE', 'http://www.google.com', 2, 'admin', 1);
INSERT INTO bookmark (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_username, subcategory_id) VALUES ('MySpace 2.0.', 'Facebook', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 'ACTIVE', 'http://www.facebook.com', 3, 'admin', 1);
INSERT INTO bookmark (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_username, subcategory_id) VALUES ('Facebook for people with jobs.', 'LinkedIn', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 'ACTIVE', 'http://www.linkedin.com', 3, 'admin', 1);
INSERT INTO bookmark (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_username, subcategory_id) VALUES ('A somewhat popular search engine.', 'DuckDuckGo', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 'ACTIVE', 'http://www.duckduckgo.com', 2, 'admin', 1);
INSERT INTO bookmark (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_username, subcategory_id) VALUES ('A site for programming Q and A.', 'Stack Overflow', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 'ACTIVE', 'http://www.stackoverflow.com', 4, 'admin', 1);
INSERT INTO bookmark (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_username, subcategory_id) VALUES ('Technology news', 'Tech Republic', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 'ACTIVE', 'http://www.techrepublic.com', 4, 'admin', 1);
INSERT INTO bookmark (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_username, subcategory_id) VALUES ('A computer hardware enthusiast community.', 'Overclock', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 'ACTIVE', 'http://www.overclock.net', 4, 'admin', 1);
INSERT INTO bookmark (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_username, subcategory_id) VALUES ('A site for learning programming.', 'Hacker Rank', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 'ACTIVE', 'http://www.hackerrank.com', 6, 'admin', 1);
INSERT INTO bookmark (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_username, subcategory_id) VALUES ('A site with classes on various topics.', 'Khan Academy', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 'ACTIVE', 'http://www.khanacademy.com', 6, 'admin', 1);
INSERT INTO bookmark (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_username, subcategory_id) VALUES ('Space!', 'NASA', '2016-10-16 22:52:58', '2016-10-16 22:52:58', 'ACTIVE', 'http://www.nasa.gov', 5, 'admin', 1);
