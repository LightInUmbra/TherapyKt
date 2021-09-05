
enum class BotActions {
    SEND_MESSAGE, NEXT_PAGE, PREVIOUS_PAGE
}


class MessageActions(val description: String, val action: BotActions = BotActions.SEND_MESSAGE)

interface Page {
    val title: String
    val messages: MutableList<BotActions>
}


interface BotPaginator<T: Any> {
    var curPage: Int
    var pageSelection: Map<Int, Page>

    fun nextPage()
    fun previousPage()

    fun addResponse(botResponse: BotResponse<T>)
}

interface BotResponse<T: Any>: BotPaginator<T> {
    fun action(): BotActions
}

abstract class BotResponser(val message: String): BotResponse<BotResponser>


class Start(message: String) : BotResponser(message) {
    override var pageSelection: Map<Int, Page> = mapOf()
    override var curPage: Int = 0

    override fun action(): BotActions {
       return BotActions.SEND_MESSAGE
    }

    override fun nextPage() {
        ++curPage
    }

    override fun previousPage() {
        --curPage
    }

    override fun addResponse(botResponse: BotResponse<BotResponser>) {
//        pageSelection[curPage].add(botResponse)
        TODO("Experimental somewhat not working right now.")
    }
}

fun main() {

    val test = mapOf(
        0 to listOf(Start("test"))
    )

    Start("d").action()
}