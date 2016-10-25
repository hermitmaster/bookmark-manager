-- Users
INSERT INTO users (password, registration_date, username, enabled) VALUES
  ('password', '2016-10-16 22:52:58', 'admin', true);

-- Roles
INSERT INTO authorities (username, authority) VALUES
  ('admin', 'admin');

-- Bookmark Categories
INSERT INTO bookmark_category (created_by_username, date_created, date_modified, name, parent_id) VALUES
  ('admin', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'None', null),
  ('admin', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'Search Engines', 1),
  ('admin', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'Social Networks', 1),
  ('admin', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'Technology', 1),
  ('admin', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'Science', 1),
  ('admin', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'Education', 1);

-- Bookmarks
INSERT INTO bookmark (created_by_username, bookmark_category_id, subcategory_id, status, date_created, date_modified, last_validated, description, name, url) VALUES
  ('admin', 2, 1, 'ACTIVE', '2016-10-16 00:00:00', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'A popular search engine.', 'Google', 'http://www.google.com'),
  ('admin', 3, 1, 'ACTIVE', '2016-10-16 00:00:00', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'MySpace 2.0.', 'Facebook', 'http://www.facebook.com'),
  ('admin', 3, 1, 'ACTIVE', '2016-10-16 00:00:00', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'A social networking site for professionals.', 'LinkedIn', 'http://www.linkedin.com'),
  ('admin', 2, 1, 'ACTIVE', '2016-10-16 00:00:00', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'A somewhat popular search engine.', 'DuckDuckGo', 'http://www.duckduckgo.com'),
  ('admin', 4, 1, 'ACTIVE', '2016-10-16 00:00:00', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'A site for programming Q and A.', 'Stack Overflow', 'http://www.stackoverflow.com'),
  ('admin', 4, 1, 'ACTIVE', '2016-10-16 00:00:00', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'Technology news', 'Tech Republic', 'http://www.techrepublic.com'),
  ('admin', 4, 1, 'ACTIVE', '2016-10-16 00:00:00', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'A computer hardware enthusiast community.', 'Overclock', 'http://www.overclock.net'),
  ('admin', 6, 1, 'ACTIVE', '2016-10-16 00:00:00', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'A site for learning programming.', 'Hacker Rank', 'http://www.hackerrank.com'),
  ('admin', 6, 1, 'ACTIVE', '2016-10-16 00:00:00', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'A site with classes on various topics.', 'Khan Academy', 'http://www.khanacademy.com'),
  ('admin', 5, 1, 'ACTIVE', '2016-10-16 00:00:00', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'Space!', 'NASA', 'http://www.nasa.gov'),
  ('admin', 1, 1, 'ACTIVE', '2016-10-16 00:00:00', '2016-10-16 00:00:00', '2016-10-16 00:00:00', 'A free web encyclopedia.', 'Wikipedia', 'http://www.wikipedia.com');
