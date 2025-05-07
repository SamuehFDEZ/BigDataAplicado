#steps using pyflink
# source myenv/bin/activate
# deactivate

# Import required modules from the pyflink.table package
from pyflink.table import EnvironmentSettings, TableEnvironment, TableDescriptor, Schema, DataTypes
# Import column operations for table expressions
from pyflink.table.expressions import col

# ---- Step 1: Setting up the Table Environment ----
# Initialize environment settings for a streaming TableEnvironment
env_settings = EnvironmentSettings.in_streaming_mode()
# Create a TableEnvironment using the streaming settings
table_env = TableEnvironment.create(env_settings)


''' Ejercicio 1. Crea la tabla con los datos simulados u lizando el conector “datagen” que contenga
 los siguientes campos:- iden ficador de po entero siendo una secuencia de 1 a 10- temperatura de po double cuya temperatura mínima sea 15 y temperatura máxima sea
 35.
 En la configuración del conector añadir ‘rows-per-second’=5  para generar datos cada 5 segundos.'''

# Defining the table 'random_source' using TableDescriptor
table_env.create_temporary_table(
    'sensores',
    # Specifying the connector as 'datagen'
    TableDescriptor.for_connector('datagen')
        # Building the schema for the table
        .schema(Schema.new_builder()
                # Defining the 'id' column with TINYINT data type
                .column('id', DataTypes.TINYINT())
                # Defining the 'data' column with TINYINT data type
                .column('temperature', DataTypes.TINYINT())
                .build())
        # Setting sequence options for 'id' column to generate values from 1 to 3
        .option('fields.id.kind', 'sequence')
        .option('fields.id.start', '1')
        .option('fields.id.end', '10')
        # Setting sequence options for 'data' column to generate values from 4 to 6
        .option('fields.temperature.kind', 'sequence')
        .option('fields.temperature.start', '15')
        .option('fields.temperature.end', '35')
        .option('rows-per-second','5')
        .build())


# Fetching the created table into a table object
table = table_env.from_path("sensores")

# Executing and printing the table's data
table.execute().print()

''' Ejercicio 2: Muestra sólo aquellos sensores cuya temperatura sea mayor a 30ºC y muestra la
 información como salida. (pista: filter)'''
table2 = table.filter(col('temperature') > 30)

table2.execute().print()

'''Ejercicio 3: Simula un error aumentando todas las temperaturas en 2 grados (pista: sumar 2 a la
 columna tempertura) y muestra la información como salida.'''

table3 = table.select(col('id'), col('temperature') + 2)

table3.execute().print()

''' Ejercicio 4. Crea un nueva tabla de sensores críticos utilizando “import DataTypes” con los
 identificadores 3, 4 y 7.'''

# Creando la tabla de sensores críticos con los IDs específicos
sensores_criticos = table_env.from_elements(
    [(3,), (4,), (7,)],
    DataTypes.ROW([
        DataTypes.FIELD("id", DataTypes.INT())
    ])
)

sensores_criticos.execute().print()

''' Ejercicio 5. Realiza la unión entre los sensores simulados y los crí cos mostrando sólo sus
 temperaturas. Para ello u liza:
 CREATE TEMPORARY VIEW 
JOIN
 FROM_PATH'''

table_env.create_temporary_view('sensores_simulados', table_env)

table_env.create_temporary_view('sensores_criticos', sensores_criticos)

# Realizando la unión mediante SQL
table6 = table_env.execute_sql("""
    SELECT s.temperatura
    FROM sensores_simulados s
    JOIN sensores_criticos c
    ON s.id = c.sensor_id
""")

table6.print()
