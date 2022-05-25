package com.example.olx.ui.uploadPhoto

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.media.Image
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olx.R
import com.example.olx.baseFragment
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_upload_photo.*
import java.io.File

class UploadPhotoFragment : baseFragment(), View.OnClickListener {

    internal var dialog: BottomSheetDialog? = null
    internal var selectedImage: File? = null
    internal var TAG = UploadPhotoFragment::class.java.simpleName
    val db = FirebaseFirestore.getInstance()
    internal lateinit var storageRef: StorageReference
    internal lateinit var imgRef: StorageReference
    internal lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_upload_photo, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.layoutManager = GridLayoutManager(activity, 3)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()
        listener()
    }

    private fun listener() {
        imageViewChoosePhoto.setOnClickListener(this)
        buttonPreview.setOnClickListener(this)
        buttonUpload.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.imageViewChoosePhoto->{
                showBottomSheetDialog()
            }
        }
    }

    private fun showBottomSheetDialog() {
        val layoutInflater = activity?.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.bottomsheet_dialog, null)
        dialog = BottomSheetDialog(activity!!)
        dialog?.setContentView(view)
        dialog?.window?.findViewById<View>(androidx.navigation.ui.R.id.design_bottom_sheet)
            ?.setBackgroundColor(resources.getColor(android.R.color.white))

        var textviewGallery = dialog!!.findViewById<TextView>(R.id.textViewPhoto)
        var textviewCamera = dialog!!.findViewById<TextView>(R.id.textViewCamera)
        var textviewCancel = dialog!!.findViewById<TextView>(R.id.textViewCancel)

        textviewCancel?.setOnClickListener{
            dialog!!.dismiss()
        }

        dialog?.show()
        val lp = WindowManager.LayoutParams()
        val window = dialog?.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes=lp
    }

}