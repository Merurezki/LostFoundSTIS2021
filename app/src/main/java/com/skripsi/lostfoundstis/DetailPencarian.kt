@file:Suppress("DEPRECATION")

package com.skripsi.lostfoundstis

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.skripsi.lostfoundstis.util.Configuration
import org.json.JSONException
import org.json.JSONObject


@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNREACHABLE_CODE")
class DetailPencarian : AppCompatActivity() {
    private var requestQueue: RequestQueue? = null
    private var stringRequest: StringRequest? = null
    private var config : Configuration = Configuration()
    private var idCari: String? = null

    private var imgFoto: ImageView? = null
    private var txtJdl: TextView? = null
    private var txtJbr: TextView? = null
    private var txtLok: TextView? = null
    private var txtWkt: TextView? = null
    private var txtCir: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_pencarian)

        val toolbar: Toolbar? = findViewById(R.id.toolbarPencarian)
        setSupportActionBar(toolbar)

        val ab = supportActionBar
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true)
        } else {
            throw NullPointerException("Ada Kesalahan")
        }

        val intent = intent
        idCari = intent.getStringExtra(config.CARI_ID)

        setuView()

        showDetialPencarian()
    }

    @SuppressLint("SetTextI18n")
    private fun showDetialPencarian(){
        requestQueue = Volley.newRequestQueue(this)
        stringRequest = StringRequest(
            Request.Method.GET, config.URL_GET_ONE_CARI + idCari,
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
                                config.TAG_CARI_KAT)
                        txtLok?.text =
                            json.getString(config.TAG_CARI_GD) + ", " + json.getString(
                                config.TAG_CARI_RG)
                        txtWkt?.text = json.getString(config.TAG_CARI_WKT) + ", " + json.getString(
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
    }

    private fun setuView(){
        imgFoto = findViewById(R.id.fbr_dcari)
        txtJdl = findViewById(R.id.jdl_dcari)
        txtJbr = findViewById(R.id.jbr_dcari)
        txtLok = findViewById(R.id.lok_dcari)
        txtWkt = findViewById(R.id.wkt_dcari)
        txtCir = findViewById(R.id.cir_dcari)
    }
}