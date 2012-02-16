-- Update Account Data table
ALTER TABLE account_data
ADD `last_logout` bigint(21) default NULL;

