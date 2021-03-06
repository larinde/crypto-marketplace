package com.koweg.market.crypto

import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify

@ExtendWith(SpringExtension::class)
@WebMvcTest(CryptoOrderController::class)
class CryptoOrderControllerTest (@Autowired val mockMvc: MockMvc){

    @MockkBean
    private lateinit var cryptoMarketService: CryptoMarketService

    @Test
    fun `should succesfully place an order and receive an ok response`() {
        every { cryptoMarketService.placeOrder(any()) } returns 300L

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"1\",\"orderType\":\"SELL\",\"coinType\":\"ETHERIUM\",\"quantity\": \"1000\", \"price\":\"370.99\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().string(containsString("orderId")))

        verify { cryptoMarketService.placeOrder(any()) }

    }

    @Test
    fun `should succesfully cancel a registered order and receive an ok response`() {
        every { cryptoMarketService.cancelOrder(any()) } returns Unit

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/orders/{orderId}", "999"))
                .andExpect(MockMvcResultMatchers.status().isOk)

        verify { cryptoMarketService.cancelOrder(any()) }

    }

    @Test
    fun `should retrieve summary information of live SELL orders`(){
        var response = mutableListOf(OrderSummary(317.0, 137.51),
                OrderSummary(33.0, 900.31))
        every { cryptoMarketService.getOrdersSummary(any()) } returns response

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/orders/summary/{type}", "SELL")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("\$.[0]quantity").value("317.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.[0]price").value("137.51"))

        verify { cryptoMarketService.getOrdersSummary(any()) }
    }

}