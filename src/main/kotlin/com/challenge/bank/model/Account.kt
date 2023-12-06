package com.challenge.bank.model

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "account")
data class Account(
    @Id @GeneratedValue var id: Long? = null,
    val name: String,
    val document: String,
    val phone: String
)
