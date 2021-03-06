@file:Suppress("DEPRECATION")

package com.skripsi.lostfoundstis

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidquery.AQuery
import com.androidquery.callback.AjaxCallback
import com.androidquery.callback.AjaxStatus
import com.rengwuxian.materialedittext.MaterialEditText
import com.skripsi.lostfoundstis.util.Configuration
import com.skripsi.lostfoundstis.util.SessionManager
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "ControlFlowWithEmptyBody", "PrivatePropertyName"
)
class TambahPencarian : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {
    private var sessionManager: SessionManager? = null
    private var config : Configuration = Configuration()
    private var aQuery: AQuery? = null

    private val PICK_IMAGE_REQUEST = 1
    private val STORAGE_PERMISSION_CODE = 123
    private var bitmap: Bitmap? = null
    private var filePath: Uri? = null

    private var requestQueueJbar: RequestQueue? = null
    private var stringRequestJbar: StringRequest? = null
    private var requestQueueLok: RequestQueue? = null
    private var stringRequestLok: StringRequest? = null

    private var imgFoto: ImageView? = null
    private var txtJdl: MaterialEditText? = null
    private var kelBarSpin: Spinner? = null
    private var katBarSpin: Spinner? = null
    private var gdLokSpin: Spinner? = null
    private var rgLokSpin: Spinner? = null
    private var wktSpin: Spinner? = null
    private var tglPick: DatePicker? = null
    private var txtCir: MaterialEditText? = null

    private var listJbar = ArrayList<HashMap<String, String>>()
    private var listLok = ArrayList<HashMap<String, String>>()

    private var parentKelBar: String? = null
    private var parentKatBar: String? = null
    private var parentGdLok: String? = null
    private var parentRgLok: String? = null
    private var parentWkt: String? = null

    private var dateTime: Long? = null
    private var date: Date? = null
    private var dateFormat: DateFormat? = null
    private var parentTgl: String? = null

    private var adapterKatBar: ArrayAdapter<*>? = null
    private var adapterRgLok: ArrayAdapter<*>? = null

