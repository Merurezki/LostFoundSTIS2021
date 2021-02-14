@file:Suppress("DEPRECATION")

package com.skripsi.lostfoundstis

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.rengwuxian.materialedittext.MaterialEditText
import org.json.JSONException
import org.json.JSONObject


@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EditDetailMyPencarian : AppCompatActivity(), View.OnClickListener {
    private var requestQueue: RequestQueue? = null
    private var stringRequest: StringRequest? = null
    private var config : Configuration = Configuration()
    private var idMyCari: String? = null

    private var imgFoto: ImageView? = null
    private var txtJdl: MaterialEditText? = null
    private var txtKel: Spinner? = null
    private var txtKat: Spinner? = null
    private var txtGd: Spinner? = null
    private var txtRg: Spinner? = null
    private var txtWkt: Spinner? = null
    private var txtTgl: DatePicker? = null
    private var txtCir: MaterialEditText? = null

    private var btnConfirmEdit: Button? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_detail_my_pencarian)

        val toolbar: Toolbar? = findViewById(R.id.toolbarMyPencarian)
        setSupportActionBar(toolbar)

        val ab = supportActionBar
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true)
        } else {
            throw NullPointerException("Ada Kesalahan")
        }

        val intent = intent
        idMyCari = intent.getStringExtra(config.CARI_ID)

        imgFoto = findViewById(R.id.fbr_dmycari)
        txtJdl = findViewById(R.id.jdl_dmycari)
        txtKel = findViewById(R.id.kel_dmycari)
        txtKat = findViewById(R.id.kat_dmycari)
        txtGd  = findViewById(R.id.gd_dmycari)
        txtRg  = findViewById(R.id.rg_dmycari)
        txtWkt = findViewById(R.id.wkt_dmycari)
        txtTgl = findViewById(R.id.tgl_dmycari)
        txtCir = findViewById(R.id.cir_dmycari)

        requestQueue = Volley.newRequestQueue(this)
        stringRequest = StringRequest(
            Request.Method.GET, config.URL_GET_ONE_CARI + idMyCari,
            { response ->
                Log.d("response ", response!!)
                try {
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray(config.TAG_CARI_JSON_ARRAY)
                    for (a in 0 until jsonArray.length()) {
                        val json = jsonArray.getJSONObject(a)
                        txtJdl?.setText(json.getString(config.TAG_CARI_JDL))
                        txtCir?.setText(json.getString(config.TAG_CARI_CIRI))
                        imgFoto?.let {
                            Glide.with(applicationContext)
                                .load(config.URL_IMG_LOC + json.getString(config.TAG_CARI_FOTO))
                                .placeholder(R.mipmap.ic_launcher)
                                .into(it)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
        requestQueue?.add(stringRequest)

        btnConfirmEdit = findViewById(R.id.btnEditConfirm)
        btnConfirmEdit?.setOnClickListener { dialogEdit() }
    }

    private fun dialogEdit() {
        val alertDialogBuilder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(
            this
        )

        // set title dialog
        alertDialogBuilder.setTitle("Edit Pencarian Ini?")

        // set pesan dari dialog
        alertDialogBuilder
            .setMessage("Klik Ya untuk edit pencarian")
            .setIcon(R.mipmap.ic_launcher)
            .setCancelable(false)
            .setPositiveButton(
                "Ya"
            ) { _, _ ->


                val i = Intent(this, DetailMyPencarian::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.putExtra(config.TAG_CARI_ID, idMyCari)
                startActivity(i)
                finish() // keluar dari aktivitas
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {

    }
}