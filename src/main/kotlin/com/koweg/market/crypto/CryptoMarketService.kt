package com.koweg.market.crypto

import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors

@Service
class CryptoMarketService{

	val orderIdGen = AtomicLong(1)

	val orderRepository = mutableMapOf(
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 1, OrderType.SELL, CoinType.ETHERIUM, 100.25, 250.00),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 2, OrderType.SELL, CoinType.LITECOIN, 50.5, 7.00),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 3, OrderType.SELL, CoinType.ETHERIUM, 100.5, 300.00),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 5, OrderType.SELL, CoinType.LITECOIN, 100.50, 250.00),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 4, OrderType.BUY, CoinType.ETHERIUM, 100.5, 25.125),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 3, OrderType.BUY, CoinType.LITECOIN, 100.5, 25.125),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 1, OrderType.BUY, CoinType.LITECOIN, 50.00, 37.73),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 9, OrderType.BUY, CoinType.LITECOIN, 10.00, 3.21),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 3, OrderType.SELL, CoinType.ETHERIUM, 70.75, 13.35),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 4, OrderType.SELL, CoinType.ETHERIUM, 100.5, 25.125),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 5, OrderType.SELL, CoinType.ETHERIUM, 100.5, 25.125)
	)


	fun placeOrder(request: OrderRequest): Unit {
		orderRepository.putIfAbsent(orderIdGen.get(), OrderResponse(orderIdGen.getAndIncrement(), request.userId.trim().toLong(), OrderType.valueOf(request.orderType.trim()), CoinType.valueOf(request.coinType.trim()), request.quantity.trim().toDouble(), request.price.trim().toDouble()))
	}

	fun cancelOrder(orderId: Long){
		orderRepository.remove(orderId)
	}

	fun getOrdersSummary(type: String): List<OrderSummary> {
		return when {
			(type.trim() == OrderType.SELL.type) -> {
				orderRepository.values
						.stream()
						.filter { o -> o.orderType.type == type.trim() }
						.collect(
								Collectors.groupingBy(
										OrderResponse::price,
										Collectors.summingDouble(OrderResponse::quantity)
								)
						).entries.map { OrderSummary(it.value, it.key) }.sortedBy { it.price }
			}
			(type.trim() == OrderType.BUY.type) -> {
				orderRepository.values
						.stream()
						.filter { o -> o.orderType.type == type.trim() }
						.collect(
								Collectors.groupingBy(
										OrderResponse::price,
										Collectors.summingDouble(OrderResponse::quantity)
								)
						).entries.map { OrderSummary(it.value, it.key) }.sortedByDescending { it.price }
			}
			else -> emptyList()
		}
	}

}