package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import io.reactivex.Observable

interface FetchAllFavoriteUseCase:
    UserCase<Observable<List<CharacterDomain>>>