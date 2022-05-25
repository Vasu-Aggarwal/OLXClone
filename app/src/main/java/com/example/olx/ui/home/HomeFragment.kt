package com.example.olx.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olx.ui.baseFragment
import com.example.olx.databinding.FragmentHomeBinding
import com.example.olx.model.CategoriesModel
import com.example.olx.ui.home.adapter.CategoriesAdapter
import com.example.olx.ui.home.adapter.sellAdapter
import com.example.olx.utilities.Constants
import com.example.olx.utilities.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : baseFragment(), sellAdapter.ItemClickListener {

//    private lateinit var sellAdapter: sellAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    val db = FirebaseFirestore.getInstance()
    private lateinit var categoriesModel: MutableList<CategoriesModel>

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvCityName.text = SharedPref(requireActivity()).getString(Constants.CITY_NAME)

        getCategoryList()
        textListener()
    }

    private fun textListener() {
        edSearch.addTextChangedListener (object :TextWatcher{
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
        var temp: MutableList<CategoriesModel> = ArrayList()
        for (data in categoriesModel){
            if(data.key.contains(toString.capitalize()) || data.key.contains(toString.lowercase())){
                temp.add(data)
            }
        }
        categoriesAdapter.updateList(temp)
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
        rv_categories.layoutManager = GridLayoutManager(context, 3)
        categoriesAdapter = CategoriesAdapter(categoriesModel, this)
        rv_categories.adapter = categoriesAdapter
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(context, "Hey "+position, Toast.LENGTH_SHORT).show()
    }
}

