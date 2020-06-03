package br.com.fioalpha.heromarvel.list_caracters

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.fioalpha.heromarvel.ApplicationCustomTest
import br.com.fioalpha.heromarvel.MainActivity
import br.com.fioalpha.heromarvel.data.Repository
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner::class)
class ListCharacterTest {

    private val repository = mockk<Repository>(relaxed = false)

    private val app = ApplicationProvider.getApplicationContext<ApplicationCustomTest>()

    private val moduleDi = module { factory(override = true) { repository } }

    @Test
    fun showCharacters () {
        every { repository.fetchCharacter(1) } returns Observable.just(characterList)
            .delay(1000,TimeUnit.MILLISECONDS)

        every { repository.fetchCharacterFavorite() } returns Observable.just(emptyList())

        app.loadModules(moduleDi) {
            ActivityScenario.launch(MainActivity::class.java)
            listCharacters {
                loadingIsShowed()
                await(1000)
                recyclerIsShowed()
                titleItem(0, "Teste")
                titleItem(1, "Teste2")
                recyclerCountItem(6)
                emptyViewIsHide()
            }
        }
    }

    @Test
    fun showCharactersItemsWithEmptyView() {

        every { repository.fetchCharacter(1) } returns Observable.just(emptyList<CharacterDomain>())
            .delay(1000, TimeUnit.MILLISECONDS)
        every { repository.fetchCharacterFavorite() } returns Observable.just(emptyList())

        app.loadModules(moduleDi) {
            ActivityScenario.launch(MainActivity::class.java)

            listCharacters {
                loadingIsShowed()
                await(1000)
                emptyViewIsShowed()
            }
        }
    }

    @Test
    fun showCharactersItemWithErrorView() {
        every { repository.fetchCharacter(1) } returns Observable.error<List<CharacterDomain>>(Exception("Error"))
            .delay(1000, TimeUnit.MILLISECONDS)

        app.loadModules(moduleDi) {
            ActivityScenario.launch(MainActivity::class.java)
            listCharacters {
                errorViewIsShowed()
                messageError("Error")
            }

        }
    }

    private val characterList = listOf(
        CharacterDomain(1, "Teste", "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg", "Teste", false),
        CharacterDomain(2, "Teste", "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg", "Teste2", false),
        CharacterDomain(3, "Teste", "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg", "Teste", false),
        CharacterDomain(4, "Teste", "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg", "Teste", false),
        CharacterDomain(5, "Teste", "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg", "Teste", false),
        CharacterDomain(6, "Teste", "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg", "Teste", false)
    )
}
