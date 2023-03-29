package com.example.firebasestudyproject.ui.dashboard.ui.dashboard

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.adapters.DashBoardAdapter
import com.example.firebasestudyproject.adapters.ProductListAdapter
import com.example.firebasestudyproject.base.BaseFragment
import com.example.firebasestudyproject.databinding.FragmentDashboardBinding
import com.example.firebasestudyproject.firestore.FireStoreClass
import com.example.firebasestudyproject.model.Product
import com.example.firebasestudyproject.ui.productdetails.ProductDetailsActivity
import com.example.firebasestudyproject.ui.settings.SettingsActivity
import com.example.firebasestudyproject.utils.Constants

class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.dashboard_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                val id = menuItem.itemId
                when (id) {
                    R.id.action_settings -> {
                        startActivity(Intent(activity, SettingsActivity::class.java))
                        return true
                    }
                }
                return onMenuItemSelected(menuItem)
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getDashBoardItemList()
    }

    private fun getDashBoardItemList() {
        showProgressDialog()
        FireStoreClass().getDashBoardItemList(this@DashboardFragment)
    }

    fun successDashBoardItemsList(dashBoardItemList: ArrayList<Product>) {
        hideProgressDialog()
        for (i in dashBoardItemList) {
            if (dashBoardItemList.size > 0) {
                binding.rvDashBoardItems.visibility = View.VISIBLE
                binding.txtNoDashBoardITemFound.visibility = View.GONE

                binding.rvDashBoardItems.layoutManager = GridLayoutManager(activity, 2)
                binding.rvDashBoardItems.setHasFixedSize(true)

                val adapterDashBoard = DashBoardAdapter(requireContext(), dashBoardItemList)
                binding.rvDashBoardItems.adapter = adapterDashBoard
                adapterDashBoard.setOnClickListener(object : DashBoardAdapter.OnClickListener {
                    override fun onClick(position: Int, product: Product) {
                        val intent = Intent(activity, ProductDetailsActivity::class.java)
                        intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
                        intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, product.userId)
                        startActivity(intent)
                    }

                })

            } else {
                binding.rvDashBoardItems.visibility = View.GONE
                binding.txtNoDashBoardITemFound.visibility = View.VISIBLE

            }

        }
    }


}