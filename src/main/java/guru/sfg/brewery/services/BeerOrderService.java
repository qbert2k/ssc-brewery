package guru.sfg.brewery.services;

import guru.sfg.brewery.web.model.BeerOrderDto;
import guru.sfg.brewery.web.model.BeerOrderPagedList;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BeerOrderService {

    BeerOrderPagedList listOrders(UUID customerId, Pageable pageable);

    BeerOrderPagedList listOrders(Pageable pageable);

    BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto);

    BeerOrderDto getOrderById(UUID customerId, UUID orderId);

    BeerOrderDto getOrderById(UUID orderId);

    void pickupOrder(UUID customerId, UUID orderId);
}
