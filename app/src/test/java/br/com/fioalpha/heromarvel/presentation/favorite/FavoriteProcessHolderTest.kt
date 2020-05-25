package br.com.fioalpha.heromarvel.presentation.favorite

import br.com.fioalpha.heromarvel.core.utils.transform
import br.com.fioalpha.heromarvel.domain.FetchAllFavoriteUseCase
import br.com.fioalpha.heromarvel.domain.HandleFavoriteCharacterUseCase
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import br.com.fioalpha.heromarvel.presentation.favorite.model.FavoriteAction
import br.com.fioalpha.heromarvel.presentation.favorite.model.FavoriteResult
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoriteProcessHolderTest {

    private val handleFavoriteCharacterUseCase: HandleFavoriteCharacterUseCase = mock()

    private val fetchAllFavoriteUseCase: FetchAllFavoriteUseCase = mock()

    private lateinit var processHolder: FavoriteProcessHolder

    private val characters = listOf(
        CharacterDomain(
            1,
            "DESCRIPTION",
            "IMAGE",
            "NAME",
            false
        ),
        CharacterDomain(
            2,
            "DESCRIPTION",
            "IMAGE",
            "NAME",
            false
        ),
        CharacterDomain(
            3,
            "DESCRIPTION",
            "IMAGE",
            "NAME",
            false
        ),
        CharacterDomain(
            4,
            "DESCRIPTION",
            "IMAGE",
            "NAME",
            false
        )
    )

    @Before
    fun setup() {
        processHolder = FavoriteProcessHolder(
            handleFavoriteCharacterUseCase, fetchAllFavoriteUseCase
        )
    }

    @Test
    fun `when called actionsProcessor With loader action Then return character list`() {
        whenever(fetchAllFavoriteUseCase.execute()).thenReturn(Observable.just(characters))
        val value = Observable.just(FavoriteAction.Loader)
            .compose(processHolder.actionsProcessor)
            .test()
            .assertNoErrors()
            .assertComplete()
            .values()

        assertTrue(value[0] is FavoriteResult.FavoriteLoaderResult.Loader)
        assertTrue(value[1] is FavoriteResult.FavoriteLoaderResult.Success)
    }

    @Test
    fun `when called actionsProcessor With delete action Then return character list`() {
        whenever(fetchAllFavoriteUseCase.execute()).thenReturn(Observable.just(characters))
        whenever(handleFavoriteCharacterUseCase.setCharacter(characters.first()))
            .thenReturn(handleFavoriteCharacterUseCase)
        whenever(handleFavoriteCharacterUseCase.execute()).thenReturn(
            Observable.just(characters.first())
        )

        val value = Observable.just(
            FavoriteAction.Delete(characters.first().transform(), 1)
        )
            .compose(processHolder.actionsProcessor)
            .test()
            .assertNoErrors()
            .assertComplete()
            .values()

        assertTrue(value[0] is FavoriteResult.FavoriteDeleteResult.Success)
    }
}
