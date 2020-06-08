package br.com.fioalpha.heromarvel.presentation.listcharacters.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fioalpha.heromarvel.COLUMNS
import br.com.fioalpha.heromarvel.DEBOUNCE_TIMEOUT
import br.com.fioalpha.heromarvel.EndlessScrollListener
import br.com.fioalpha.heromarvel.R
import br.com.fioalpha.heromarvel.SKIP_ITEM
import br.com.fioalpha.heromarvel.core.utils.goToDetailsCharacter
import br.com.fioalpha.heromarvel.core.utils.hide
import br.com.fioalpha.heromarvel.core.utils.show
import br.com.fioalpha.heromarvel.core.utils.transform
import br.com.fioalpha.heromarvel.core.views.EmptyViewCustom
import br.com.fioalpha.heromarvel.core.views.ErrorViewCustom
import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewData
import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewStatus
import com.jakewharton.rxbinding2.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class ListCharacterFragment : Fragment(), SearchView.OnQueryTextListener {

    private val adapter: CharactersAdapter = CharactersAdapter(
        get(),
        ::handleFavoriteClick,
        ::handleItemClick
    )

    private val disposable = CompositeDisposable()

    private val viewModel: CharactersViewModel by inject()

    private lateinit var recycler: RecyclerView

    private lateinit var loader: ProgressBar

    private lateinit var errorView: ErrorViewCustom

    private lateinit var emptyView: EmptyViewCustom

    private lateinit var moreLoadView: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_characters, container, false)
        with(view) {
            recycler = findViewById(R.id.list_recycler)
            loader = findViewById(R.id.list_progress)
            errorView = findViewById(R.id.list_error)
            emptyView = findViewById(R.id.list_empty)
            moreLoadView = findViewById(R.id.list_more_progress)
        }
        setupRecycler()
        return view
    }

    private val endlessScrollListener: EndlessScrollListener by lazy {
        object : EndlessScrollListener(recycler.layoutManager as GridLayoutManager) {
            override fun onLoadMore(): Boolean {
                disposable.add(
                    viewModel.moreItem()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(::render)
                        .subscribe()
                )
                return true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menu_search)
            .run {
                this.actionView?.let {
                    setupSearchView(it as SearchView)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        recycler.addOnScrollListener(endlessScrollListener)
        disposable.add(
            viewModel.loadData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(::render)
                .subscribe()
        )
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
        recycler.removeOnScrollListener(endlessScrollListener)
    }

    private fun render(characterViewStatus: CharacterViewStatus) {
        when (characterViewStatus) {
            CharacterViewStatus.LOADING -> handlerLoadingStatusView()
            CharacterViewStatus.EMPTY -> handleEmptyStatusView()
            CharacterViewStatus.MOREITEMLOADING -> handleMoreItem()
            is CharacterViewStatus.AddItem -> handleAddNewItems(characterViewStatus)
            is CharacterViewStatus.Data -> handleDataStatusView(characterViewStatus)
            is CharacterViewStatus.Error -> handleErrorStatusView(characterViewStatus.message)
        }
    }

    private fun handleAddNewItems(data: CharacterViewStatus.AddItem) {
        moreLoadView.hide()
        loader.hide()
        recycler.show()
        errorView.hide()
        emptyView.hide()
        adapter.add(data.data)
        endlessScrollListener.enableMore()
    }

    private fun handleMoreItem() {
        moreLoadView.show()
    }

    private fun setupSearchView(searchView: SearchView) {
        RxSearchView.queryTextChanges(searchView)
            .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            .skip(SKIP_ITEM)
            .switchMap {
                if (it.isEmpty()) endlessScrollListener.enableMore()
                else endlessScrollListener.disableMore()
                viewModel.search(it.toString()).subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(::render)
            .subscribe()
    }

    private fun setupRecycler() {
        recycler.apply {
            adapter = this@ListCharacterFragment.adapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@ListCharacterFragment.context, COLUMNS)
            addOnScrollListener(endlessScrollListener)
        }
    }

    private fun handleFavoriteClick(characterViewData: CharacterViewData, position: Int) {
        disposable.add(
            viewModel.handleFavorite(characterViewData, position)
                .doOnNext { adapter.updateData(it.first.transform(), it.second) }
                .subscribe()
        )
    }

    private fun handleItemClick(characterViewData: CharacterViewData) {
        goToDetailsCharacter(characterViewData)
    }

    private fun handlerLoadingStatusView() {
        loader.show()
        recycler.hide()
        errorView.hide()
        emptyView.hide()
    }

    private fun handleEmptyStatusView() {
        loader.hide()
        recycler.hide()
        errorView.hide()
        emptyView.show()
    }

    private fun handleDataStatusView(data: CharacterViewStatus.Data) {
        moreLoadView.hide()
        loader.hide()
        recycler.show()
        errorView.hide()
        emptyView.hide()
        adapter.loadData(data.data)
    }

    private fun handleErrorStatusView(message: String) {
        loader.hide()
        recycler.hide()
        errorView.show()
        emptyView.hide()
        errorView.setMessage(message)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.search(query.orEmpty())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(::render)
            .subscribe()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}
