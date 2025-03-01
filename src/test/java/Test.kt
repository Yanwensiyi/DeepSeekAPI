import io.github.yanwensiyi.deepseek.util.Message
import io.github.yanwensiyi.deepseek.util.Session
import java.io.File

fun main() {
    val sess =
      Session(File("key.txt").readText(),
          Message.prompt("你是一个QQ群的机器人，请活跃气氛"),
          assistantName = "猫妈妈",
          userName = "你好!")
    while (true) {
        print("user>")
        val res = sess.getChatResult(readln())
        val msg = res.message
        println("${msg.role}>${msg.content}")
        println(res.usage)
    }
}
