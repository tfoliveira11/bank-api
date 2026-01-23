package com.challenge.bank.controller

import com.challenge.bank.model.Account
import com.challenge.bank.repository.AccountRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var repository: AccountRepository

    @Test
    fun `test find all`() {
        repository.save(Account(name = "Test", document = "", phone = ""))

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].document").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].phone").isString)
            .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `test find by id`() {
        val account = repository.save(Account(name = "Test", document = "99999999999", phone = "997055558"))

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts/${account.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isMap)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(account.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(account.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.document").value(account.document))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(account.phone))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create account`() {
        val account = Account(name = "Test Create Account", document = "99999999999", phone = "997055558")
        val json = ObjectMapper().writeValueAsString(account)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/accounts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf())
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(account.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.document").value(account.document))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(account.phone))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test update account`() {
        val account = repository.save(Account(name = "Test Update", document = "30010198921", phone = "996582632"))
            .copy(name = "Updated Name")
        val json = ObjectMapper().writeValueAsString(account)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/accounts/${account.id}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())

        val updatedAccount: Optional<Account> = repository.findById(account.id!!)
        Assertions.assertTrue(updatedAccount.isPresent)
        Assertions.assertEquals(account.name, updatedAccount.get().name)
    }

    @Test
    fun `test delete account`() {
        val account = repository.save(Account(name = "Test Delete", document = "30010198921", phone = "996582632"))

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/accounts/${account.id}").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isOk)

        val deletedAccount: Optional<Account> = repository.findById(account.id!!)
        Assertions.assertFalse(deletedAccount.isPresent)
    }

    @Test
    fun `test create account error empty name`() {
        val account = Account(name = "", document = "", phone = "")
        val json = ObjectMapper().writeValueAsString(account)
        val createAccountRequest = MockMvcRequestBuilders.post("/v1/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())

        mockMvc.perform(createAccountRequest)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[nome] não pode ser branco"))
            .andDo(MockMvcResultHandlers.print())
    }
}