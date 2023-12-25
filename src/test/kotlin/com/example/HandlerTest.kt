package com.example

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.Test
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just

@ExtendWith(MockKExtension::class)
class HandlerTest {

    @InjectMockKs
    lateinit var handler: Handler

    @MockK
    lateinit var context: Context

    @MockK
    lateinit var lambdaLogger: LambdaLogger

    @Test
    fun handleRequest() {
        val input = APIGatewayProxyRequestEvent()

        every { context.logger } returns lambdaLogger
        every { lambdaLogger.log(any<String>()) } just Runs

        val response = handler.handleRequest(input, context)

        response.statusCode shouldBe 200
        response.body.shouldNotBeNull()
    }
}
