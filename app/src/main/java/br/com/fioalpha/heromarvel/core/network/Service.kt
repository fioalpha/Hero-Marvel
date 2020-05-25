package br.com.fioalpha.heromarvel.core.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("characters")
    fun fetchAllCharacters(
        @Query("ts") timeStamp: Long,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("offset") page: Int
    ): Observable<DataResponse>

    @GET("characters")
    fun fetchCharacters(
        @Query("nameStartsWith") term: String,
        @Query("ts") timeStamp: Long,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String
    ): Observable<DataResponse>
}

data class DataResponse(
    val data: ResultResponse
)

data class ResultResponse(
    val results: List<CharacterSimpleResponse>
)

data class CharacterSimpleResponse(
    val id: Long,
    val name: String,
    val description: String,
    val thumbnail: ThumbnailResponse
)

data class ThumbnailResponse(
    val path: String,
    val extension: String
)
