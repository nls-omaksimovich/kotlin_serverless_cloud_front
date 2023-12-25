package com.example

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.google.gson.GsonBuilder
import java.util.*

class Handler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private val mapper = GsonBuilder().setPrettyPrinting().create()
    private val mp3FilePath = "/1-second-of-silence.mp3"

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        val logger = context.logger

        // log execution details
        logger.log("ENVIRONMENT VARIABLES: " + mapper.toJson(System.getenv()))
        logger.log("CONTEXT: " + mapper.toJson(context))
        // process event
        logger.log("EVENT: " + mapper.toJson(input))

        val mp3FileData = getMp3FileData()
        val base64EncodedMp3 = Base64.getEncoder().encodeToString(mp3FileData)

        return APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withHeaders(
                mapOf(
                    "Content-Type" to "audio/mpeg",
                    "Content-Encoding" to "base64"
                )
            )
            .withIsBase64Encoded(true)
            .withBody(base64EncodedMp3)
    }

    private fun getMp3FileData(): ByteArray =
        this::class.java.getResource(mp3FilePath)?.readBytes() ?: ByteArray(0)
}
