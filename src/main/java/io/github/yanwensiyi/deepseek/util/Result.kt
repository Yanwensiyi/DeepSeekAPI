package io.github.yanwensiyi.deepseek.util

import org.json.JSONObject

data class Result(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage
) {
    data class Choice(
        val index: Int,
        val message: Message,
        val logprobs: JSONObject?
    )

    data class Usage(
        val promptTokens: Int,
        val completionTokens: Int,
        val totalTokens: Int,
        val promptCacheHitTokens: Int,
        val promptCacheMissTokens: Int
    )

    var raw: String? = null
        private set
    val message get() = choices.first().message

    constructor(obj: JSONObject) : this(
        obj.getString("id"),
        obj.getString("object"),
        obj.getLong("created"),
        obj.getString("model"),
        ArrayList<Choice>().apply {
            val choices = obj.getJSONArray("choices")
            for (i in 0 until choices.length()) {
                val choice = choices.getJSONObject(i)
                add(Choice(
                    choice.getInt("index"),
                    Message(choice.getJSONObject("message")),
                    choice.get("logprobs") as? JSONObject
                ))
            }
        }, obj.getJSONObject("usage").let {
            Usage(
                it.getInt("prompt_tokens"),
                it.getInt("completion_tokens"),
                it.getInt("total_tokens"),
                it.getInt("prompt_cache_hit_tokens"),
                it.getInt("prompt_cache_miss_tokens")
            )
        }
    )

    constructor(raw: String) : this(JSONObject(raw)) {
        this.raw = raw
    }
}