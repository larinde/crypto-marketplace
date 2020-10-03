package com.koweg.market.crypto

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.boot.test.mock.mockito.MockBean

@ExtendWith(SpringExtension::class)
@WebMvcTest(CryptoOrderController::class)
class CryptoOrderControllerTest {

    @Autowired
    lateinit private var mockMvc: MockMvc

    @MockBean
    private lateinit var cryptoMarketService: CryptoMarketService

    @Test
    fun `should succesfully place an order and receive an ok response`() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"1\",\"orderType\":\"SELL\",\"coinType\":\"ETHERIUM\",\"quantity\": \"1000\", \"price\":\"370.99\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("orderId")))
   }

    @Test
    fun `should succesfully cancel a registered order and receive an ok response`() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/orders/{orderId}", "999"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `should retrieve summary information of live SELL orders`(){
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/orders/summary/{type}", "SELL")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

}