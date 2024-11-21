package com.example.logistics.service;

import com.example.logistics.model.Order;
import com.example.logistics.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Order order) {
        order.setCreatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
    public List<Order> findOrdersByDateRange(String startDate, String endDate) {
        try {
            // Парсинг строк в LocalDate
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            // Конвертация LocalDate в LocalDateTime для начала и конца диапазона
            LocalDateTime startDateTime = start.atStartOfDay(); // Начало дня
            LocalDateTime endDateTime = end.atTime(23, 59, 59); // Конец дня

            // использования JPA для фильтрации по дате
            return orderRepository.findByCreatedAtBetween(startDateTime, endDateTime);
        } catch (DateTimeParseException e) {
            // Обработка ошибки парсинга
            System.out.println("Ошибка парсинга даты: " + e.getMessage());
            return List.of(); // Возвращаем пустой список в случае ошибки
        }
    }


}
