package com.example

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.example.constant.CONTENT_TYPE_HEADER_KEY
import com.example.constant.JSON_HEADER_VALUE
import com.example.constant.MP3_HEADER_VALUE
import com.example.error.ErrorResponse
import com.google.gson.GsonBuilder
import java.util.Base64

class Handler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private val mapper = GsonBuilder().setPrettyPrinting().create()
    private val mp3FilePath = "/1-second-of-silence.mp3"

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        val logger = context.logger

        logRequestParams(input, context)

        val textParam = input.extractTextParam()
        logger.log("text param value=[$textParam]")

        if (textParam == null) {
            val errorResponse = ErrorResponse(400, "text param is null")

            return APIGatewayProxyResponseEvent()
                .withStatusCode(400)
                .withHeaders(
                    mapOf(
                        CONTENT_TYPE_HEADER_KEY to JSON_HEADER_VALUE
                    )
                )
                .withBody(mapper.toJson(errorResponse))
        } else {
            val mp3FileData = getMp3FileData()
            val base64EncodedMp3 = Base64.getEncoder().encodeToString(mp3FileData)

            return APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(
                    mapOf(
                        CONTENT_TYPE_HEADER_KEY to MP3_HEADER_VALUE
                    )
                )
                .withIsBase64Encoded(true)
                .withBody(base64EncodedMp3)
        }
    }

    private fun logRequestParams(input: APIGatewayProxyRequestEvent, context: Context) {
        val logger = context.logger

        // log execution details
        logger.log("ENVIRONMENT VARIABLES: " + mapper.toJson(System.getenv()))
        logger.log("CONTEXT: " + mapper.toJson(context))
        // process event
        logger.log("EVENT: " + mapper.toJson(input))
    }

    private fun getMp3FileData(): ByteArray =
        this::class.java.getResource(mp3FilePath)?.readBytes() ?: ByteArray(0)

    /**
     * if no queryStringParameters has passed, APIGatewayProxyRequestEvent.queryStringParameters is null
     */
    private fun APIGatewayProxyRequestEvent.extractTextParam(): String? {
        val queryStringParameters = this.queryStringParameters
        return if (queryStringParameters != null) {
            this.queryStringParameters["text"]
        } else
            null
    }
}
