package com.example.gadgetgalaxy.data.source.remote


import com.example.gadgetgalaxy.common.Constants.Endpoint.ADD_TO_CART
import com.example.gadgetgalaxy.common.Constants.Endpoint.CLEAR_CART
import com.example.gadgetgalaxy.common.Constants.Endpoint.DELETE_FROM_CART
import com.example.gadgetgalaxy.common.Constants.Endpoint.GET_ALL_PRODUCTS
import com.example.gadgetgalaxy.common.Constants.Endpoint.GET_CART_PRODUCTS
import com.example.gadgetgalaxy.common.Constants.Endpoint.GET_PRODUCT_DETAIL
import com.example.gadgetgalaxy.common.Constants.Endpoint.GET_SALE_PRODUCTS
import com.example.gadgetgalaxy.common.Constants.Endpoint.SEARCH_PRODUCT
import com.example.gadgetgalaxy.data.model.request.AddToCartRequest
import com.example.gadgetgalaxy.data.model.request.ClearCartRequest
import com.example.gadgetgalaxy.data.model.request.DeleteFromCartRequest
import com.example.gadgetgalaxy.data.model.response.CRUDResponse
import com.example.gadgetgalaxy.data.model.response.GetCartProductsResponse
import com.example.gadgetgalaxy.data.model.response.GetProductDetailResponse
import com.example.gadgetgalaxy.data.model.response.GetProductResponse
import com.example.gadgetgalaxy.data.model.response.GetSaleProductResponse
import com.example.gadgetgalaxy.data.model.response.GetSearchProductResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface ProductService {

    @Headers("store:For_Project")
    @GET(GET_ALL_PRODUCTS)
    suspend fun getAllProducts(): GetProductResponse

    @Headers("store:For_Project")
    @GET(GET_PRODUCT_DETAIL)
    suspend fun getProductDetail(
        @Query("id") id: Int
    ): GetProductDetailResponse

    @Headers("store:For_Project")
    @GET(GET_SALE_PRODUCTS)
    suspend fun getSaleProducts(): GetSaleProductResponse

    @Headers("store:For_Project")
    @GET(SEARCH_PRODUCT)
    suspend fun getSearchProduct(
        @Query("query") query: String
    ): GetSearchProductResponse

    @Headers("store:For_Project")
    @POST(ADD_TO_CART)
    suspend fun addToCart(
        @Body addToCartRequest: AddToCartRequest
    ): CRUDResponse

    @Headers("store:For_Project")
    @GET(GET_CART_PRODUCTS)
    suspend fun getCartProducts(
        @Query("userId") userId: String
    ): GetCartProductsResponse

    @Headers("store:For_Project")
    @POST(DELETE_FROM_CART)
    suspend fun deleteFromCart(
        @Body deleteFromCartRequest: DeleteFromCartRequest
    ): CRUDResponse

    @Headers("store:For_Project")
    @POST(CLEAR_CART)
    suspend fun clearCart(
        @Body clearCartRequest: ClearCartRequest
    ) : CRUDResponse

}