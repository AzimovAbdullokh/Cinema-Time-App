package com.example.data.network.cloud.cloudModels.person

import com.google.gson.annotations.SerializedName

data class MovieCrewCloud(
    @SerializedName("cast") val cast: List<PersonCloud>,
    @SerializedName("crew") val crew: List<CrewCloud>,
    @SerializedName("id") val id: Int,
)