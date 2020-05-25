package br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterViewData (
    val id: Long,
    val imagePath: String,
    val title: String,
    val description: String,
    var favorite: Boolean = false
): Parcelable