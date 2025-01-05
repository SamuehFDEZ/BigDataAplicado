with source_customers as (
    select * from {{ref('stg_customers')}}
),

source_orders as (
    select * from {{ref('stg_orders')}}
),

source_payments as (
    select * from {{ref('stg_payments')}}
),

final as (
    select c.customer_id, 
        count(p.order_id) as number_of_orders,
        sum(p.amount) as total_amount, 
        (select min(order_date) from source_orders where customer_id = c.customer_id) as first_order,
        (select max(order_date) from source_orders where customer_id = c.customer_id) as most_recent_order,
        (select count(order_id) from source_orders where customer_id = c.customer_id) as number_of_orders
    from source_customers c, 
        source_orders o, 
        source_payments p
    where c.customer_id = o.customer_id and 
        p.order_id = o.order_id
    group by c.customer_id

)

select * from final