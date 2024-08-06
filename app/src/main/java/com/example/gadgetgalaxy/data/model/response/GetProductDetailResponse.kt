package com.example.gadgetgalaxy.data.model.response


import com.example.gadgetgalaxy.data.model.Product

data class GetProductDetailResponse(
    val message: String?,
    val product: Product,
    val status: Int?
)