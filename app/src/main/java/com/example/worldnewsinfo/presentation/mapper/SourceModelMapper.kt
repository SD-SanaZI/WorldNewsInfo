package com.example.worldnewsinfo.presentation.mapper

import androidx.annotation.DrawableRes
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.domain.SourceDomainModel
import com.example.worldnewsinfo.presentation.source.SourceModel

class SourceModelMapper {
    fun mapSourceDomainModel(domainModel: SourceDomainModel): SourceModel {
        return SourceModel(
            getDrawableResFromName(domainModel.sourceName),
            domainModel.sourceName,
            domainModel.id,
            domainModel.supportingText
        )
    }

    @DrawableRes
    private fun getDrawableResFromName(name: String): Int {
        return when (name) {
            "Bloomberg" -> R.drawable.chanel_1
            "Daily Mail" -> R.drawable.chanel_2
            "FOX NEWS" -> R.drawable.chanel_3
            "The New York Times" -> R.drawable.chanel_4
            "CNN" -> R.drawable.chanel_5
            else -> R.drawable.chanel_5
        }
    }
}