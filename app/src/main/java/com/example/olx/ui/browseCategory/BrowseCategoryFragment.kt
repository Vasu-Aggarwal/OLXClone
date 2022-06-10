package com.example.olx.ui.browseCategory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olx.R
import com.example.olx.model.CategoriesModel
import com.example.olx.model.DataItemModel
import com.example.olx.ui.baseFragment
import com.example.olx.ui.browseCategory.adapter.BrowseCategoryAdapter
import com.example.olx.utilities.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.adapter_my_ads.*
import kotlinx.android.synthetic.main.fragment_browse.*
import kotlinx.android.synthetic.main.fragment_browse.edSearch
import kotlinx.android.synthetic.main.fragment_browse.rv_categories
import kotlinx.android.synthetic.main.fragment_home.*

class BrowseCategoryFragment : baseFragment(), BrowseCategoryAdapter.ItemClickListener {

    private var categoriesAdapter: BrowseCategoryAdapter? = null
    private lateinit var dataItemModel: MutableList<DataItemModel>
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_browse, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getlist()
        rv_categories.layoutManager = LinearLayoutManager(context)
        textListener()
    }

    private fun textListener() {
        edSearch.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }

        })
    }


    private fun filterList(toString: String) {
        var temp: MutableList<DataItemModel> = ArrayList()
        for (data in dataItemModel){
            if(data.brand.contains(toString.capitalize()) || data.brand.contains(toString.lowercase())){
                temp.add(data)
            }
        }
        categoriesAdapter?.updateList(temp)
    }

    private fun getlist() {
        showProgressBar()
        db.collection(arguments?.getString(Constants.KEY)!!)
            .get().addOnSuccessListener {
                hideProgressBar()
                dataItemModel = it.toObjects(DataItemModel::class.java)
                setAdapter()
            }
    }

    private fun setAdapter() {
        categoriesAdapter = BrowseCategoryAdapter(dataItemModel, this)
        rv_categories.adapter = categoriesAdapter
    }

    override fun onItemClick(position: Int) {
        var bundle = Bundle()
        bundle.putString(Constants.DOCUMENT_ID, dataItemModel.get(position).id)
        bundle.putString(Constants.KEY, dataItemModel.get(position).type)
        findNavController().navigate(R.id.action_browse_to_details, bundle)
    }
}