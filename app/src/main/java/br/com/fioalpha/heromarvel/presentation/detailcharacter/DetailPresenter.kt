package br.com.fioalpha.heromarvel.presentation.detailcharacter

import androidx.annotation.DrawableRes
import br.com.fioalpha.heromarvel.R
import br.com.fioalpha.heromarvel.core.utils.SchedulerRx
import br.com.fioalpha.heromarvel.domain.HandleFavoriteCharacterUseCase
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import br.com.fioalpha.heromarvel.presentation.detailcharacter.model.CharacterDetailViewData
import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DetailPresenter(
    private val view: DetailsView,
    private val scheduler: SchedulerRx,
    private val handleFavoriteCharacterUseCase: HandleFavoriteCharacterUseCase
) {

    private val disposable = CompositeDisposable()

    fun loadData(characterDetail: CharacterViewData?) {
        characterDetail?.let {
            val character = CharacterDetailViewData.transform(it) { isFavorite ->
                getIconFavorite(isFavorite)
            }
            view.hideError()
            view.showData(character)
            view.updateFavorite(character)
        } ?: view.showError("Dados incorretos")
    }

    fun handleClickFavorite(character: CharacterDetailViewData?) {
        character?.let {
            disposable.add(handleFavorite(character))
        }?: view.showError("Dados incorretos")

    }


    @DrawableRes
    fun getIconFavorite(favorite: Boolean): Int {
        return if (favorite) R.drawable.favorite
            else R.drawable.un_favorite
    }

    fun CharacterDomain.transform() = CharacterDetailViewData(
        id = id,
        name = name,
        descriptions = description,
        favorite = favorite,
        imagePath = imagePath,
        iconFavorite = getIconFavorite(favorite)
    )

    fun unbind() {
        disposable.clear()
    }

    private fun handleFavorite(character: CharacterDetailViewData): Disposable {
        view.hideError()
        return handleFavoriteCharacterUseCase.setCharacter(CharacterDomain.transform(character))
            .execute()
            .subscribeOn(scheduler.ioSchedule())
            .observeOn(scheduler.mainSchedule())
            .map { it.transform() }
            .subscribe(
                {
                    view.updateFavorite(it)
                },
                { view.showError(it.message.orEmpty()) },
                {}
            )
    }

}

