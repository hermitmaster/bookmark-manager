INSERT INTO user
  (username, password, registration_date)
VALUES
  ('admin', 'password', NOW());

INSERT INTO bookmark_category
  (name, date_created, date_modified, parent_id, created_by_id)
VALUES
  ('Search Engine', NOW(), NOW(), NULL, 1),
  ('Social Network', NOW(), NOW(), NULL, 1),
  ('Technology', NOW(), NOW(), NULL, 1),
  ('Science', NOW(), NOW(), NULL, 1),
  ('Education', NOW(), NOW(), NULL, 1);

INSERT INTO bookmark
  (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_id, subcategory_id)
VALUES
  ('A popular search engine.', 'Google', NOW(), NOW(), 'ACTIVE', 'http://www.google.com', 1, 1, null),
  ('MySpace 2.0.', 'Facebook', NOW(), NOW(), 'ACTIVE', 'http://www.facebook.com', 2, 1, null),
  ('Facebook for people with jobs.', 'LinkedIn', NOW(), NOW(), 'ACTIVE', 'http://www.linkedin.com', 2, 1, null),
  ('A somewhat popular search engine.', 'DuckDuckGo', NOW(), NOW(), 'ACTIVE', 'http://www.duckduckgo.com', 1, 1, null),
  ('A site for programming Q and A.', 'Stack Overflow', NOW(), NOW(), 'ACTIVE', 'http://www.stackoverflow.com', 3, 1, null),
  ('Technology news', 'Tech Republic', NOW(), NOW(), 'ACTIVE', 'http://www.techrepublic.com', 3, 1, null),
  ('A computer hardware enthusiast community.', 'Overclock', NOW(), NOW(), 'ACTIVE', 'http://www.overclock.net', 3, 1, null),
  ('A site for learning programming.', 'Hacker Rank', NOW(), NOW(), 'ACTIVE', 'http://www.hackerrank.com', 5, 1, null),
  ('A site with classes on various topics.', 'Khan Academy', NOW(), NOW(), 'ACTIVE', 'http://www.khanacademy.com', 5, 1, null),
  ('Space!', 'NASA', NOW(), NOW(), 'ACTIVE', 'http://www.nasa.gov', 4, 1, null);

# INSERT INTO bookmark_category_bookmarks (bookmark_category_id, bookmarks_id) SELECT bookmark_category_id, id FROM bookmark;