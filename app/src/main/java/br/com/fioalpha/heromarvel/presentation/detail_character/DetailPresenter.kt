package br.com.fioalpha.heromarvel.presentation.detail_character

import androidx.annotation.DrawableRes
import br.com.fioalpha.heromarvel.R
import br.com.fioalpha.heromarvel.core.utils.SchedulerRx
import br.com.fioalpha.heromarvel.domain.HandleFavoriteCharacterUseCase
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import br.com.fioalpha.heromarvel.presentation.detail_character.model.CharacterDetailViewData
import io.reactivex.disposables.CompositeDisposable

class DetailPresenter(
    private val view: DetailsView,
    private val scheduler: SchedulerRx,
    private val handleFavoriteCharacterUseCase: HandleFavoriteCharacterUseCase
) {

    private val disposable = CompositeDisposable()

    private var characterDetail: CharacterDetailViewData? = null

    fun loadData(characterDetail: CharacterDetailViewData?) {
        this.characterDetail = characterDetail
        characterDetail?.let {
            view.showData(characterDetail)
        } ?: view.showError("Dados incorretos")
    }

    fun handleClickFavorite() {
        disposable.add(
            handleFavoriteCharacterUseCase.setCharacter(CharacterDomain.transform(characterDetail!!))
                .execute()
                .subscribeOn(scheduler.ioSchedule())
                .observeOn(scheduler.mainSchedule())
                .map { it.favorite }
                .doOnNext {
                    this.characterDetail = this.characterDetail?.copy(favorite = it)
                }
                .subscribe(
                    { view.updateFavorite(getIconFavorite()) },
                    { view.showError(it.message.orEmpty()) },
                    {}
                )
        )
    }

    fun handleStatusFavorite() {
        view.updateFavorite(getIconFavorite())
    }

    @DrawableRes
    private fun getIconFavorite(): Int {
        return if (characterDetail?.favorite == true)
            R.drawable.favorite
            else
            R.drawable.un_favorite
    }
}