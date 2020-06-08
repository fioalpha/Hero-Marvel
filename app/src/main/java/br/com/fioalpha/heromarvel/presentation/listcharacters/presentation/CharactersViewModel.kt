package br.com.fioalpha.heromarvel.presentation.listcharacters.presentation

import br.com.fioalpha.heromarvel.DEBOUNCE_TIMEOUT
import br.com.fioalpha.heromarvel.INIT_PAGE
import br.com.fioalpha.heromarvel.NUMBER_MULTIPLE_PAGE
import br.com.fioalpha.heromarvel.core.utils.transform
import br.com.fioalpha.heromarvel.domain.FetchCharactersUseCase
import br.com.fioalpha.heromarvel.domain.HandleFavoriteCharacterUseCase
import br.com.fioalpha.heromarvel.domain.IsCharacterFavoriteUseCase
import br.com.fioalpha.heromarvel.domain.SearchHeroUseCase
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewData
import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewStatus
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import java.util.concurrent.TimeUnit

class CharactersViewModel(
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val handleFavoriteCharacterUseCase: HandleFavoriteCharacterUseCase,
    private val isCharacterFavoriteUseCase: IsCharacterFavoriteUseCase,
    private val searchHeroUseCase: SearchHeroUseCase
) {

    private var quantity: Int = INIT_PAGE

    fun loadData(page: Int = INIT_PAGE): Observable<CharacterViewStatus> = fetchCharactersUseCase.page(page)
        .execute()
        .doOnNext { quantity ++ }
    .compose(handleCharacter())

    fun search(term: String): Observable<CharacterViewStatus> = searchHeroUseCase.setTerm(term)
        .execute()
        .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
        .doOnNext { quantity = INIT_PAGE }
        .compose(handleCharacter())

    fun handleFavorite(characterViewData: CharacterViewData, position: Int): Observable<Pair<CharacterDomain, Int>> {
        return handleFavoriteCharacterUseCase.setCharacter(characterViewData.transform())
            .execute()
            .onErrorReturn { characterViewData.transform() }
            .map { Pair(it, position) }
    }

    fun moreItem() = fetchCharactersUseCase.page(quantity * NUMBER_MULTIPLE_PAGE)
            .execute()
            .doOnNext { quantity ++ }
            .flatMap { isCharacterFavoriteUseCase.setCharacters(it).execute() }
            .map(::transform)
            .map(::addNewItem)
            .onErrorReturn(::handleError)
            .cast(CharacterViewStatus::class.java)
            .startWith(CharacterViewStatus.MOREITEMLOADING)

    private fun addNewItem(items: List<CharacterViewData>): CharacterViewStatus {
        return CharacterViewStatus.AddItem(items)
    }

    private fun handleQuantityItem(items: List<CharacterViewData>): CharacterViewStatus = if (items.isEmpty()) {
        CharacterViewStatus.EMPTY } else { CharacterViewStatus.Data(items) }

    private fun handleCharacter() = ObservableTransformer<List<CharacterDomain>, CharacterViewStatus> {
        item -> item.flatMap { isCharacterFavoriteUseCase.setCharacters(it).execute() }
        .map(::transform)
        .map(::handleQuantityItem)
        .onErrorReturn(::handleError)
        .cast(CharacterViewStatus::class.java)
        .startWith(CharacterViewStatus.LOADING) }

    private fun transform(items: List<CharacterDomain>): List<CharacterViewData> {
        return items.transform { it.transform() }
    }

    private fun handleError(error: Throwable) = CharacterViewStatus.Error(error.message.orEmpty())
}
