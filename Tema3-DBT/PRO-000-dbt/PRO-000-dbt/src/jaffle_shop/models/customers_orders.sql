with source_customers as (
    select * from {{ref('stg_customers')}}
),

source_orders as (
    select * from {{ref('stg_orders')}}
),

final as (
    select c.customer_id,
        c.first_name || ' ' || c.last_name as customer_full_name,
        (select min(order_date) from source_orders where customer_id = c.customer_id) as first_order,
        (select max(order_date) from source_orders where customer_id = c.customer_id) as most_recent_order,
        (select count(order_id) from source_orders where customer_id = c.customer_id) as number_of_orders
    from source_customers c, source_orders o 
    where c.customer_id = o.customer_id
)

select * from final