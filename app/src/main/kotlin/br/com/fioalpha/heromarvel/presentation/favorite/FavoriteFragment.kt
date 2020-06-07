package br.com.fioalpha.heromarvel.presentation.favorite

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fioalpha.heromarvel.R
import br.com.fioalpha.heromarvel.core.utils.goToDetailsCharacter
import br.com.fioalpha.heromarvel.core.utils.hide
import br.com.fioalpha.heromarvel.core.utils.show
import br.com.fioalpha.heromarvel.core.views.EmptyViewCustom
import br.com.fioalpha.heromarvel.core.views.ErrorViewCustom
import br.com.fioalpha.heromarvel.presentation.favorite.model.FavoriteIntent
import br.com.fioalpha.heromarvel.presentation.favorite.model.FavoriteState
import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.CharactersAdapter
import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class FavoriteFragment: Fragment() {

    private val adapterCharacter: CharactersAdapter = CharactersAdapter(
        get(),
        ::handleFavoriteClick,
        ::handleItemClick
    )

    private val disposable = CompositeDisposable()

    private val viewModel: FavoriteViewModel by inject()

    private lateinit var recycler: RecyclerView

    private lateinit var loader: ProgressBar

    private lateinit var errorView: ErrorViewCustom

    private lateinit var emptyView: EmptyViewCustom

    private val deleteIntent = PublishSubject.create<FavoriteIntent.DeleteIntent>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_characters_favorite, container, false)
        setupView(view)
        setupRecycler()


        return view
    }

    private fun setupView(view: View) {
        with(view) {
            recycler = findViewById(R.id.list_recycler)
            loader = findViewById(R.id.list_progress)
            errorView = findViewById(R.id.list_error)
            emptyView = findViewById(R.id.list_empty)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposable.add(
            viewModel.states()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(::render)
                .subscribe()
        )
        disposable.addAll(viewModel.processIntents(intents()))
    }

    private fun setupRecycler() {
        recycler.apply {
            this.adapter = adapterCharacter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun render(status: FavoriteState) {
        when (status) {
            FavoriteState.Loading -> handlerLoadingStatusView()
            FavoriteState.EmptyView ->  handleEmptyStatusView()
            is FavoriteState.Success -> handleDataStatusView(status.data)
            is FavoriteState.Fail -> handleErrorStatusView(status.message)
            is FavoriteState.Remove -> adapterCharacter.remove(status.position)
        }
    }


    private fun handleFavoriteClick(characterViewData: CharacterViewData, position: Int) {
       deleteIntent.onNext(FavoriteIntent.DeleteIntent(characterViewData, position))
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
        disposable.dispose()
    }

    private fun intents(): Observable<FavoriteIntent> {
        return Observable.merge(initialIntent(), deleteIntent)
    }

    private fun initialIntent(): Observable<FavoriteIntent> {
        return Observable.just(FavoriteIntent.LoaderIntent)
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

    private fun handleDataStatusView(data: List<CharacterViewData>) {
        loader.hide()
        recycler.show()
        errorView.hide()
        emptyView.hide()
        adapterCharacter.add(data)
    }

    private fun handleErrorStatusView(message: String) {
        loader.hide()
        recycler.hide()
        errorView.show()
        emptyView.hide()
        errorView.setMessage(message)
    }

}

