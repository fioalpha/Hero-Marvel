package br.com.fioalpha.heromarvel.presentation.detail_character

import br.com.fioalpha.heromarvel.R
import br.com.fioalpha.heromarvel.core.utils.SchedulerRx
import br.com.fioalpha.heromarvel.core.utils.transform
import br.com.fioalpha.heromarvel.domain.HandleFavoriteCharacterUseCase
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import br.com.fioalpha.heromarvel.presentation.detail_character.model.CharacterDetailViewData
import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewData
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailPresenterTest {

    private val detailView: DetailsView = mock()

    private val handleFavoriteCharacterUseCase: HandleFavoriteCharacterUseCase = mock()

    private val schedulerRx: SchedulerRx = mock()

    private lateinit var detailPresenter: DetailPresenter

    private val characterViewData = CharacterViewData(
        1, "TEST", "NAME", "DESCRIPTION", false
    )

    private val characterDetail = CharacterDetailViewData(
        1, "NAME", "DESCRIPTION", "TEST", false
    )

    @Before
    fun setup() {
        detailPresenter = DetailPresenter(
            detailView,
            schedulerRx,
            handleFavoriteCharacterUseCase
        )

        whenever(schedulerRx.ioSchedule()).thenReturn(Schedulers.trampoline())
        whenever(schedulerRx.mainSchedule()).thenReturn(Schedulers.trampoline())

    }

    @After
    fun tearDown () {
        verifyNoMoreInteractions(detailView)
    }

    @Test
    fun `when called load Character Then load data on view`() {
        detailPresenter.loadData(characterViewData)
        verify(detailView).showData(CharacterDetailViewData.transform(characterViewData) {
            R.drawable.un_favorite
        })
        verify(detailView).hideError()
        verify(detailView).updateFavorite(any())
    }

    @Test
    fun `when called load Character nullable Then load error`() {
        detailPresenter.loadData(null)
        verify(detailView).showError("Dados incorretos")
    }

    @Test
    fun `when called handle favorite With character favorite false Then change favorite status call update favorite ` () {

        whenever(handleFavoriteCharacterUseCase.setCharacter(CharacterDomain.transform(characterDetail)))
            .thenReturn(handleFavoriteCharacterUseCase)
        whenever(handleFavoriteCharacterUseCase.execute()).thenReturn(
            Observable.just(characterViewData.transform().copy(favorite = true))
        )

        detailPresenter.handleClickFavorite(characterDetail)

        verify(detailView).updateFavorite(
            characterDetail.copy(favorite = true, iconFavorite = detailPresenter.getIconFavorite(true))
        )
        verify(detailView).hideError()
        verify(detailView).updateFavorite(any())
    }

}