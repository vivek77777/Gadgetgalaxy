package com.example.gadgetgalaxy.ui.main.cart

import android.app.AlertDialog
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
import com.example.gadgetgalaxy.data.model.request.ClearCartRequest
import com.example.gadgetgalaxy.databinding.FragmentCartBinding
import com.example.gadgetgalaxy.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart), CartAdapter.CartListener {

    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartAdapter(this) }
    private val viewModelFirebase by viewModels<AuthViewModel>()
    private val viewModel by viewModels<CartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {

            rvCart.adapter = cartAdapter

            ivDelete.setOnClickListener {

                val alertDialog = AlertDialog.Builder(requireContext())

                alertDialog.setTitle("Deleting Cart")
                alertDialog.setMessage("Do you want to delete all items ?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    val clearCartRequest = ClearCartRequest(viewModelFirebase.currentUser!!.uid)
                    viewModel.clearCart(clearCartRequest)
                    viewModel.getCartProducts(viewModelFirebase.currentUser!!.uid)
                }
                alertDialog.setNegativeButton("No") { _, _ ->
                    alertDialog.setCancelable(true)
                }
                alertDialog.show()
            }

            buttonOrder.setOnClickListener {
                findNavController().navigate(CartFragmentDirections.cartToPayment())
            }

        }

        initAdapter()
        observeData()

    }


    private fun initAdapter() {
        with(viewModel) {
            getCartProducts(viewModelFirebase.currentUser!!.uid)
            totalAmount.observe(viewLifecycleOwner) { total ->
                binding.tvTotal.text = String.format("$%.2f", total)
            }
        }
    }


    private fun observeData() {
        viewModel.cartState.observe(viewLifecycleOwner) { state ->

            when (state) {

                CartState.Loading -> {
                    binding.progressBarCart.visible()
                }

                is CartState.CartList -> {
                    binding.rvCart.visible()
                    cartAdapter.submitList(state.products)
                    binding.progressBarCart.invisible()
                }

                is CartState.PostResponse -> {
                    binding.progressBarCart.invisible()
                    Toast.makeText(
                        requireContext(),
                        state.crud.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is CartState.Error -> {
                    binding.rvCart.invisible()
                    Toast.makeText(
                        requireContext(),
                        state.throwable.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarCart.invisible()
                }

            }

        }
    }

    override fun onCartClick(id: Int) {
        val action = CartFragmentDirections.cartToDetail(id)
        findNavController().navigate(action)
    }

    override fun onDeleteItemClick(id: Int) {
        viewModel.deleteFromCart(id)
        viewModel.getCartProducts(viewModelFirebase.currentUser!!.uid)
    }

    override fun onIncreaseItemClick(price: Double) {
        viewModel.increase(price)
    }

    override fun onDecreaseItemClick(price: Double) {
        viewModel.decrease(price)
    }


}