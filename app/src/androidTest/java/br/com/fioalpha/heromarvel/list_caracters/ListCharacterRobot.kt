package br.com.fioalpha.heromarvel.list_caracters

import androidx.test.espresso.ViewInteraction
import br.com.fioalpha.heromarvel.R
import br.com.fioalpha.heromarvel.core.BaseTestRobot


class ListCharacterRobot: BaseTestRobot() {

    fun loadingIsShowed() = isShow(R.id.list_progress)

    fun recyclerIsShowed() = isShow(R.id.list_recycler)

    fun recyclerCountItem(count: Int): ViewInteraction = countItem(R.id.list_recycler, count)

    fun titleItem(position: Int, text: String): ViewInteraction = itemViewInRecycler(
        R.id.list_recycler, R.id.character_adapter_title, position, text
    )

    fun emptyViewIsShowed() = isShow(R.id.empty_view_image)

    fun emptyViewIsHide() = isHide(R.id.empty_view_image)

    fun errorViewIsShowed() = isShow(R.id.error_custom_image)

    fun errorViewIsHide() = isHide(R.id.error_custom_message)

    fun messageError(message: String) = matchText(R.id.error_custom_message, message)

}

fun listCharacters(func: ListCharacterRobot.() -> Unit)  = ListCharacterRobot().apply {
    func()
}
