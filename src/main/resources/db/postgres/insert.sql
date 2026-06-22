INSERT INTO e_budget
({{columns}})
VALUES ({{values}})
ON CONFLICT (info_guid)
DO UPDATE SET
{{updates}};
