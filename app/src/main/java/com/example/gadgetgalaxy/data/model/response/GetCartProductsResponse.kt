package com.example.gadgetgalaxy.data.model.response


import com.example.gadgetgalaxy.data.model.Product

data class GetCartProductsResponse(
    val message: String?,
    val products: List<Product>?,
    val status: Int?
)