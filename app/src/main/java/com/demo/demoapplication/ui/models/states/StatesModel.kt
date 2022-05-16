package com.demo.demoapplication.ui.models.states

import com.google.gson.annotations.SerializedName

data class StatesModel(

	@field:SerializedName("StatesModel")
	val statesModel: List<StatesModelItem?>? = null
)

data class StatesModelItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("abbreviation")
	val abbreviation: String? = null
)
