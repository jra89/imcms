
ALTER TABLE images_cache ADD meta_id integer;
ALTER TABLE images_cache ADD no integer;
ALTER TABLE images_cache ADD file_no varchar(100);
-- 1-default, 2-force, 3-less_than, 4-greater_than, 5-percent
ALTER TABLE images_cache ADD resize integer;
