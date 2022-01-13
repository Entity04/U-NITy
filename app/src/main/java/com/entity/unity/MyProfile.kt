package com.entity.unity


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.entity.unity.constants.Constants
import com.entity.unity.databinding.ActivityMyProfileBinding
import com.entity.unity.databinding.DialogCustomImageSelectionBinding
import com.entity.unity.model.UserProfile
import com.entity.unity.videocall.FirebaseServ
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.File
import java.util.*


class MyProfile : AppCompatActivity() {

    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2
    }

    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var filePath: Uri? = null
    private lateinit var mBinding: ActivityMyProfileBinding
    val postid: String = UUID.randomUUID().toString()
    private lateinit var etName: EditText
    private lateinit var tvEmail: TextView
    private lateinit var tvRole: TextView
    private lateinit var btnUpdate: Button
    private lateinit var imgProfile: CircleImageView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        etName= findViewById(R.id.et_name)
        tvEmail= findViewById(R.id.tv_email)
        tvRole= findViewById(R.id.tv_role)
        btnUpdate = findViewById(R.id.btn_update)
        imgProfile = findViewById(R.id.iv_profile_user_image)

        if(Constants.hashmap[FirebaseAuth.getInstance().uid.toString()]==true){
            tvRole.text="Counsellor"
        }
        else{
            tvRole.text="Student"
        }

        tvEmail.text=FirebaseAuth.getInstance().currentUser?.email

        imgProfile.setOnClickListener {
            customImageSelectionDialog()
        }

        val db= FirebaseFirestore.getInstance()
        btnUpdate.setOnClickListener {
            val mp = HashMap<String, String>()
            mp["name"] = etName.text.toString()
            mp["email"] = tvEmail.text.toString()
            mp["uid"] = FirebaseAuth.getInstance().uid.toString()
            if(Constants.hashmap[FirebaseAuth.getInstance().uid.toString()]==true){
                mp["role"]="Counsellor"
            }
            else{
                mp["role"]="Student"
            }
            db.collection("Profile").document(FirebaseAuth.getInstance().uid.toString()).set(mp)
                .addOnSuccessListener {
                    uploadImage()
                    FirebaseServ().sendNotification("Hello!! Profile Updated Successfully")
                    startActivity(Intent(this,MainActivity2::class.java))
                    finish()

                }.addOnFailureListener {
                    Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show()
                }
        }

        loadData()

    }


    fun loadData(){


        val db= FirebaseFirestore.getInstance()

        val storage = FirebaseStorage.getInstance()
        db.collection("Profile").get().addOnSuccessListener{
            val list: MutableList<DocumentSnapshot> = it.documents
            list.let {
                for(d in list) {
                    val id:String=d.id
                    if(d.get("uid").toString()==FirebaseAuth.getInstance().currentUser?.uid) {
                        val gsReference = storage.getReference("Profile/$id")
                        val profile = UserProfile(
                            d.get("name").toString(),
                            d.get("email").toString(),
                            d.get("uid").toString(),
                            gsReference,
                            d.get("role").toString()
                        )

                        updateProfile(profile)
                    }
                }

            }

        }
    }

    private fun updateProfile(profile: UserProfile) {

        tvEmail.text=profile.email
        etName.setText(profile.name)
        tvRole.text=profile.role
        val localFile = File.createTempFile("images", ".jpeg")
        profile.gref!!.getFile(localFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                imgProfile.setImageBitmap(bitmap)

            }.addOnFailureListener {
                // Handle any errors

                Log.d("Error", it.toString())
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
        //    filePath = data.data
        //}
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {

                data?.extras?.let {
                    val thumbnail: Bitmap =
                        data.extras!!.get("data") as Bitmap // Bitmap from camera

                    // Set Capture Image bitmap to the imageView using Glide
                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .into(mBinding.ivProfileUserImage)


                    // Replace the add icon with edit icon once the image is loaded.
                    mBinding.ivProfileUserImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_vector_edit
                        )
                    )
                    filePath = data.data
                }
            } else if (requestCode == GALLERY) {

                data?.let {
                    // Here we will get the select image URI.
                    val selectedPhotoUri = data.data
                    // Set Selected Image URI to the imageView using Glide
                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                @Nullable e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                // log exception
                                Log.e("TAG", "Error loading image", e)
                                return false // important to return false so the error placeholder can be placed
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {

                                val bitmap: Bitmap = resource.toBitmap()


                                return false
                            }
                        })
                        .into(mBinding.ivProfileUserImage)

                    // Replace the add icon with edit icon once the image is selected.
                    mBinding.ivProfileUserImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_vector_edit
                        )
                    )
                    filePath = data.data
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference!!.child("Profile/$postid")
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)

        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {

            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        report?.let {
                            // Here after all the permission are granted launch the CAMERA to capture an image.
                            if (report.areAllPermissionsGranted()) {

                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                startActivityForResult(intent, CAMERA)
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }
                }).onSameThread()
                .check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this)
                .withPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : PermissionListener {

                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                        // Here after all the permission are granted launch the gallery to select and image.
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )

                        startActivityForResult(galleryIntent, GALLERY)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        //Toast
                    }


                    override fun onPermissionRationaleShouldBeShown(
                        p0: com.karumi.dexter.listener.PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }
                }).onSameThread()
                .check()

            dialog.dismiss()
        }

        //Start the dialog and display it on screen.
        dialog.show()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}