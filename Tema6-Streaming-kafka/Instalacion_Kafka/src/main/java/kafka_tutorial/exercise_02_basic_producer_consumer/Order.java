package kafka_tutorial.exercise_02_basic_producer_consumer;

import java.util.List;

public class Order {
    public String orderId;
    public String customerName;
    public List<String> items;
    public double total;

    public Order(String orderId, String customerName, List<String> items, double total) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = items;
        this.total = total;
    }
}