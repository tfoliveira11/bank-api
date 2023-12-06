package com.challenge.bank.controller.handler

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(request: HttpServletRequest, exception: Exception): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.message!!))
    }
}

data class ErrorResponse(val statusCode: Int, val message: String)