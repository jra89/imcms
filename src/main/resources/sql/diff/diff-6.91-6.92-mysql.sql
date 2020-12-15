SET @schema_version__major_new = 6;
SET @schema_version__minor_new = 92;

UPDATE imcms_doc_i18n_meta SET headline = TRIM(headline);

UPDATE database_version
SET major = @schema_version__major_new,
    minor = @schema_version__minor_new;