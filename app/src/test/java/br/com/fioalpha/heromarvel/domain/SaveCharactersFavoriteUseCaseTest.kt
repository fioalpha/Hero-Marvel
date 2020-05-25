package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.data.Repository
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test

class SaveCharactersFavoriteUseCaseTest {

    private val repository: Repository = mock()

    private lateinit var userCase: HandleFavoriteCharacterUseCaseImpl

    private val CHARACTER_FAVORITE =
        CharacterDomain(
            id = 1,
            description = "TEST_1",
            imagePath = "PATH",
            name = "TEST",
            favorite = true
        )

    private val CHARACTER_UNFAVORITE =
        CharacterDomain(
            id = 1,
            description = "TEST_1",
            imagePath = "PATH",
            name = "TEST",
            favorite = false
        )

    @Before
    fun setup() {
        userCase = HandleFavoriteCharacterUseCaseImpl(repository)
    }

    @Test
    fun `when execute Then save favorite character` () {
//        whenever(repository.saveFavorite(CHARACTER_FAVORITE)).thenReturn(Single.just(CHARACTER_FAVORITE))
//        userCase.setCharacter(CHARACTER_FAVORITE)
//            .execute()
//            .test()
//            .assertComplete()
//            .assertNoErrors()
    }

    @Test
    fun `when execute Then remove favorite character` () {
//        whenever(repository.removeFavorite(CHARACTER_UNFAVORITE)).thenReturn(Single.just(CHARACTER_UNFAVORITE))
//        userCase.setCharacter(CHARACTER_UNFAVORITE)
//            .execute()
//            .test()
//            .assertComplete()
//            .assertNoErrors()
    }

    @Test
    fun `when execute Not character isInitialized` () {
        userCase.execute()
            .test()
            .assertNotComplete()
            .assertError {
                it is ExceptionInInitializerError
            }
    }

}