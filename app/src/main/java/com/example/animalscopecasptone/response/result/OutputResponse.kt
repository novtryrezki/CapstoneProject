package com.example.animalscopecasptone.response.result

import com.google.gson.annotations.SerializedName

data class OutputResponse(

	@field:SerializedName("OutputResponse")
	val outputResponse: List<OutputResponseItem>
)

data class OutputResponseItem(

	@field:SerializedName("fakta")
	val fakta: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("karakteristik")
	val karakteristik: String,

	@field:SerializedName("namaHewan")
	val namaHewan: String
)
