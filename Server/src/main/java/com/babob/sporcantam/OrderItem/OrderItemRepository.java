package com.babob.sporcantam.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Integer> {
    @Query(
            value = "SELECT * FROM order_item u WHERE u.shopping_cartid = ?1",nativeQuery = true)
    Collection<OrderItem> getItemsById(Long id);

    @Query(
            value = "SELECT * FROM cart_item u WHERE u.shopping_cartid = ?2 AND u.uuid = ?1",nativeQuery = true)
    Collection<OrderItem> getOrderItems( String order_id);
}



