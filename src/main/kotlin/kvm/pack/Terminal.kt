package kvm.pack


interface Pagination<T: Any> {
    fun getPage(): T?
    fun getAllPages(): List<T>
}

enum class PageType {
    SELECTION, TYPING
}

enum class Actions {
    SEND_MESSAGE, GO_TO_PAGE
}

interface IPage {
    val title: String?
    val type: PageType

    fun action(): Actions
}

data class Page(override val title: String?, override val type: PageType = PageType.SELECTION): IPage {
    private var options: Map<Int, String> = mapOf(
        0 to "Start therapy session",
        1 to "Exit"
    )

    override fun action(): Actions {
        println(title)
        for (it in options) println("[${it.key}] ${it.value}")
        return Actions.SEND_MESSAGE
    }
}

data class Bot(val name: String, val pager: Map<Int, Page>): Pagination<Page> {
    override fun getPage(): Page {
        return getAllPages()[0]
    }

    override fun getAllPages(): List<Page> {
        return emptyList()
    }
}

fun main() {
    val bot = Bot("test-bot", mapOf(0 to Page("Welcome to TherapyKt")))
    bot.pager[0]?.action()
}