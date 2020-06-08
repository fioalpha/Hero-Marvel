package br.com.fioalpha.heromarvel.presentation.listcharacters.presentation

import br.com.fioalpha.heromarvel.core.utils.transform
import br.com.fioalpha.heromarvel.domain.FetchCharactersUseCase
import br.com.fioalpha.heromarvel.domain.HandleFavoriteCharacterUseCase
import br.com.fioalpha.heromarvel.domain.IsCharacterFavoriteUseCase
import br.com.fioalpha.heromarvel.domain.MockCreator
import br.com.fioalpha.heromarvel.domain.SearchHeroUseCase
import br.com.fioalpha.heromarvel.domain.fromJson
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewStatus
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharactersViewModelTest {

    private val handleFavoriteCharacterUseCase: HandleFavoriteCharacterUseCase = mock()
    private val isFavoriteCharacterUseCase: IsCharacterFavoriteUseCase = mock()
    private val fetchCharactersUseCase: FetchCharactersUseCase = mock()
    private val searchHeroUseCase: SearchHeroUseCase = mock()

    private lateinit var charactersViewModel: CharactersViewModel
    private val CHARACTER_JSON = "characters_list.json"

    private val characterData: List<CharacterDomain> = Gson().fromJson<List<CharacterDomain>>(
        MockCreator().getMockText(CHARACTER_JSON))

    @Before
    fun setup() {
        charactersViewModel = CharactersViewModel(
            fetchCharactersUseCase,
            handleFavoriteCharacterUseCase,
            isFavoriteCharacterUseCase,
            searchHeroUseCase
        )
    }

    @Test
    fun `when load data With list of character nothing favorite`() {
        whenever(fetchCharactersUseCase.page(0)).thenReturn(fetchCharactersUseCase)
        whenever(fetchCharactersUseCase.execute()).thenReturn(Observable.just(characterData))
        whenever(isFavoriteCharacterUseCase.setCharacters(characterData)).thenReturn(isFavoriteCharacterUseCase)
        whenever(isFavoriteCharacterUseCase.execute()).thenReturn(Observable.just(characterData))

        val values = charactersViewModel.loadData()
            .test()
            .assertNoErrors()
            .assertComplete()
            .values()
        assertTrue(values[0] is CharacterViewStatus.LOADING)
        assertTrue(values[1] is CharacterViewStatus.Data)
        val items = values[1] as CharacterViewStatus.Data
        assertEquals(items.data.count(), 2)
    }

    @Test
    fun `when load data With empty list of character nothing favorite`() {
        whenever(fetchCharactersUseCase.page(0)).thenReturn(fetchCharactersUseCase)
        whenever(fetchCharactersUseCase.execute()).thenReturn(Observable.just(emptyList()))
        whenever(isFavoriteCharacterUseCase.setCharacters(emptyList())).thenReturn(isFavoriteCharacterUseCase)
        whenever(isFavoriteCharacterUseCase.execute()).thenReturn(Observable.just(emptyList()))

        val values = charactersViewModel.loadData()
            .test()
            .assertNoErrors()
            .assertComplete()
            .values()

        assertTrue(values[0] is CharacterViewStatus.LOADING)
        assertTrue(values[1] is CharacterViewStatus.EMPTY)
    }

    @Test
    fun `when load data With any exceptions`() {
        whenever(fetchCharactersUseCase.page(0)).thenReturn(fetchCharactersUseCase)
        whenever(fetchCharactersUseCase.execute()).thenReturn(Observable.error(Exception("")))

        val values = charactersViewModel.loadData()
            .test()
            .values()
        assertTrue(values[0] is CharacterViewStatus.LOADING)
        assertTrue(values[1] is CharacterViewStatus.Error)
    }

    @Test
    fun `when handle favorite With character un favorite Then return character favorite false`() {
        val character = characterData.first()

        whenever(handleFavoriteCharacterUseCase.setCharacter(character))
            .thenReturn(handleFavoriteCharacterUseCase)
        whenever(handleFavoriteCharacterUseCase.execute())
            .thenReturn(Observable.just(character))

        val values = charactersViewModel.handleFavorite(character.transform(), 1)
            .test()
            .assertNoErrors()
            .assertComplete()
            .values()

        assertEquals(character, values.first().first)
        assertEquals(1, values.first().second)
    }

    @Test
    fun `when handle favorite With character un favorite Then return character favorite true`() {
        val character = characterData.first().copy(favorite = true)

        whenever(handleFavoriteCharacterUseCase.setCharacter(character))
            .thenReturn(handleFavoriteCharacterUseCase)
        whenever(handleFavoriteCharacterUseCase.execute())
            .thenReturn(Observable.just(character))

        val values = charactersViewModel.handleFavorite(character.transform(), 1)
            .test()
            .assertNoErrors()
            .assertComplete()
            .values()

        assertEquals(character, values.first().first)
        assertEquals(1, values.first().second)
    }

    @Test
    fun `when handle favorite With character un favorite Then return character maner status`() {
        val character = characterData.first().copy(favorite = true)

        whenever(handleFavoriteCharacterUseCase.setCharacter(character))
            .thenReturn(handleFavoriteCharacterUseCase)
        whenever(handleFavoriteCharacterUseCase.execute())
            .thenReturn(Observable.error(Exception()))

        val values = charactersViewModel.handleFavorite(character.transform(), 1)
            .test()
            .assertNoErrors()
            .assertComplete()
            .values()

        assertEquals(character, values.first().first)
        assertEquals(1, values.first().second)
    }

    @Test
    fun `transform Characters to CharacterViewData`() {
        val values = characterData.transform { it.transform() }
        assertEquals(values.count(), 2)
    }
}
