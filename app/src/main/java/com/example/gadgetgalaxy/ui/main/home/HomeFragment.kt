package com.example.gadgetgalaxy.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gadgetgalaxy.R
import com.example.gadgetgalaxy.common.invisible
import com.example.gadgetgalaxy.common.visible
import com.example.gadgetgalaxy.data.model.ProductUI
import com.example.gadgetgalaxy.data.model.request.AddToCartRequest
import com.example.gadgetgalaxy.databinding.FragmentHomeBinding
import com.example.gadgetgalaxy.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), ProductsAdapter.ProductListener,
    DiscountProductsAdapter.DiscountProductListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val viewModelFirebase by viewModels<AuthViewModel>()
    private val productsAdapter by lazy { ProductsAdapter(this) }
    private val discountsAdapter by lazy { DiscountProductsAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.ivProfile.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToProfile())
        }

        binding.rvProducts.adapter = productsAdapter
        binding.rvDiscount.adapter = discountsAdapter

        viewModel.getAllProducts()
        viewModel.getFavoriteProducts()
        viewModel.getSaleProducts()
        observeData()
    }


    private fun observeData() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Loading -> {
                    binding.progressBarHome.visible()
                }

                is HomeState.ProductList -> {
                    productsAdapter.submitList(state.products)
                    binding.progressBarHome.invisible()
                }

                is HomeState.SaleProductList -> {
                    discountsAdapter.submitList(state.saleProducts)
                    binding.progressBarHome.invisible()
                }

                is HomeState.PostResponse -> {
                    binding.progressBarHome.invisible()
                    Toast.makeText(
                        requireContext(),
                        state.crud.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is HomeState.Error -> {
                    binding.progressBarHome.invisible()
                    Toast.makeText(
                        requireContext(),
                        state.throwable.message.orEmpty(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onProductClick(id: Int) {
        val direction = HomeFragmentDirections.homeToDetail(id)
        findNavController().navigate(direction)
    }

    override fun onCartButtonClick(id: Int) {
        val addToCartRequest = AddToCartRequest(viewModelFirebase.currentUser!!.uid, id)
        viewModel.addToCart(addToCartRequest)
    }

    override fun onFavButtonClick(product: ProductUI) {
        if (product.isFavorite) {
            viewModel.removeFromFavorite(product)
        } else {
            viewModel.addToFavorite(product)
        }

        viewModel.getAllProducts()
        viewModel.getSaleProducts()

    }

}