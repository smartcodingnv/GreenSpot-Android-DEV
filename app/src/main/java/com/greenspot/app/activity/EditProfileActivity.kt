package com.greenspot.app.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.greenspot.app.R
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.UserDetailsResponce
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.content_edit_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class EditProfileActivity : AppCompatActivity() {
    private lateinit var token: String
    private val TAG = "EditProfileActivity"
    val myCalendar = Calendar.getInstance()
    private var userChoosenTask = ""
    private val REQUEST_CAMERA = 0
    private val SELECT_FILE = 1
    private var selectedImage: Uri? = null
    private var picturePath = ""


    private lateinit var helper: PreferenceHelper
    private lateinit var helperlang: PreferenceHelper
    private lateinit var utils: Utils
    private lateinit var progress: Progress
    private lateinit var viewDilaog: ViewDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        utils = Utils(this)
        progress = Progress(this)
        viewDilaog = ViewDialog(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        token = "Bearer " + helper.LoadStringPref(AppConfig.PREFERENCE.AUTHTOKEN, "")

        initdetails()

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


        img_profile.setOnClickListener(View.OnClickListener {

            selectImage()
        })


        btn_updateprofile.setOnClickListener(View.OnClickListener {

            updateProfile()
        })

        et_dob.setOnClickListener(View.OnClickListener {
            DatePickerDialog(
                this, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        })

    }

    private fun initdetails() {


        getuserDetails(authToken = token, langCode = helperlang.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")!!)

    }

    private fun updateLabel() {
        val myFormat = "dd-MM-yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        et_dob.setText(sdf.format(myCalendar.time))
    }

    var date =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }


    private fun updateProfile() {

        val name = et_name.getText().toString()
        val lastname = et_lname.getText().toString()
        val email = et_email.getText().toString()
        val number = et_number.getText().toString()
//        val dob = et_dob.getText().toString()
//        val contry = et_contry.getText().toString()
//        val address = et_address.getText().toString()


        if (name.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.alert_firstname),
                Toast.LENGTH_SHORT
            ).show()
            return

        }

        if (lastname.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.alert_lastname),
                Toast.LENGTH_SHORT
            ).show()
            return

        }
        if (email.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.alert_email),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (number.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.enter_the_number),
                Toast.LENGTH_SHORT
            ).show()

            return
        }


//        if (dob.isEmpty()) {
//
//            Toast.makeText(
//                applicationContext,
//                "Select the date of birth",
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return
//        }
//        if (contry.isEmpty()) {
//
//            Toast.makeText(
//                applicationContext,
//                "Select the contry",
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return
//        }
//
//        if (address.isEmpty()) {
//
//            Toast.makeText(
//                applicationContext,
//                "Enter the address",
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return
//        }
        if (!isValidEmail(email)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.res_validemail),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

