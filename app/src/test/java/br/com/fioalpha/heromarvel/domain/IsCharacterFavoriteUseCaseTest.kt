package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.data.Repository
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class IsCharacterFavoriteUseCaseTest {

    private val CHARACTER_JSON = "characters_list.json"

    private val characters =
        Gson().fromJson<List<CharacterDomain>>(MockCreator().getMockText(CHARACTER_JSON))

    private val repository: Repository = mock()

    private lateinit var isCharactersUseCase: IsCharacterFavoriteUseCase

    @Before
    fun setup() {
        isCharactersUseCase = IsCharacterFavoriteUseCaseImpl(repository)
    }

    @Test
    fun `when called execute With on character matcher list favorite`() {
        whenever(repository.fetchCharacterFavorite()).thenReturn(
            Observable.just(listOf(CharacterDomain(1, "", "", "")))
        )

        isCharactersUseCase.setCharacters(characters)
            .execute()
            .test()
            .assertComplete()
            .assertValue {
                it.first().favorite
            }
    }

    @Test
    fun `when called execute With favorite  list empty`() {
        whenever(repository.fetchCharacterFavorite()).thenReturn(
            Observable.just(emptyList())
        )

        isCharactersUseCase.setCharacters(characters)
            .execute()
            .test()
            .assertComplete()
            .assertValue {
                !it[0].favorite && !it[1].favorite
            }
    }

    @Test
    fun `when called execute With characters  list empty`() {
        whenever(repository.fetchCharacterFavorite()).thenReturn(
            Observable.just(emptyList())
        )

        isCharactersUseCase
            .execute()
            .test()
            .assertComplete()
            .assertValue { it.isEmpty() }
    }

    @Test
    fun `when called execute With all item not matcher`() {
        whenever(repository.fetchCharacterFavorite()).thenReturn(
            Observable.just(
                listOf(
                    CharacterDomain(3, "Three", "", "Three"),
                    CharacterDomain(4, "Three", "", "Four")
                )
            )
        )
        isCharactersUseCase.setCharacters(characters)
            .execute()
            .test()
            .assertComplete()
            .assertValue { characters == it }
    }

    @Test
    fun `when called execute With all item is matcher`() {
        whenever(repository.fetchCharacterFavorite()).thenReturn(
            Observable.just(
                listOf(
                    CharacterDomain(1, "Three", "", "Three"),
                    CharacterDomain(2, "Three", "", "Four")
                )
            )
        )
        isCharactersUseCase.setCharacters(characters)
            .execute()
            .test()
            .assertComplete()
            .assertValue { it[0].favorite && it[1].favorite }
    }
}
