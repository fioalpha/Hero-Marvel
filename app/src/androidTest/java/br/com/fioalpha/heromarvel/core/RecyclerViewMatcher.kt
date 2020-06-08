package br.com.fioalpha.heromarvel.core

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class RecyclerViewMatcher(private val recyclerViewId: Int) {
    fun atPosition(position: Int): Matcher<View?>? {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View?>? {
        return object : TypeSafeMatcher<View?>() {
            var resources: Resources? = null
            var childView: View? = null
            override fun describeTo(description: Description?) {
                resources?.let {
                    val text = try { it.getResourceName(recyclerViewId) } catch (ex: Resources.NotFoundException) { "RecyclerView resource not found" }
                    description?.appendText(text)
                }
            }

            override fun matchesSafely(view: View?): Boolean {
                resources = view?.resources

                if (childView == null) {
                    val recyclerView = view?.rootView?.findViewById(recyclerViewId) as RecyclerView
                    childView = if (recyclerView.id == recyclerViewId) {
                        recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                    } else {
                        return false
                    }
                }
                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView: View = childView!!.findViewById(targetViewId)
                    view === targetView
                }
            }
        }
    }
}
