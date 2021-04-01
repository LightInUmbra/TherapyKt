package kvm.pack

import com.google.gson.Gson
import java.io.PrintWriter
import java.net.URL
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

data class Response(var bad: HashSet<String> = HashSet(), var good: HashSet<String> = HashSet(), var fitness: Int = 0)

private val allResponses = hashSetOf<Response>()
fun response(lambda: Response.() -> Unit): Response {
    val response = Response()
    response.apply(lambda)
    return response
}

// Loads & Reads JSON Configuration
fun loadConfig() =
    URL("https://raw.githubusercontent.com/TechnoInventor/TherapyKt/master/messages.json").readText().let {
        Gson().fromJson(it, Response::class.java).let { resp ->
            allResponses += arrayListOf(
                response {
                    bad = resp.bad
                    fitness = 1
                },
                response {
                    bad = resp.good
                    fitness = 0
                }
            )
        }
    }

// Name input, loops if no kvm.pack.response
fun createName() {
    "Please enter a name!".newLine().let {
        if (it.isBlank()) createName() else "Great to meet you, $it".print()
    }
}

// Asks how you're doing. Loops if you don't say yes or no
fun askMood() {
    "So, How is everything?".newLine().let { result ->
        allResponses.firstOrNull { it.good.contains(result) || it.bad.contains(result) }.let {
            if (it == null) {
                "Please answer with yes or no".print()
                askMood()
                return
            }
            if (it.fitness == 0) goodMood() else badMood()
        }
    }
}

fun String.breakPause() {
    println(this)
    Thread.sleep(1000L)
}

// Leaves a happy message if user is okay
fun goodMood(): Unit = "That's so awesome to hear! I hope the rest of your day stays well!".breakPause().let {
    exitProcess(0)
}

// Moves forward with program to determine what's wrong with the user
fun badMood() = """Oh, I'm sorry to hear. Would you like to vent?
Venting is useful when you're in a bad state of mind.
It's better to let stress out in a positive way, like writing or
singing. So how about it? Would you like to type about it?
I'm all ears!
yes or no?""".newLine().let {
    if (it == "yes") {
        "Great! Please make sure to type exit when you're done.\n\n".print()
        freeText()
    } else badMoodCont()
}

val lines = arrayListOf<String>()

fun String.newLine(): String {
    println(String.format("%s\n", this))
    return readLine()!!.split("\\s+".toRegex())[0].toLowerCase()
}

// Journal Entry, has option to save after typing
fun freeText() {
    while (true) {
        val line = readLine() ?: break
        lines += line

        if (line == "exit") {
            lines.remove("exit")
            break
        }
    }
    saveFile()
}

fun ArrayList<String>.sendLines(): String {
    this.forEach { println(String.format("%s\n", it)) }
    return "Press enter to exit".newLine()
}

// Decides whether to save or not
fun saveFile() {
    when ("Would you like to save this?\n[Yes]\t\t[No]".newLine()) {
        "yes" -> "Name Of File?".newLine().let {
            return@let PrintWriter(it).apply {
                write(lines.joinToString("\n"))
                flush()
                close()
                "Thank you for using the program! Please leave feedback by emailing\nmigsterfixer@gmail.com\nPress Enter To Exit: ".newLine()
            }
        }
        "no" -> arrayListOf(
            "Awh, well alright! I hope you were able to de-stress!",
            "Thank you for using the program! Please leave feedback by emailing\n".plus("migsterfixer@gmail.com")
        ).sendLines()
        else -> "Please type yes or no\n".breakPause().run { saveFile() }
    }
}

fun String.print() = println(this)

// Gives preset options for why user is in bad mood
fun badMoodCont() {
    """That's Alright. If you don't want to type it out, that's alright.
If you are in a really bad spot, you feel like harming yourself, or others,
don't hesitate. Call someone. Let someone know how you feel. The situation will
only get worse if you don't speak up. I encourage you to find help!
    """.print().let {
        arrayListOf("Thank you for using the program! Please leave feedback by emailing\n".plus("migsterfixer@gmail.com"), "Press Enter To Exit: ").sendLines()
    }
}

// Everyone knows what this does
fun main() {
    loadConfig()
    createName()
    askMood()
}
