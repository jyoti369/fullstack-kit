package snippets.java.spring;

import jakarta.persistence.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

// ---- Entity ----
@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Version  // optimistic locking
    private int version;

    public void addItem(OrderItem item) { items.add(item); item.setOrder(this); }
    public void removeItem(OrderItem item) { items.remove(item); item.setOrder(null); }

    // getters/setters omitted for brevity
}

// ---- Repository ----
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Derived queries
    List<Order> findByCustomerIdOrderByCreatedAtDesc(String customerId);
    Optional<Order> findByIdAndCustomerId(Long id, String customerId);

    // JPQL
    @Query("SELECT o FROM Order o WHERE o.status = :status AND SIZE(o.items) > 0")
    List<Order> findActiveOrdersWithItems(@Param("status") OrderStatus status);

    // Native SQL
    @Query(value = "SELECT * FROM orders WHERE created_at > NOW() - INTERVAL '7 days'",
           nativeQuery = true)
    List<Order> findRecentOrders();

    // Paging
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    // Count
    long countByCustomerId(String customerId);

    // Bulk update
    @Modifying
    @Query("UPDATE Order o SET o.status = 'CANCELLED' WHERE o.customerId = :cid AND o.status = 'PENDING'")
    int cancelPendingOrdersByCustomer(@Param("cid") String customerId);
}

// ---- Service ----
@Service
@Transactional(readOnly = true)  // default read-only; write methods override
public class OrderService {
    private final OrderRepository repo;
    OrderService(OrderRepository repo) { this.repo = repo; }

    @Transactional
    public Order createOrder(String customerId, List<OrderItem> items) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.PENDING);
        items.forEach(order::addItem);
        return repo.save(order);
    }

    public Page<Order> getOrders(OrderStatus status, int page, int size) {
        return repo.findByStatus(status, PageRequest.of(page, size, Sort.by("id").descending()));
    }

    @Transactional
    public Order updateStatus(Long id, OrderStatus newStatus) {
        Order order = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(newStatus);
        return order; // dirty checking — auto-saved on commit
    }
}

enum OrderStatus { PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED }
