package com.koweg.market.crypto

import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors

@Service
class CryptoMarketService{

	val orderIdGen = AtomicLong(1)

	val orderRepository = mutableMapOf(
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 900, OrderType.SELL, CoinType.ETHERIUM, 74.25, 111.11),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 901, OrderType.SELL, CoinType.ETHERIUM, 25.75, 111.11),
			orderIdGen.get() to OrderResponse(orderIdGen.getAndIncrement(), 903, OrderType.BUY, CoinType.LITECOIN, 77.75, 7.00)
	)


	fun placeOrder(request: OrderRequest): Long {
		val orderId = orderIdGen.get()
		orderRepository.putIfAbsent(orderId, OrderResponse(orderIdGen.getAndIncrement(), request.userId.trim().toLong(), OrderType.valueOf(request.orderType.trim()), CoinType.valueOf(request.coinType.trim()), request.quantity.trim().toDouble(), request.price.trim().toDouble()))
		return orderId
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