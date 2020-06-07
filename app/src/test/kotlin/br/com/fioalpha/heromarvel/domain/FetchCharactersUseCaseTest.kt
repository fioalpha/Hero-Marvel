package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.data.Repository
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class FetchCharactersUseCaseTest {

    private val mockCreator = MockCreator()

    private val repository: Repository = mock()

    private val gson = Gson()

    private val CHARACTER_JSON = "characters_list.json"

    private lateinit var usecase: FetchCharactersUseCase

    @Before
    fun setup() {
        usecase = FetchCharactersUseCaseImp(repository)
    }


    @Test
    fun `call execute With Success receiver data` () {
        whenever(repository.fetchCharacter(1)).thenReturn(
            Observable.just(
                    gson.fromJson<List<CharacterDomain>>(mockCreator.getMockText(CHARACTER_JSON)
                )
            )
        )

        usecase.execute()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(
                gson.fromJson<List<CharacterDomain>>(mockCreator.getMockText(CHARACTER_JSON))
            )
    }

    @Test
    fun `call execute  With paging Success receiver data` () {
        usecase.page(2)
        whenever(repository.fetchCharacter(2)).thenReturn(
            Observable.just(
                gson.fromJson<List<CharacterDomain>>(mockCreator.getMockText(CHARACTER_JSON)
                )
            )
        )

        usecase.execute()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(gson.fromJson<List<CharacterDomain>>(mockCreator.getMockText(CHARACTER_JSON)))
    }

    @Test
    fun `call execute  With Failed receiver data` () {
        whenever(repository.fetchCharacter(1)).thenReturn(
            Observable.error(Exception("ERROR"))
        )

        usecase.execute()
            .test()
            .assertNotComplete()
            .assertError { it is Exception }
    }
}

class MockCreator {
     fun getMockText(file: String): String {
        return javaClass.classLoader!!.getResource("mocks/models/$file").readText()
    }
}

inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)