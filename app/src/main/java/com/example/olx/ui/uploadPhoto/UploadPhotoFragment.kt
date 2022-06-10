package com.example.olx.ui.uploadPhoto

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olx.MainActivity
import com.example.olx.R
import com.example.olx.ui.baseFragment
import com.example.olx.ui.PreviewImageActivity
import com.example.olx.ui.uploadPhoto.adapter.UploadImagesAdapter
import com.example.olx.utilities.Constants
import com.example.olx.utilities.OnActivityResultData
import com.example.olx.utilities.SharedPref
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_upload_photo.*
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class UploadPhotoFragment : baseFragment(), View.OnClickListener, UploadImagesAdapter.ItemClickListener {

    private val imageUriList: ArrayList<String> = ArrayList()
    private var imageUri: Uri? = null
    private var count: Int = 0
    private lateinit var uploadTask: UploadTask
    private var imagesAdapter: UploadImagesAdapter? = null
    private var selectedImagesArrayList: ArrayList<String> = ArrayList()
    private var outputFileUri: String? = null
    internal var dialog: BottomSheetDialog? = null
    internal var selectedImage: File? = null
    internal var file: File? = null
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

        registerCallBackPhoto()
    }

    private fun registerCallBackPhoto() {
        (activity as MainActivity).getOnActivityResult(object: OnActivityResultData{
            override fun resultData(bundle: Bundle) {
                linearLayoutChoosePhoto.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE

                val mpaths = bundle.getStringArrayList(Constants.IMAGE_PATHS)

                selectedImage = File(mpaths!![0])
                outputFileUri = mpaths[0]
                selectedImagesArrayList.add(mpaths[0])
                setAdapter()
            }
        })
    }

    private fun setAdapter() {
        if(imagesAdapter!=null){
            imagesAdapter!!.customNotify(selectedImagesArrayList)
        }
        else{
            imagesAdapter = UploadImagesAdapter(activity!!, selectedImagesArrayList, this)
            recyclerView.adapter = imagesAdapter
        }
    }

    private fun listener() {
        imageViewChoosePhoto.setOnClickListener(this)
        buttonPreview.setOnClickListener(this)
        buttonUpload.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.imageViewChoosePhoto->{
                if (selectedImagesArrayList.size > 4) {
                    Toast.makeText(activity, "You already selected 5 photos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    showBottomSheetDialog()
                }
            }

            R.id.buttonPreview->{
                if(selectedImage!=null){
                    startActivity(Intent(activity, PreviewImageActivity::class.java).putExtra("imageUri", outputFileUri))
                }
                else
                    Toast.makeText(activity, "Add Image First", Toast.LENGTH_SHORT).show()
            }

            R.id.buttonUpload->{
                if(selectedImage==null || selectedImage!!.exists())
//                    Toast.makeText(activity!!, "Please select a Photo", Toast.LENGTH_SHORT).show()
//                        saveFileInFirebaseStorage()
                        postAd()
                else
                    saveFileInFirebaseStorage()
            }
        }
    }

    private fun saveFileInFirebaseStorage() {
        for(i in 0..selectedImagesArrayList.size-1){
            val file = File(selectedImagesArrayList[i])
            uploadImage(file, file.name, i)
        }
    }

    private fun uploadImage(file: File, name: String, i: Int) {
        imgRef = storageRef.child("images/$name")

        uploadTask = imgRef.putFile(imageUri!!)

        uploadTask.addOnSuccessListener (object: OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                imgRef.downloadUrl.addOnSuccessListener {
                    count++
                    val url = it.toString()
                    imageUriList.add(url)
                    if(count==selectedImagesArrayList.size)
                        postAd()
                }
            }
        })
    }

    private fun postAd() {
        showProgressBar()
        val documentId = db.collection(arguments?.getString(Constants.KEY)!!).document().id

        val documentData = hashMapOf(
            Constants.ADDRESS to arguments?.getString(Constants.ADDRESS),
            Constants.BRAND to arguments?.getString(Constants.BRAND),
            Constants.AD_DESCRIPTION to arguments?.getString(Constants.AD_DESCRIPTION),
            Constants.AD_TITLE to arguments?.getString(Constants.AD_TITLE),
            Constants.PHONE to arguments?.getString(Constants.PHONE),
            Constants.PRICE to arguments?.getString(Constants.PRICE),
            Constants.TYPE to arguments?.getString(Constants.KEY),
            Constants.ID to documentId,
            Constants.USER_ID to SharedPref(activity!!).getString(Constants.USER_ID),
            Constants.CREATED_DATE to Date(),
            "images" to imageUriList
        )
        db.collection(arguments?.getString(Constants.KEY)!!)
            .add(documentData)
            .addOnSuccessListener {
                updateDocumentId(it.id)
            }
    }

    private fun updateDocumentId(id: String) {
        val docData = mapOf(
            Constants.ID to id
        )
        db.collection(arguments?.getString(Constants.KEY)!!)
            .document(id)
            .update(docData).addOnSuccessListener {
                hideProgressBar()
                Toast.makeText(activity!!, "Ad Posted Successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_photo_to_my_ads)
            }
    }

    private fun showBottomSheetDialog() {
        val layoutInflater = activity?.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.bottomsheet_dialog, null)
        dialog = BottomSheetDialog(activity!!)
        dialog?.setContentView(view)
        dialog?.window?.findViewById<View>(androidx.navigation.ui.R.id.design_bottom_sheet)
            ?.setBackgroundColor(resources.getColor(android.R.color.transparent))

        var textviewGallery = dialog!!.findViewById<TextView>(R.id.textViewPhoto)
        var textviewCamera = dialog!!.findViewById<TextView>(R.id.textViewCamera)
        var textviewCancel = dialog!!.findViewById<TextView>(R.id.textViewCancel)

        textviewCancel?.setOnClickListener{
            dialog!!.dismiss()
        }

        textviewCamera?.setOnClickListener{
            dialog!!.dismiss()
            chooseImage(ImagePicker.Mode.CAMERA)
        }

        textviewGallery?.setOnClickListener{
            dialog!!.dismiss()
            chooseImage(ImagePicker.Mode.GALLERY)
//            com.github.drjacky.imagepicker.ImagePicker.with(activity!!).galleryOnly()
        }

        dialog?.show()
        val lp = WindowManager.LayoutParams()
        val window = dialog?.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes=lp
    }

    private fun chooseImage(mode: ImagePicker.Mode){
            ImagePicker.Builder(activity)
            .mode(mode)
            .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
            .extension(ImagePicker.Extension.JPG)
            .allowMultipleImages(false)
            .enableDebuggingMode(true)
            .build()
    }

    override fun onItemClick(){
        showBottomSheetDialog()
    }
}