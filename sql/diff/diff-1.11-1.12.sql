-- diff-1.11-1.12.sql

BEGIN TRANSACTION
SET QUOTED_IDENTIFIER ON
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
SET ARITHABORT ON
SET NUMERIC_ROUNDABORT OFF
SET CONCAT_NULL_YIELDS_NULL ON
SET ANSI_NULLS ON
SET ANSI_PADDING ON
SET ANSI_WARNINGS ON
COMMIT
BEGIN TRANSACTION
CREATE UNIQUE NONCLUSTERED INDEX IX_users_login_name ON dbo.users
	(
	login_name
	) ON [PRIMARY]
GO
COMMIT

-- 2004-11-12 Lennart �

ALTER TABLE roles ADD CONSTRAINT roles_role_name UNIQUE ( role_name )

-- 2004-11-15 Kreiger

ALTER TABLE users ADD CONSTRAINT users_login_name UNIQUE ( login_name )

-- 2004-11-18 Kreiger
