package br.com.fioalpha.heromarvel.data

import androidx.room.EmptyResultSetException
import br.com.fioalpha.heromarvel.core.network.CharacterSimpleResponse
import br.com.fioalpha.heromarvel.core.network.Service
import br.com.fioalpha.heromarvel.core.room_local.AppDatabase
import br.com.fioalpha.heromarvel.core.room_local.CharacterFavoriteRoom
import br.com.fioalpha.heromarvel.core.utils.GeneratorHash
import br.com.fioalpha.heromarvel.core.utils.transform
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class RepositoryImpl(
     private val remoteDataSource: Service,
     private val localDataSource: AppDatabase,
     private val generator: GeneratorHash
): Repository {

    private val hash: String = generator.createHash()
    private val time: Long = generator.getTimeStamp()
    private val publicKey = generator.getPublicKey()

    override fun fetchCharacter(page: Int): Observable<List<CharacterDomain>> {
       return remoteDataSource.fetchAllCharacters(
           time,
           publicKey,
           hash,
           page
       ).map { data -> data.data.results.transform { it.transform() } }
    }

    private fun CharacterSimpleResponse.transform(): CharacterDomain {
        return CharacterDomain(
            id,
            description,
            "${thumbnail.path}.${thumbnail.extension}",
            name,
            false
        )
    }

    override fun fetchCharacterFavorite(): Observable<List<CharacterDomain>> {
        return localDataSource.characterFavoriteDao()
            .getAll()
            .toObservable()
            .map { it.transform {  character  ->
                CharacterDomain(
                    id = character.id?: 0L,
                    description = character.description.orEmpty(),
                    name = character.name.orEmpty(),
                    imagePath = character.pathImage.orEmpty(),
                    favorite = true
                )
            } }
    }

    override fun removeFavorite(character: CharacterDomain): Completable {
        return Completable.defer {
            localDataSource.characterFavoriteDao()
                .delete(
                    CharacterFavoriteRoom(
                        character.id,
                        character.name,
                        character.imagePath,
                        character.description
                    )
                )
                .blockingAwait()
            return@defer Completable.complete()
        }
    }

    override fun saveFavorite(character: CharacterDomain): Completable {
        return Completable.defer {
            localDataSource.characterFavoriteDao()
                .save(
                    CharacterFavoriteRoom(
                        character.id,
                        character.name,
                        character.imagePath
                    )
                )
                .blockingAwait()
            return@defer Completable.complete()
        }
    }

    override fun characterIsFavorite(character: CharacterDomain): Single<Boolean> {
        return try {
            localDataSource.characterFavoriteDao()
                .getById(character.id)
                .map { true }

        } catch (e: EmptyResultSetException) {
            Single.just(false)
        }
    }

    override fun characterByTerm(term: String): Observable<List<CharacterDomain>> {
        return remoteDataSource.fetchCharacters(
            term,
            time,
            publicKey,
            hash
        ).map { data -> data.data.results.transform { it.transform() } }
    }
}
