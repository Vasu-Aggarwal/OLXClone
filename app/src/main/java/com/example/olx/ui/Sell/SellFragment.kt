package com.example.olx.ui.Sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olx.R
import com.example.olx.baseFragment
import com.example.olx.databinding.FragmentSellBinding
import com.example.olx.model.CategoriesModel
import com.example.olx.ui.home.adapter.sellAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : baseFragment(), sellAdapter.ItemClickListener {

    private lateinit var categoriesModel: MutableList<CategoriesModel>
    private var _binding: FragmentSellBinding? = null

    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSellBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getCategoryList()
    }

    private fun getCategoryList() {
        showProgressBar()
        db.collection("Categories").get().addOnSuccessListener {
            hideProgressBar()
            categoriesModel = it.toObjects(CategoriesModel::class.java)
            setAdapter()
        }
    }

    private fun setAdapter() {
        rv_offerings.layoutManager = GridLayoutManager(context, 3)
        val sellAdapter = sellAdapter(categoriesModel, this)
        rv_offerings.adapter = sellAdapter
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putString("key", categoriesModel.get(position).key)
        findNavController().navigate(R.id.action_sell_to_include_details, bundle)
    }
}