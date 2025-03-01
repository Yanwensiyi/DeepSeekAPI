package io.github.yanwensiyi.deepseek.util

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Duration

/**
 * Session used to handle a sequence of messages
 */
class Session(
    val key: String,
    var prompt: Message,
    var limit: Int = 1000,
    var temperature: Float = 1.3f,
    var maxTokens: Int = 1000,
    var model: String = "deepseek-chat",
    var assistantName: String? = null,
    var userName: String? = null
) {
    companion object {
        const val URL = "https://api.deepseek.com/chat/completions"
    }

    val client: OkHttpClient = OkHttpClient.Builder()
      .readTimeout(Duration.ofSeconds(60))
      .build()
    val messages = ArrayDeque<Message>()

    val length: Int
        get() {
            var l = 0
            l += prompt.length
            l += messages.sumOf { it.length }
            return l
        }
    val body
        get() = json {
            "model" to model
            "stream" to false
            "temperature" to temperature
            "max_tokens" to maxTokens
            "messages" to jsonArray().apply {
                put(prompt.json)
                while (length > limit)
                    messages.removeFirst()
                messages.map(Message::json).forEach(::put)
            }
        }.toRequestBody()

    fun getChatResult(msg: String) = getChatResult(Message.user(msg, userName))

    fun getChatResult(msg: Message): Result {
        messages += msg
        val req = Request.Builder()
          .url(URL)
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer $key")
          .post(body)
          .build()
        client.newCall(req).execute().use { resp ->
            val body = resp.body!!.string()
            val result = Result(body)
            messages += result.message.apply {
                name = this@Session.assistantName
            }
            return result
        }
    }
}