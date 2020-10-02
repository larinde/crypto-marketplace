package com.koweg.market.crypto

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

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

