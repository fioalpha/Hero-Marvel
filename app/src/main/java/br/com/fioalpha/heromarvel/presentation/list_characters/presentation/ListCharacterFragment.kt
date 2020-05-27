package br.com.fioalpha.heromarvel.presentation.list_characters.presentation

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fioalpha.heromarvel.R
import br.com.fioalpha.heromarvel.core.utils.goToDetailsCharacter
import br.com.fioalpha.heromarvel.core.utils.hide
import br.com.fioalpha.heromarvel.core.utils.show
import br.com.fioalpha.heromarvel.core.utils.transform
import br.com.fioalpha.heromarvel.core.views.EmptyViewCustom
import br.com.fioalpha.heromarvel.core.views.ErrorViewCustom
import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewData
import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewStatus
import com.jakewharton.rxbinding2.widget.RxSearchView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class ListCharacterFragment: Fragment(), SearchView.OnQueryTextListener  {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_characters, container, false)
        with(view) {
            recycler = findViewById(R.id.search_recycler)
            loader = findViewById(R.id.search_progress)
            errorView = findViewById(R.id.search_error)
            emptyView = findViewById(R.id.search_empty)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_toolbar, menu)
        val searchView = menu.findItem(R.id.menu_search)
            .run { this.actionView as SearchView }

        setupSearchView(searchView)
    }

    override fun onStart() {
        super.onStart()
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
    }

    private fun render(characterViewStatus: CharacterViewStatus) {
        when (characterViewStatus) {
            CharacterViewStatus.LOADING -> handlerLoadingStatusView()
            CharacterViewStatus.EMPTY ->  handleEmptyStatusView()
            is CharacterViewStatus.Data -> handleDataStatusView(characterViewStatus)
            is CharacterViewStatus.Error -> handleErrorStatusView(characterViewStatus.message)
        }
    }

    private fun setupSearchView(searchView: SearchView) {
        RxSearchView.queryTextChanges(searchView)
            .debounce(300, TimeUnit.MILLISECONDS)
            .skip(3)
            .switchMap { viewModel.search(it.toString()).subscribeOn(Schedulers.io()) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(::render)
            .subscribe()
    }

    private fun setupRecycler() {
        recycler.apply {
            adapter = this@ListCharacterFragment.adapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@ListCharacterFragment.context, 2)
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

    private fun handlerLoadingStatusView(){
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

