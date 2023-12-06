package com.challenge.bank.service

import com.challenge.bank.model.Account
import java.util.Optional
import javax.swing.text.html.Option

interface AccountService {
    fun create(account: Account): Account

    fun getAccounts(): List<Account>

    fun getAccountById(id: Long): Optional<Account>

    fun update(id:Long, account: Account): Optional<Account>

    fun delete(id: Long)
}