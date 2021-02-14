@file:Suppress("DEPRECATION")

package com.skripsi.lostfoundstis

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject


@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailMyPencarian : AppCompatActivity(), View.OnClickListener {
    private var requestQueue: RequestQueue? = null
    private var stringRequest: StringRequest? = null
    private var config : Configuration = Configuration()
    private var idMyCari: String? = null

    private var imgFoto: ImageView? = null
    private var txtJdl: TextView? = null
    private var txtJbr: TextView? = null
    private var txtLok: TextView? = null
    private var txtWkt: TextView? = null
    private var txtCir: TextView? = null

    private var btnEdit: Button? = null
    private var btnHapus: Button? = null
    private var btnSelesai: Button? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_my_pencarian)

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
        txtJbr = findViewById(R.id.jbr_dmycari)
        txtLok = findViewById(R.id.lok_dmycari)
        txtWkt = findViewById(R.id.wkt_dmycari)
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
                        txtJdl?.text = json.getString(config.TAG_CARI_JDL)
                        txtJbr?.text =
                            json.getString(config.TAG_CARI_KEL) + ", " + json.getString(
                                config.TAG_CARI_KAT
                            )
                        txtLok?.text =
                            json.getString(config.TAG_CARI_GD) + ", " + json.getString(
                                config.TAG_CARI_RG
                            )
                        txtWkt?.text =
                            json.getString(config.TAG_CARI_WKT) + ", " + json.getString(
                                config.TAG_CARI_TGL)
                        txtCir?.text = json.getString(config.TAG_CARI_CIRI)
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

        btnEdit = findViewById(R.id.btnEdit)
        btnHapus = findViewById(R.id.btnHapus)
        btnSelesai= findViewById(R.id.btnSelesai)

        btnEdit?.setOnClickListener(this)
        btnHapus?.setOnClickListener { dialogHapus() }
        btnSelesai?.setOnClickListener { dialogSelesai() }
    }

    override fun onClick(view: View) {
        // Mendapatkan Semua ID yang Terdapat pada Masing-masing Widget
        when (view.id) {
            R.id.btnEdit -> {
                val i = Intent(this, EditDetailMyPencarian::class.java)
                i.putExtra(config.TAG_CARI_ID, idMyCari)
                startActivity(i)
            }
        }
    }

    private fun dialogHapus() {
        val alertDialogBuilder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(
            this
        )

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Pencarian Ini?")

        // set pesan dari dialog
        alertDialogBuilder
            .setMessage("Klik Ya untuk hapus pencarian")
            .setIcon(R.mipmap.ic_launcher)
            .setCancelable(false)
            .setPositiveButton(
                "Ya"
            ) { _, _ ->
                requestQueue = Volley.newRequestQueue(this)
                stringRequest = StringRequest(
                    Request.Method.GET, config.URL_DELETE_CARI + idMyCari,
                    { response ->
                        Log.d("response ", response!!)
                        try {
                            Toast.makeText(
                                this,
                                "Pencarian Kamu Berhasil Dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                ) { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
                requestQueue?.add(stringRequest)

                val i = Intent(this, MyPencarian::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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

    private fun dialogSelesai() {
        val alertDialogBuilder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(
            this
        )

        // set title dialog
        alertDialogBuilder.setTitle("Selesaikan Pencarian Ini?")

        // set pesan dari dialog
        alertDialogBuilder
            .setMessage("Klik Ya untuk selesaikan pencarian")
            .setIcon(R.mipmap.ic_launcher)
            .setCancelable(false)
            .setPositiveButton(
                "Ya"
            ) { _, _ ->
                requestQueue = Volley.newRequestQueue(this)
                stringRequest = StringRequest(
                    Request.Method.GET, config.URL_FINISH_CARI + idMyCari,
                    { response ->
                        Log.d("response ", response!!)
                        try {
                            Toast.makeText(
                                this,
                                "Pencarian Kamu Berhasil Diselesaikan",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                ) { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
                requestQueue?.add(stringRequest)

                val i = Intent(this, MyPencarian::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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
}