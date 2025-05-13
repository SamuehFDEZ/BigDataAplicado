from pyflink.table import EnvironmentSettings, TableEnvironment
from pyflink.table.expressions import col

# Configurar entorno en modo batch
env_settings = EnvironmentSettings.in_batch_mode()
table_env = TableEnvironment.create(env_settings)

# Crear tabla de usuarios
usuarios = table_env.from_elements(
    [(1, 'Alice', 'USA'),
     (2, 'Bob', 'Canada'),
     (3, 'Carla', 'USA'),
     (4, 'David', 'Mexico'),
     (5, 'Alice', 'Canada')],
    ['user_id', 'name', 'country']
)

# Crear tabla de visualizaciones
view = table_env.from_elements(
    [(101, 1, 'A1', 10, '2024-12-01'),
     (102, 2, 'A2', 30, '2024-12-02'),
     (103, 3, 'A2', 15, '2024-12-03'),
     (104, 4, 'A1', 5, '2024-12-03'),
     (105, 5, 'A3', 20, '2024-12-04'),
     (106, 6, 'A1', 50, '2024-12-04'),
     (107, 7, 'A4', 25, '2024-12-05'),
     (108, 8, 'A2', 35, '2024-12-05'),
     (109, 9, 'A3', 40, '2024-12-06'),
     (110, 10, 'A1', 60, '2024-12-06')],
    ['view_id', 'user_id', 'video_id', 'watch_me_minutes', 'date']
)

# Registrar vistas temporales
table_env.create_temporary_view("usuarios", usuarios)
table_env.create_temporary_view("view", view)

print("Ejercicio 2 - Total minutos por usuario:")
result2 = ((view.join(usuarios).where(col("view.user_id") == col("usuarios.user_id"))
           .group_by(col("usuarios.user_id")))
           .select(col("usuarios.user_id"), col("watch_me_minutes").sum.alias("total_minutes")))

result2.execute().print()

print("Ejercicio 3 - País con mayor tiempo total de visualización:")
result3 = table_env.execute_sql("""
    SELECT country
    FROM (
        SELECT u.country, SUM(v.watch_me_minutes) AS total_watch_time
        FROM usuarios u
        JOIN view v ON u.user_id = v.user_id
        GROUP BY u.country
    )
    ORDER BY total_watch_time DESC
    LIMIT 1
""")
result3.execute().print()

print("Ejercicio 4 - Promedio de visualización por video:")
result4 = ((view.group_by(col("video_id"))
           .select(col("video_id"), col("watch_me_minutes").avg.alias("media_visualizacion")))
           .order_by(col("media_visualizacion").desc))

result4.execute().print()

print("Ejercicio 5 - Usuarios únicos por video:")
result5 = table_env.execute_sql("""
    SELECT video_id,
           COUNT(DISTINCT u.user_id) AS usuarios_unicos,
           SUM(v.watch_me_minutes) AS total_visualizacion
    FROM usuarios u
    JOIN view v ON u.user_id = v.user_id
    GROUP BY video_id
    ORDER BY total_visualizacion DESC
""")
result5.execute().print()

print("Ejercicio 6 - Top 3 usuarios por minutos vistos:")
result6 = table_env.execute_sql("""
    SELECT u.name, SUM(v.watch_me_minutes) AS total_minutes
    FROM usuarios u
    JOIN view v ON u.user_id = v.user_id
    GROUP BY u.name
    ORDER BY total_minutes DESC
    LIMIT 3
""")
result6.execute().print()

print("Ejercicio 7 - Día con más minutos visualizados:")
result7 = (((view.group_by(col("date"))
           .select(col("date"), col("watch_me_minutes").sum.alias("total_minutes")))
           .order_by(col("total_minutes").desc))
           .limit(1))

result7.execute().print()