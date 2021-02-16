@file:Suppress("DEPRECATION")

package com.skripsi.lostfoundstis

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.rengwuxian.materialedittext.MaterialEditText
import com.skripsi.lostfoundstis.util.Configuration
import com.skripsi.lostfoundstis.util.SessionManager
import org.json.JSONException
import org.json.JSONObject


@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MyProfile : AppCompatActivity(), View.OnClickListener {
    private var sessionManager: SessionManager? = null
    private var requestQueue: RequestQueue? = null
    private var stringRequest: StringRequest? = null
    private var config : Configuration = Configuration()
    private var myIdUser : String? = null

    private var txtNama: TextView? = null
    private var txtNimNip: TextView? = null
    private var txtSts: TextView? = null
    private var txtEmail: TextView? = null
    private var txtTlp: MaterialEditText? = null

    private var btnEditTlp: Button? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile)

        sessionManager = SessionManager(this)
        myIdUser = sessionManager?.getUserId

        val toolbar: Toolbar? = findViewById(R.id.toolbarMyProfile)
        setSupportActionBar(toolbar)

        val ab = supportActionBar
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true)
        } else {
            throw NullPointerException("Ada Kesalahan")
        }

        setupView()

        showMyProfile()
    }

    override fun onClick(view: View) {

    }

    private fun dialogEditTlp() {
        val alertDialogBuilder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(
            this
        )

        // set title dialog
        alertDialogBuilder.setTitle("Ganti Nomor Telepon Ini?")

        // set pesan dari dialog
        alertDialogBuilder
            .setMessage("Klik Ya untuk mengganti nomor telepon")
            .setIcon(R.mipmap.ic_launcher)
            .setCancelable(false)
            .setPositiveButton(
                "Ya"
            ) { _, _ ->
                requestQueue = Volley.newRequestQueue(this)
                stringRequest = StringRequest(
                    Request.Method.GET, config.URL_UPDATE_TLP + myIdUser + "&no_telp=" + txtTlp?.text.toString(),
                    { response ->
                        Log.d("response ", response!!)
                        try {
                            Toast.makeText(
                                this,
                                "Nomor Telepon Berhasil Diganti",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                ) { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
                requestQueue?.add(stringRequest)
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

    private fun showMyProfile(){
        requestQueue = Volley.newRequestQueue(this)
        stringRequest = StringRequest(
            Request.Method.GET, config.URL_GET_PROFIL + myIdUser,
            { response ->
                Log.d("response ", response!!)
                try {
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray(config.TAG_PROFIL_JSON)
                    for (a in 0 until jsonArray.length()) {
                        val json = jsonArray.getJSONObject(a)
                        txtNama?.text = json.getString(config.TAG_NAMA_USER)
                        txtNimNip?.text = myIdUser
                        txtSts?.text = json.getString(config.TAG_STS_USER)
                        txtEmail?.text = json.getString(config.TAG_EMAIL_USER)
                        txtTlp?.setText(json.getString(config.TAG_PHONE_USER))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
        requestQueue?.add(stringRequest)
    }

    private fun setupView(){
        txtNama = findViewById(R.id.nama_user)
        txtNimNip = findViewById(R.id.nimnip_user)
        txtSts = findViewById(R.id.status_user)
        txtEmail = findViewById(R.id.email_user)
        txtTlp = findViewById(R.id.telepon_user)

        btnEditTlp = findViewById(R.id.btnEditTlp)
        btnEditTlp?.setOnClickListener { dialogEditTlp() }
    }
}