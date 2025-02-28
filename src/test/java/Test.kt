import io.github.yanwensiyi.deepseek.util.Message
import io.github.yanwensiyi.deepseek.util.Session
import java.io.File

fun main() {
    val sess = Session(File("key.txt").readText(), Message.prompt("你是一个QQ群的机器人，请活跃气氛", "机器人"))
    while (true) {
        print("user>")
        val ln = readln().split(" ")
        if (ln[0] == "q")
            return
        val res = sess.getChatResult(Message.user(ln[1], ln[0]))
        val msg = res.message
        println("${msg.role}>${msg.content}")
        println(res.usage)
    }
}
