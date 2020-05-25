package br.com.fioalpha.heromarvel.presentation.list_characters.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewData
import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class ListCharacterFragment: Fragment() {

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
            recycler = findViewById(R.id.characters_recycler)
            loader = findViewById(R.id.characters_progress)
            errorView = findViewById(R.id.character_error)
            emptyView = findViewById(R.id.empty_view_custom)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
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

    private fun setupRecycler() {
        recycler.apply {
            adapter = this@ListCharacterFragment.adapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@ListCharacterFragment.context, 2)
        }
    }

    private fun handleFavoriteClick(characterViewData: CharacterViewData, position: Int) {
        viewModel.handleFavorite(characterViewData, position)
            .doOnNext { adapter.updateData(it.first.transform(), it.second) }
            .subscribe()
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

}

