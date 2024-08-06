package com.example.gadgetgalaxy.data.repository

import com.example.gadgetgalaxy.common.Resource
import com.example.gadgetgalaxy.data.mapper.mapToProductEntity
import com.example.gadgetgalaxy.data.mapper.mapToProductUI
import com.example.gadgetgalaxy.data.model.ProductUI
import com.example.gadgetgalaxy.data.model.request.AddToCartRequest
import com.example.gadgetgalaxy.data.model.request.ClearCartRequest
import com.example.gadgetgalaxy.data.model.request.DeleteFromCartRequest
import com.example.gadgetgalaxy.data.model.response.CRUDResponse
import com.example.gadgetgalaxy.data.source.local.ProductDao
import com.example.gadgetgalaxy.data.source.remote.ProductService
import com.google.firebase.auth.FirebaseAuth


class ProductRepository(
    private val productService: ProductService,
    private val productDao: ProductDao
) {

    suspend fun getProductDetail(id: Int): Resource<ProductUI> {
        return try {

            val getFavoriteIds = getFavoriteIds()
            val result = productService.getProductDetail(id).product
            result.let {
                Resource.Success(it.mapToProductUI(isFavorite = getFavoriteIds.contains(it.id)))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getAllProducts(): Resource<List<ProductUI>> {
        return try {
            val getFavoriteIds = getFavoriteIds()
            val result = productService.getAllProducts().products.orEmpty()

            if (result.isEmpty()) {
                Resource.Error(Exception("Products not found"))
            } else {
                Resource.Success(result.map {
                    it.mapToProductUI(isFavorite = getFavoriteIds.contains(it.id))
                })
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getSaleProducts(): Resource<List<ProductUI>> {
        return try {
            val getFavoriteIds = getFavoriteIds()
            val result = productService.getSaleProducts().products.orEmpty()

            if (result.isEmpty()) {
                Resource.Error(Exception("Products not found"))
            } else {
                Resource.Success(result.map {
                    it.mapToProductUI(isFavorite = getFavoriteIds.contains(it.id))
                })
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getSearchProducts(query: String): Resource<List<ProductUI>> {
        return try {
            val getFavoriteIds = getFavoriteIds()
            val result = productService.getSearchProduct(query).products
            result?.let {
                Resource.Success(result.map {
                    it.mapToProductUI(isFavorite = getFavoriteIds.contains(it.id))
                })
            } ?: kotlin.run {
                Resource.Error(Exception("Products not found !"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun addToCart(addToCartRequest: AddToCartRequest): Resource<CRUDResponse> {
        return try {
            val result = productService.addToCart(addToCartRequest)
            if (result.status == 200) {
                Resource.Success(result)
            } else {
                Resource.Error(Exception("Product not added"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getCartProducts(userId: String): Resource<List<ProductUI>> {
        return try {
            val getFavoriteIds = getFavoriteIds()
            val result = productService.getCartProducts(userId)

            if (result.status == 200) {
                Resource.Success(result.products.orEmpty().map {
                    it.mapToProductUI(isFavorite = getFavoriteIds.contains(it.id))
                })
            } else {
                Resource.Error(Exception("Cart Empty"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun deleteFromCart(deleteFromCartRequest: DeleteFromCartRequest): Resource<CRUDResponse> {
        return try {
            val result = productService.deleteFromCart(deleteFromCartRequest)
            if (result.status == 200) {
                Resource.Success(result)
            } else {
                Resource.Error(Exception("Product not deleted"))
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun clearCart(clearCartRequest: ClearCartRequest): Resource<CRUDResponse> {

        return try {
            val result = productService.clearCart(clearCartRequest)
            if (result.status == 200) {
                Resource.Success(result)
            } else {
                Resource.Error(Exception("Cart not Cleared !"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
        getCartProducts(FirebaseAuth.getInstance().currentUser!!.uid)
    }


    suspend fun addToFavorites(product: ProductUI) {
        productDao.addToFavorites(product.mapToProductEntity())
    }

    suspend fun removeFromFavorites(product: ProductUI) {
        productDao.removeFromFavorites(product.mapToProductEntity())
    }

    suspend fun getFavoriteProducts(): Resource<List<ProductUI>> {
        return try {
            val result = productDao.getFavoriteProducts().map { it.mapToProductUI() }
            if (result.isEmpty()) {
                Resource.Error(Exception("There are no products here!"))
            } else {
                Resource.Success(result)
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getFavoriteIds() = productDao.getFavoriteIds()



}
