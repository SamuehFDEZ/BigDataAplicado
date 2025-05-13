# group_by using api table
# Import the necessary modules for Environment Settings and Table Environment
from pyflink.table import EnvironmentSettings, TableEnvironment
# Import the column selection function
from pyflink.table.expressions import col

'''
 Ejercicio 1. 
 Configurar PyFlink en modo batch y crear dos tablas temporales a partir de datos simulados de la
 tabla de usuarios y de la tabla views (visualizaciones)
 '''

# Set the environment to batch mode for batch processing of queries
env_settings = EnvironmentSettings.in_batch_mode()
# Initialize the TableEnvironment for query execution
table_env = TableEnvironment.create(env_settings)

usuarios = table_env.from_elements(
    [(1, 'Alice', 'USA'),
     (2, 'Bob', 'Canada'),
     (3, 'Carla', 'USA'),
     (4, 'David', 'Mexico'),
     (5, 'Alice', 'Canada'),],
    ['user_id', 'name', 'country']
)

view = table_env.from_elements(
    [(101, 1, 'A1', 10, '2024-12-01'),
     (102, 2, 'A2', 30, '2024-12-02'),
     (103, 3, 'A2', 15, '2024-12-03'),
     (104, 4, 'A1', 5, '2024-12-03'),
     (105, 5 ,'A3', 20, '2024-12-04'),
     (106, 6, 'A1', 50, '2024-12-04'),
     (107, 7, 'A4', 25, '2024-12-05'),
     (108, 8, 'A2', 35, '2024-12-05'),
     (109, 9, 'A3', 40, '2024-12-06'),
     (110, 10, 'A1', 60, '2024-12-06')],
    ['view_id', 'user_id', 'video_id', 'watch_me_minutes', 'date']
)

# usuarios.execute().print()
# view.execute().print()

'''
 Ejercicio 2. 
Calcular el tiempo total de visualización por usuario usando Table API 
'''
table_env.execute_sql(
    """
    CREATE TABLE view_total_minutes AS
    SELECT u.user_id, SUM(v.watch_me_minutes) AS total_minutes
    FROM view v
    INNER JOIN usuarios u
    ON v.user_id = u.user_id
    GROUP BY u.user_id;
    """
)

'''
Ejercicio 3. 
Obtener el país con mayor tiempo de visualización total usando SQL 
'''
table3 = table_env.execute_sql("""
    SELECT pais
    FROM usuarios u 
    INNER JOIN view v 
    ON u.user_id = v.user_id
    WHERE v.watch_me_minutes = (
        SELECT MAX(watch_me_minutes) 
        FROM view
    )
    GROUP BY pais
    ORDER BY pais DESC
    LIMIT 1;
""")

'''
Ejercicio 4. 
Promedio de empo de visualización por video usando Table API 
'''



'''
Ejercicio 5. 
Contar cuántos usuarios únicos vieron cada video (SQL) 
'''