package com.koweg.market.crypto

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors

@SpringBootApplication
class CryptoMarketplaceApplication

fun main(args: Array<String>) {
	runApplication<CryptoMarketplaceApplication>(*args)
}

enum class CoinType (val type: String){
	LITECOIN("LITECOIN"), ETHERIUM("ETHERIUM")
}

enum class OrderType(val type: String) {
	BUY("BUY"), SELL("SELL")
}

data class OrderResponse(val orderId: Long,
						 val userId: Long,
						 val orderType: OrderType,
						 val coinType: CoinType,
						 val quantity: Double,
						 val price: Double)

data class OrderRequest(var userId: String,
						var orderType: String,
						var coinType: String,
						var quantity: String,
						var price: String)

data class OrderSummary(val quantity: Double,
						val price: Double)

@RestController
@RequestMapping("/api")
class CryptoOrderController{

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

	@PostMapping("/orders", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
	fun placeOrder(@RequestBody request: OrderRequest): ResponseEntity<String> {
		orderRepository.putIfAbsent(orderIdGen.get(), OrderResponse(orderIdGen.getAndIncrement(), request.userId.trim().toLong(), OrderType.valueOf(request.orderType.trim()), CoinType.valueOf(request.coinType.trim()), request.quantity.trim().toDouble(), request.price.trim().toDouble()))
		return ResponseEntity.ok("ok")
	}

	@DeleteMapping("/orders/{orderId}")
	fun cancelOrder(@PathVariable orderId: Long): HttpEntity<String> {
		orderRepository.remove(orderId)
		return ResponseEntity.ok("ok")
	}

	@GetMapping("/orders/summary/{type}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
	fun getOrdersSummary(@PathVariable type: String): List<OrderSummary> {
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