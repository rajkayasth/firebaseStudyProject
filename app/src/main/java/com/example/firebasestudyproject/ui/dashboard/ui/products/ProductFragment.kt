package com.example.firebasestudyproject.ui.dashboard.ui.products

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.adapters.ProductListAdapter
import com.example.firebasestudyproject.base.BaseFragment
import com.example.firebasestudyproject.databinding.FragmentProductsBinding
import com.example.firebasestudyproject.firestore.FireStoreClass
import com.example.firebasestudyproject.model.Product
import com.example.firebasestudyproject.ui.dashboard.ui.products.addproducts.AddProductActivity
import com.example.firebasestudyproject.ui.settings.SettingsActivity

class ProductFragment : BaseFragment() {

    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(ProductViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val menuHost: MenuHost = requireActivity()
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.add_prodcuts_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                val id = menuItem.itemId
                when (id) {
                    R.id.action_product_add -> {
                        // findNavController().navigate(ProductFragmentDirections.actionNavigationProductToAddProductFragment())
                        startActivity(Intent(activity, AddProductActivity::class.java))
                        return true
                    }
                }
                return onMenuItemSelected(menuItem)
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return root
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    fun successProductListFromFireStore(productList: ArrayList<Product>) {
        hideProgressDialog()
        for (i in productList) {
            if (productList.size > 0) {
                binding.rvProductItems.visibility = View.VISIBLE
                binding.txtNoProductAvailable.visibility = View.GONE

                binding.rvProductItems.layoutManager = LinearLayoutManager(activity)
                binding.rvProductItems.setHasFixedSize(true)

                val adapterProduct = ProductListAdapter(requireContext(), productList)
                binding.rvProductItems.adapter = adapterProduct
            } else {
                binding.rvProductItems.visibility = View.GONE
                binding.txtNoProductAvailable.visibility = View.VISIBLE

            }

        }
    }

    fun getProductListFromFireStore() {
        showProgressDialog()
        FireStoreClass().getProductList(this@ProductFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}