'''Enunciado:
 En esta práctica debes realizar un análisis financiero de una empresa internacional que opera en
 varios países de Europa. El equipo de Contabilidad ha detectado que los ingresos (ingresos) de
 ciertas regiones fueron reportados con un error donde algunos países sus valores han sido
 mul plicados por 2.
 Tenéis una tabla de pedidos (ordenes) que incluye los nombres de los clientes, su país y el ingreso
 por pedido donde debéis de realizar el ajuste a esos ingresos antes de que los datos se trasladen a
 otro sistema.'''

'''
Pista: Debéis de realizar UDF definida como función lamda en PyFlink que corrija los ingresos.
 Los puntos a desarrollar en la prác ca son los siguientes:'''

from pyflink.table import EnvironmentSettings, TableEnvironment, TableDescriptor, Schema
from pyflink.table import DataTypes
from pyflink.table.udf import udf
import pandas as pd

'''
  Ejercicio 1. Crea un entorno en modo batch para procesamiento por lotes.
'''
# Initialize a batch processing environment. Batch mode is selected for finite, bounded data processing.
env_settings = EnvironmentSettings.in_batch_mode()
table_env = TableEnvironment.create(env_settings)

'''
 Ejercicio 2. Define la tabla orders con los siguientes datos:
 - Alicia, España, 1500
 - Roberto, Alemania, 2300
 - Esther, Francia, 1239
'''
orders = table_env.from_elements(
    [('Alicia', 'España', 1500), ('Roberto', 'Alemania', 2300), ('Esther', 'Francia', 1239)],
    ['name', 'country', 'revenue']
)
orders.execute().print()

'''
Ejercicio 3. Crea una UDF por Pandas, definida con lambda:
- columnas nombre, país e ingreso- multiplica por 2 el ingreso
- devuelve un DataFreame con nombre e ingreso ajustado
'''

map_function = udf(
    lambda x: pd.concat([x.name, x.country, x.revenue * 2], axis=1),
    result_type=DataTypes.ROW(
        [DataTypes.FIELD("name", DataTypes.STRING()),
         DataTypes.FIELD("revenue", DataTypes.BIGINT())]
    ),
    func_type="pandas"  # Use Pandas for vectorized operations, efficient for batch processing.
)

''' 
Ejercicio 4. Aplica esta función a la tabla

 Ejercicio 5. Imprime el resultado final con los ingresos corregidos.
'''


orders.map(map_function).execute().print()

