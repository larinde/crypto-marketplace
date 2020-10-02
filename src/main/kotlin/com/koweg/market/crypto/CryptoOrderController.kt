package com.koweg.market.crypto

import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class CryptoOrderController(val cryptoMarketService: CryptoMarketService){

	@PostMapping("/orders", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
	fun placeOrder(@RequestBody request: OrderRequest): ResponseEntity<String> {
		cryptoMarketService.placeOrder(request)
		return ResponseEntity.ok("ok")
	}

	@DeleteMapping("/orders/{orderId}")
	fun cancelOrder(@PathVariable orderId: Long): HttpEntity<String> {
		cryptoMarketService.cancelOrder(orderId)
		return ResponseEntity.ok("ok")
	}

	@GetMapping("/orders/summary/{type}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
	fun getOrdersSummary(@PathVariable type: String): List<OrderSummary> {
		return cryptoMarketService.getOrdersSummary(type)
	}

}