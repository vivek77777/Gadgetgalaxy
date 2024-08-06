package com.example.gadgetgalaxy.data.model.response


import com.example.gadgetgalaxy.data.model.Product

data class GetSearchProductResponse(
    val message: String?,
    val products: List<Product>?,
    val status: Int?
)