SELECT *
FROM e_budget
WHERE updated_at >= ?
ORDER BY updated_at;
