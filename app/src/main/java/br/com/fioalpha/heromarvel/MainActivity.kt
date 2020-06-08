package br.com.fioalpha.heromarvel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val bottomNavigation: BottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation(setupToolbar())
    }

    private fun setupNavigation(toolbar: Toolbar) {
        val navController = findNavController(R.id.host_nav)
        bottomNavigation.setupWithNavController(navController)
        val appConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        NavigationUI.setupWithNavController(toolbar, navController, appConfiguration)
    }

    private fun setupToolbar(): Toolbar {
        return findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
        }
    }
}

const val INIT_VALUE = 0
private const val STARTING = 0
abstract class EndlessScrollListener(
    private val layoutManager: GridLayoutManager
) : RecyclerView.OnScrollListener() {
    private var visibleItemCount = INIT_VALUE
    private var totalItemCount = INIT_VALUE
    private var pastVisibleItems = INIT_VALUE
    private var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > STARTING) {
            visibleItemCount = layoutManager.childCount
            totalItemCount = layoutManager.itemCount
            pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

            if (loading) {
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    loading = false
                    onLoadMore()
                }
            }
        }
    }

    fun enableMore() {
        loading = true
    }

    fun disableMore() {
        loading = false
    }

    abstract fun onLoadMore(): Boolean
}
