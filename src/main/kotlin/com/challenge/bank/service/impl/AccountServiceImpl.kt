package com.challenge.bank.service.impl

import com.challenge.bank.model.Account
import com.challenge.bank.repository.AccountRepository
import com.challenge.bank.service.AccountService
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.*

@Service
class AccountServiceImpl(private val repository: AccountRepository) : AccountService {

    override fun create(account: Account): Account {
        Assert.hasLength(account.name, "[nome] não pode ser branco")
        Assert.hasLength(account.document, "[documento] não pode ser branco")
        return repository.save(account)
    }

    override fun getAccounts(): List<Account> {
        return repository.findAll();
    }

    override fun getAccountById(id: Long): Optional<Account> {
        return repository.findById(id)
    }

    override fun update(id: Long, account: Account): Optional<Account> {
        return getAccountById(id).map {
            val accountToUpdate = it.copy(
                name = account.name,
                document = account.document,
                phone = account.phone
            )

            repository.save(accountToUpdate)
        }
    }

    override fun delete(id: Long) {
        getAccountById(id).map { repository.delete(it) }
            .orElseThrow { throw RuntimeException("Account id $id not found") }
    }
}