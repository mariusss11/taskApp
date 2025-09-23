

SELECT * FROM task_app.users;

SELECT * FROM task_app.tasks;

SELECT * FROM task_app.groups;

SELECT d
	g.name,
	u.email,
	t.title,
	t.description
FROM task_app.groups g
JOIN task_app.tasks t ON  g.id = t.group_id
JOIN task_app.users u ON g.user_id = u.id