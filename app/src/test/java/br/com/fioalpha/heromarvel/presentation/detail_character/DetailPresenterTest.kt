package br.com.fioalpha.heromarvel.presentation.detail_character

import br.com.fioalpha.heromarvel.core.utils.SchedulerRx
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class DetailPresenterTest {

    private val detailView: DetailsView = mock()

    private val fetchDetailUseCase: FetchDetailUseCase = mock()

    private val schedulerRx: SchedulerRx = mock()

    private lateinit var detailPresenter: DetailPresenter

    @Before
    fun setup() {
        detailPresenter = DetailPresenter(
            detailView,
            fetchDetailUseCase,
            schedulerRx
        )

        whenever(schedulerRx.ioSchedule()).thenReturn(Schedulers.trampoline())
        whenever(schedulerRx.mainSchedule()).thenReturn(Schedulers.trampoline())
    }

    @Test
    fun `when called loadCharacter With details character` () {
        whenever(fetchDetailUseCase.setIdCharacter(1)).thenReturn(fetchDetailUseCase)
//        whenever(fetchDetailUseCase.execute()).thenReturn(Single.just(CharacterDetailDomain(1, "dfd", ""))
    }

}