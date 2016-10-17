INSERT INTO user
  (username, password, registration_date)
VALUES
  ('admin', 'password', NOW());

INSERT INTO bookmark_category
  (name, date_created, date_modified, parent_id, created_by_id)
VALUES
  ('None', NOW(), NOW(), NULL, 1),
  ('Search Engines', NOW(), NOW(), 1, 1),
  ('Social Networks', NOW(), NOW(), 1, 1),
  ('Technology', NOW(), NOW(), 1, 1),
  ('Science', NOW(), NOW(), 1, 1),
  ('Education', NOW(), NOW(), 1, 1);

INSERT INTO bookmark
  (description, name, date_created, date_modified, status, url, bookmark_category_id, created_by_id, subcategory_id)
VALUES
  ('A popular search engine.', 'Google', NOW(), NOW(), 'ACTIVE', 'http://www.google.com', 2, 1, 1),
  ('MySpace 2.0.', 'Facebook', NOW(), NOW(), 'ACTIVE', 'http://www.facebook.com', 3, 1, 1),
  ('Facebook for people with jobs.', 'LinkedIn', NOW(), NOW(), 'ACTIVE', 'http://www.linkedin.com', 3, 1, 1),
  ('A somewhat popular search engine.', 'DuckDuckGo', NOW(), NOW(), 'ACTIVE', 'http://www.duckduckgo.com', 2, 1, 1),
  ('A site for programming Q and A.', 'Stack Overflow', NOW(), NOW(), 'ACTIVE', 'http://www.stackoverflow.com', 4, 1, 1),
  ('Technology news', 'Tech Republic', NOW(), NOW(), 'ACTIVE', 'http://www.techrepublic.com', 4, 1, 1),
  ('A computer hardware enthusiast community.', 'Overclock', NOW(), NOW(), 'ACTIVE', 'http://www.overclock.net', 4, 1, 1),
  ('A site for learning programming.', 'Hacker Rank', NOW(), NOW(), 'ACTIVE', 'http://www.hackerrank.com', 6, 1, 1),
  ('A site with classes on various topics.', 'Khan Academy', NOW(), NOW(), 'ACTIVE', 'http://www.khanacademy.com', 6, 1, 1),
  ('Space!', 'NASA', NOW(), NOW(), 'ACTIVE', 'http://www.nasa.gov', 5, 1, 1);