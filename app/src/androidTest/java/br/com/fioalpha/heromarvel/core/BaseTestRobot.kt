package br.com.fioalpha.heromarvel.core

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.fioalpha.heromarvel.core.CustomMatchers.Companion.withItemCount

abstract class BaseTestRobot {

    fun fillEditText(resId: Int, text: String): ViewInteraction =
        onView(withId(resId)).perform(ViewActions.replaceText(text), ViewActions.closeSoftKeyboard())

    fun clickButton(resId: Int): ViewInteraction = onView((withId(resId))).perform(ViewActions.click())

    fun textView(resId: Int): ViewInteraction = onView(withId(resId))

    fun matchText(viewInteraction: ViewInteraction, text: String): ViewInteraction = viewInteraction
        .check(matches(withText(text)))

    fun matchText(resId: Int, text: String): ViewInteraction = matchText(textView(resId), text)

    fun isShow(@IdRes resTd: Int): ViewInteraction = getView(resTd).check(matches(isDisplayed()))

    fun isHide(@IdRes resTd: Int): ViewInteraction = onView(withId(resTd)).check(matches(withEffectiveVisibility(
        ViewMatchers.Visibility.GONE)))

    fun getItemList(listRes: Int) = getView(listRes)

    fun countItem(@IdRes id: Int, count: Int) = getItemList(id).check(
        matches(withItemCount(count))
    )

    fun itemViewInRecycler(
        @IdRes idParent: Int,
        @IdRes idChildRecycler: Int,
        positions: Int,
        textMatchers: String
    ): ViewInteraction = onView(
        RecyclerViewMatcher(idParent)
        .atPositionOnView(positions, idChildRecycler))
        .check(matches(withText(textMatchers)))

    fun await(time: Long = 100) {
        Thread.sleep(time)
    }
    private fun getView(@IdRes resId: Int) = onView(withId(resId))
}
