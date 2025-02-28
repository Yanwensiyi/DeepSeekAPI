package io.github.yanwensiyi.deepseek.util

import org.json.JSONObject

data class Message(
    val role: String,
    val content: String,
    var name: String?
) {
    companion object {
        @JvmStatic
        fun prompt(content: String, name: String? = null) = Message("system", content, name)
        @JvmStatic
        fun user(content: String, name: String? = null) = Message("user", content, name)
        @JvmStatic
        fun assistant(content: String, name: String? = null) = Message("assistant", content, name)
        @JvmStatic
        fun tool(content: String, name: String? = null) = Message("tool", content, name)
    }

    var raw: String? = null
        private set

    constructor(obj: JSONObject) : this(
        obj.getString("role"),
        obj.getString("content"),
        if (obj.has("name")) obj.getString("name") else null
    )

    constructor(raw: String) : this(JSONObject(raw)) {
        this.raw = raw
    }

    val length
        get() = content.length
    val json
        get() = jsonObject {
            "role" to role
            "content" to content
            if (name != null)
                "name" to name
        }
}