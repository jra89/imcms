CREATE PROCEDURE StartDocSet @meta_id INT AS
/**
	Changes the start document
**/

UPDATE sys_data SET value = @meta_id WHERE sys_id = 0

IF @@ROWCOUNT = 0 BEGIN
		SET IDENTITY_INSERT sys_types ON
		INSERT INTO sys_types (type_id, name) VALUES(0, 'StartDocument')
		SET IDENTITY_INSERT sys_types OFF
		SET IDENTITY_INSERT sys_data ON
		INSERT INTO sys_data (sys_id, type_id, value) VALUES(0, 0, @meta_id)
		SET IDENTITY_INSERT sys_data OFF
END


;