//        if (isValidMobile(number)) {
//            Toast.makeText(
//                applicationContext,
//                getString(R.string.res_validnumber),
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return
//
//        }

        updateDetails(
            authToken = token, fname = name, lname = lastname, email = email, number = number,
            langCode = helperlang.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")!!
        )


    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }



    private fun selectImage() {
        val items = arrayOf<CharSequence>(getString(R.string.str_takephoto), getString(R.string.str_chooselib), getString(R.string.res_cancel))

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.res_addphoto))
        builder.setItems(items) { dialog, item ->
            val result = Utility.checkPermission(this)

            if (items[item] == getString(R.string.str_takephoto)) {
                userChoosenTask = getString(R.string.str_takephoto)
                if (result)
                    cameraIntent()
            } else if (items[item] == getString(R.string.str_chooselib)) {
                userChoosenTask = getString(R.string.str_chooselib)
                if (result)
                    galleryIntent()

            } else if (items[item] == getString(R.string.res_cancel)) {
                dialog.dismiss()
            }
        }
        builder.show()
    }

        private fun cameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun galleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                selectedImage = data!!.getData()
                img_profile.setImageURI(selectedImage)
                Log.e(TAG, "onActivityResult: $selectedImage")
                //                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {

                onCaptureImageResult(data!!)

                //                Log.e(TAG, "onActivityResult: "+data.getData());
                //                selectedImage = data.getData();
                //                imgProfile.setImageURI(selectedImage);
                //                Log.e(TAG, "onActivityResult: " + selectedImage);
                //                picturePath = getPath(getActivity().getApplicationContext(), selectedImage);

                //                onCaptureImageResult(data);
            }

        }
    }


    private fun onCaptureImageResult(data: Intent) {
        val thumbnail = data.extras!!.get("data") as Bitmap?
        //        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        //
        //        File destination = new File(Environment.getExternalStorageDirectory(),
        //                System.currentTimeMillis() + ".jpg");
        //
        //        FileOutputStream fo;
        //        try {
        //            destination.createNewFile();
        //            fo = new FileOutputStream(destination);
        //            fo.write(bytes.toByteArray());
        //            fo.close();
        //        } catch (FileNotFoundException e) {
        //            e.printStackTrace();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //
        //        Log.e(TAG, "onCaptureImageResult: " + thumbnail);
        //        BitmapDrawable ob = new BitmapDrawable(getResources(), thumbnail);
        //        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        //        byte[] byteArray = byteArrayOutputStream.toByteArray();

        selectedImage = getImageUri(this, thumbnail!!)
        Log.e(TAG, "uri  " + selectedImage)
        img_profile.setImageURI(selectedImage)
        picturePath = getPath(this, selectedImage!!)
        //        imgProfile.setImageBitmap(thumbnail);


    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


    fun getPath(context: Context, uri: Uri): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }

    private fun getuserDetails(authToken: String, langCode: String) {

//        progress.createDialog(false)
//        progress.DialogMessage(getString(R.string.please_wait))
//        progress.showDialog()

        viewDilaog.showDialog()
        utils.hideKeyboard()
        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val loginResponceCall = apiService?.USERDETAILS_CALL(authToken, langCode)
        loginResponceCall?.enqueue(object : Callback<UserDetailsResponce> {
            override fun onResponse(@NonNull call: Call<UserDetailsResponce>, @NonNull response: Response<UserDetailsResponce>) {
                viewDilaog.hideDialog()
                val userDetails = response.body()

                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (response.body()?.status == 1) {


                        Log.e("fname", " " + userDetails!!.data.firstName)

                        et_name.setText(userDetails.data.firstName)
                        et_lname.setText(userDetails.data.lastName)
                        et_email.setText(userDetails.data.email)
                        et_number.setText(userDetails.data.phone)

                        helper.initPref()
                        helper.SaveStringPref(AppConfig.PREFERENCE.USER_FNAME, userDetails.data.firstName)
                        helper.SaveStringPref(AppConfig.PREFERENCE.USER_LNAME, userDetails.data.lastName)
                        helper.SaveStringPref(AppConfig.PREFERENCE.USER_EMAIL, userDetails.data.email)
                        helper.SaveStringPref(AppConfig.PREFERENCE.USER_CONTACTNO, userDetails.data.phone)
                        helper.ApplyPref()

                    } else {

                        Toast.makeText(
                            applicationContext,
                            userDetails?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                }else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<UserDetailsResponce>, @NonNull t: Throwable) {
                viewDilaog.hideDialog()
                Log.e(TAG, "onFailure: " + t.cause)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun login() {

        helper.clearAllPrefs()
        startActivity(
            Intent(this, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }


    private fun updateDetails(
        authToken: String,
        fname: String,
        lname: String,
        email: String,
        number: String,
        langCode: String
    ) {

//        progress.createDialog(false)
//        progress.DialogMessage(getString(R.string.please_wait))
//        progress.showDialog()
        viewDilaog.showDialog()
        utils.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val loginResponceCall =
            apiService?.UPDATE_PROFILE(authToken, fname, lname, email, number, langCode)


        loginResponceCall?.enqueue(object : Callback<UserDetailsResponce> {
            override fun onResponse(@NonNull call: Call<UserDetailsResponce>, @NonNull response: Response<UserDetailsResponce>) {
                viewDilaog.hideDialog()
                val userDetails = response.body()


                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (response.body()?.status == 1) {


                        Toast.makeText(
                            applicationContext,
                            userDetails?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        getuserDetails(token, langCode =helperlang.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")!!)

//
//                        et_name.setText(userDetails!!.data.detail.firstName)
//                        et_lname.setText(userDetails!!.data.detail.lastName)
//                        et_email.setText(userDetails!!.data.detail.email)
//                        et_number.setText(userDetails!!.data.detail.phone)

                    } else {

                        Toast.makeText(
                            applicationContext,
                            userDetails?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<UserDetailsResponce>, @NonNull t: Throwable) {
                viewDilaog.hideDialog()
                Log.e(TAG, "onFailure: " + t.cause)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


}