    private var btnImageAdd: ImageButton? = null
    private var btnConfirmAdd: Button? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tambah_pencarian)

        requestStoragePermission()

        sessionManager = SessionManager(applicationContext)
        aQuery = AQuery(this)

        val toolbar: Toolbar? = findViewById(R.id.toolbarAdd)
        setSupportActionBar(toolbar)

        val ab = supportActionBar
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true)
        } else {
            throw NullPointerException("Ada Kesalahan")
        }

        setupView()

        daftarJbar()
        daftarLok()

        btnImageAdd = findViewById(R.id.btnAddImage)
        btnImageAdd?.setOnClickListener(this)

        kelBarSpin?.onItemSelectedListener = this
        katBarSpin?.onItemSelectedListener = this
        gdLokSpin?.onItemSelectedListener = this
        rgLokSpin?.onItemSelectedListener = this
        wktSpin?.onItemSelectedListener = this

        btnConfirmAdd = findViewById(R.id.btnAddConfirm)
        btnConfirmAdd?.setOnClickListener(this)
    }

    @SuppressLint("SimpleDateFormat")
    private fun tambahMyCari(){
        // kirim data ke server
        val url: String = config.URL_ADD_CARI
        val param: MutableMap<String, String> = HashMap()
        param[config.TAG_CARI_ID_USER] = sessionManager?.getUserId.toString()
        param[config.TAG_CARI_ID_JBAR] = parentKatBar.toString()
        param[config.TAG_CARI_ID_LOK] = parentRgLok.toString()
        param[config.TAG_CARI_JDL] = txtJdl?.text.toString()
        param[config.TAG_CARI_CIRI] = txtCir?.text.toString()
        param[config.TAG_CARI_WKT] = parentWkt.toString()

        dateTime = tglPick?.calendarView?.date
        date = dateTime?.let { Date(it) }
        dateFormat = SimpleDateFormat("yyyy-MM-dd")
        parentTgl = dateFormat?.format(date)
        param[config.TAG_CARI_TGL] = parentTgl.toString()

        // menampilkan progressbar saat mengirim data
        val pd = ProgressDialog(this)
        pd.isIndeterminate = true
        pd.setCancelable(false)
        pd.setInverseBackgroundForced(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setTitle("Info")
        pd.setMessage("Tambah Pencarian")
        pd.show()
        try {
            // format ambil data
            aQuery?.progress(pd)?.ajax(
                url, param,
                String::class.java,
                object : AjaxCallback<String>() {
                    override fun callback(
                        url: String,
                        objecT: String,
                        status: AjaxStatus
                    ) {
                        try {
                            val jsonObject = JSONObject(objecT)
                            val result = jsonObject.getString("result")
                            val msg = jsonObject.getString("msg")
                            val name = jsonObject.getString("pname")
                            if (result.equals("true", ignoreCase = true)) {
                                if(filePath != null){
                                    try {
                                        //mendapatkan path photo di handphone
                                        val path = getPath(filePath)
                                        val uploadId = UUID.randomUUID().toString()

                                        //Creating a multi part request
                                        MultipartUploadRequest(applicationContext, uploadId, config.URL_UPLOAD_CARI)
                                            .addFileToUpload(path, "image") //Adding file
                                            .addParameter("name", name)
                                            .setNotificationConfig(UploadNotificationConfig())
                                            .setMaxRetries(2)
                                            .startUpload() //Starting the upload
                                    } catch (exc: Exception) {
                                        pesan(applicationContext, "Gagal upload foto")
                                    }
                                }
                                val i = Intent(applicationContext, MainActivity::class.java)
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(i)
                                pesan(applicationContext, msg)
                                finish()
                            } else {
                                pesan(applicationContext, msg)
                            }
                        } catch (e: JSONException) {
                            pesan(applicationContext, "Error convert data json")
                        }
                    }
                })
        } catch (e: Exception) {
            pesan(applicationContext, "Gagal mengambil data")
        }
    }

    private fun dialogTambah() {
        val alertDialogBuilder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(
            this
        )

        // set title dialog
        alertDialogBuilder.setTitle("Tambah Pencarian Ini?")

        // set pesan dari dialog
        alertDialogBuilder
            .setMessage("Klik Ya untuk tambah pencarian")
            .setIcon(R.mipmap.ic_launcher)
            .setCancelable(false)
            .setPositiveButton(
                "Ya"
            ) { _, _ ->
                tambahMyCari()
            }
            .setNegativeButton(
                "Tidak"
            ) { dialog, _ ->
                dialog.cancel()
            }

        // membuat alert dialog dari builder
        val alertDialog: android.app.AlertDialog? = alertDialogBuilder.create()

        // menampilkan alert dialog
        alertDialog?.show()
    }

    // fungsi ketika tombol tambah pencarian diklik
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddImage -> {
                showFileChooser()
            }
            R.id.btnAddConfirm -> {
                txtJdl?.error = null
                txtCir?.error = null
                // cek kebaradan teks
                when {
                    isEmpty(txtJdl) -> {
                        txtJdl?.error = "Judul masih kosong"
                        txtJdl?.requestFocus()
                    }
                    isEmpty(txtCir) -> {
                        txtCir?.error = "Ciri barang masih kosong"
                        txtCir?.requestFocus()
                    }
                    else -> {
                        btnConfirmAdd?.setOnClickListener { dialogTambah() }
                    }
                }
            }
        }
    }

    // fungsi untuk mendapatkan data dari spinner
    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (parent.id) {
            R.id.kel_add_spin -> {
                val listKelBar = ArrayList<String>()
                for (i in 0 until listJbar.size){
                    val hashmap = listJbar[i]
                    if (hashmap[config.TAG_KEL_BAR] == parent.selectedItem){
                        listKelBar.add(hashmap[config.TAG_KAT_BAR].toString())
                    }
                }
                parentKelBar = parent.selectedItem.toString()
                adapterKatBar = ArrayAdapter(
                    this,
                    R.layout.simple_list_item,
                    listKelBar
                )
                adapterKatBar?.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                katBarSpin?.adapter = adapterKatBar
            }

            R.id.kat_add_spin -> {
                for (i in 0 until listJbar.size){
                    val hashmap = listJbar[i]
                    if (hashmap[config.TAG_KEL_BAR] == parentKelBar && hashmap[config.TAG_KAT_BAR] == parent.selectedItem){
                        parentKatBar = hashmap[config.TAG_ID_JBAR].toString()
                    }
                }
            }

            R.id.gd_add_spin -> {
                val listRgLok = ArrayList<String>()
                for (i in 0 until listLok.size){
                    val hashmap = listLok[i]
                    if (hashmap[config.TAG_GD_LOK] == parent.selectedItem){
                        listRgLok.add(hashmap[config.TAG_RG_LOK].toString())
                    }
                }
                parentGdLok = parent.selectedItem.toString()
                adapterRgLok = ArrayAdapter(
                    this,
                    R.layout.simple_list_item,
                    listRgLok
                )
                adapterRgLok?.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                rgLokSpin?.adapter = adapterRgLok
            }

            R.id.rg_add_spin -> {
                for (i in 0 until listLok.size){
                    val hashmap = listLok[i]
                    if (hashmap[config.TAG_GD_LOK] == parentGdLok && hashmap[config.TAG_RG_LOK] == parent.selectedItem){
                        parentRgLok = hashmap[config.TAG_ID_LOK].toString()
                    }
                }
            }

            R.id.wkt_add_spin -> {
                parentWkt = parent.selectedItem.toString()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    // mengambil data dan menampilkan foto yang dipilih
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imgFoto?.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // fungsi untuk mendapatkan data spinner jenis barang
    private fun daftarJbar(){
        requestQueueJbar = Volley.newRequestQueue(this)
        listJbar = ArrayList()
        stringRequestJbar = StringRequest(
            Request.Method.GET, config.URL_GET_JBAR,
            { response ->
                Log.d("response ", response!!)
                try {
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray(config.TAG_JBAR_JSON)
                    for (a in 0 until jsonArray.length()) {
                        val json = jsonArray.getJSONObject(a)
                        val map = HashMap<String, String>()
                        map[config.TAG_ID_JBAR] = json.getString(config.TAG_ID_JBAR)
                        map[config.TAG_KEL_BAR] = json.getString(config.TAG_KEL_BAR)
                        map[config.TAG_KAT_BAR] = json.getString(config.TAG_KAT_BAR)
                        listJbar.add(map)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
        requestQueueJbar?.add(stringRequestJbar)
    }

    // fungsi untuk mendapatkan data spinner lokasi kampus
    private fun daftarLok(){
        // mendapatkan data spinner lokasi kampus
        requestQueueLok = Volley.newRequestQueue(this)
        listLok = ArrayList()
        stringRequestLok = StringRequest(
            Request.Method.GET, config.URL_GET_LOK,
            { response ->
                Log.d("response ", response!!)
                try {
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray(config.TAG_LOK_JSON)
                    for (a in 0 until jsonArray.length()) {
                        val json = jsonArray.getJSONObject(a)
                        val map = HashMap<String, String>()
                        map[config.TAG_ID_LOK] = json.getString(config.TAG_ID_LOK)
                        map[config.TAG_GD_LOK] = json.getString(config.TAG_GD_LOK)
                        map[config.TAG_RG_LOK] = json.getString(config.TAG_RG_LOK)
                        listLok.add(map)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
        requestQueueLok?.add(stringRequestLok)
    }

    // fungsi untuk mendapatkan view dari layout
    private fun setupView(){
        imgFoto = findViewById(R.id.fbr_add)
        txtJdl = findViewById(R.id.jdl_add)
        kelBarSpin = findViewById(R.id.kel_add_spin)
        katBarSpin = findViewById(R.id.kat_add_spin)
        gdLokSpin = findViewById(R.id.gd_add_spin)
        rgLokSpin = findViewById(R.id.rg_add_spin)
        wktSpin = findViewById(R.id.wkt_add_spin)
        tglPick = findViewById(R.id.tgl_add)
        txtCir = findViewById(R.id.cir_add)
    }

    // Fungsi untuk menampilkan pesan toast
    private fun pesan(c: Context?, msg: String?) {
        Toast.makeText(c, msg, Toast.LENGTH_LONG).show()
    }

    // fungsi untuk mengecek apakah isian kosong
    private fun isEmpty(editText: MaterialEditText?): Boolean {
        // jika banyak huruf lebih dari 0
        return editText?.text.toString().trim { it <= ' ' }.isEmpty()
    }

    // fungsi untuk request permission
    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) return
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    // fungsi ini akan terpanggil ketika user ingin memilih untuk mengizinkan permission atau tidak
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        // mengecek request code untuk request yang dijalankan
        if (requestCode == STORAGE_PERMISSION_CODE) {

            // mengecek apakah permission diizinkan
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // menampilkan toast jika permission diizinkan
                Toast.makeText(
                    this,
                    "Permission granted now you can read the storage",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // menampilkan toast jika permission tidak diizinkan
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    // fungsi untuk membuka galeri foto
    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), PICK_IMAGE_REQUEST)
    }

    // fungsi untuk mendapatkan path file
    private fun getPath(uri: Uri?): String {
        var cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor!!.moveToFirst()
        var documentId = cursor.getString(0)
        documentId = documentId.substring(documentId.lastIndexOf(":") + 1)
        cursor.close()
        cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, MediaStore.Images.Media._ID + " = ? ", arrayOf(documentId), null
        )
        cursor!!.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }
}