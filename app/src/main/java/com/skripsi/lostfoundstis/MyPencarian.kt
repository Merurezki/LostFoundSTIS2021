@file:Suppress("DEPRECATION")

package com.skripsi.lostfoundstis

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.skripsi.lostfoundstis.adapter.RecycleAdapter
import com.skripsi.lostfoundstis.util.Configuration
import com.skripsi.lostfoundstis.util.SessionManager
import org.json.JSONException
import org.json.JSONObject
import java.util.*


@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MyPencarian : AppCompatActivity() {
    private var sessionManager: SessionManager? = null

    private var recyView: RecyclerView? = null
    private var requestQueue: RequestQueue? = null
    private var stringRequest: StringRequest? = null
    private val config: Configuration = Configuration()
    private var listMyCari = ArrayList<HashMap<String, String>>()
    private var idUser: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_pencarian)

        // mendapatkan user id dari login session
        sessionManager = SessionManager(applicationContext)
        idUser = sessionManager?.getUserId

        // membuat tombol navigasi kembali ke halaman sebelumnya
        val toolbar: Toolbar? = findViewById(R.id.toolbarMyPencarian)
        setSupportActionBar(toolbar)

        val ab = supportActionBar
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true)
        } else {
            throw NullPointerException("Ada Kesalahan")
        }

        // Menampilkan daftar / list pencarian saya
        recyView = findViewById(R.id.recMyCari)

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyView?.layoutManager = llm

        showMyPencarian()

        listTouch()
    }

    private fun showMyPencarian(){
        requestQueue = Volley.newRequestQueue(this)
        listMyCari = ArrayList()
        stringRequest = StringRequest(
            Request.Method.GET, config.URL_GET_ALL_SAYA + idUser,
            { response ->
                Log.d("response ", response!!)
                try {
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray(config.TAG_CARI_JSON_ARRAY)
                    for (a in 0 until jsonArray.length()) {
                        val json = jsonArray.getJSONObject(a)
                        val map = HashMap<String, String>()
                        map[config.TAG_CARI_ID] = json.getString(config.TAG_CARI_ID)
                        map[config.TAG_CARI_JDL] = json.getString(config.TAG_CARI_JDL)
                        map[config.TAG_CARI_JBAR] =
                            json.getString(config.TAG_CARI_KEL) + ", " + json.getString(
                                config.TAG_CARI_KAT
                            )
                        map[config.TAG_CARI_LOK] =
                            json.getString(config.TAG_CARI_GD) + ", " + json.getString(
                                config.TAG_CARI_RG
                            )
                        map[config.TAG_CARI_WKT] =
                            json.getString(config.TAG_CARI_WKT) + ", " + json.getString(
                                config.TAG_CARI_TGL)
                        map[config.TAG_CARI_FOTO] = json.getString(config.TAG_CARI_FOTO)
                        listMyCari.add(map)
                        val adapter = RecycleAdapter(this, listMyCari)
                        recyView?.adapter = adapter
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
        requestQueue?.add(stringRequest)
    }

    private fun listTouch(){
        recyView?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            var gestureDetector = GestureDetector(
                this@MyPencarian,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }
                })

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    val position = rv.getChildAdapterPosition(child)
                    val intentCari = Intent(this@MyPencarian, DetailMyPencarian::class.java)
                    val map: HashMap<String, String> = listMyCari[position]
                    val myCariId = map[config.TAG_CARI_ID]
                    intentCari.putExtra(config.CARI_ID, myCariId)
                    overridePendingTransition(0,0)
                    startActivity(intentCari)
                    overridePendingTransition(0,0)

                    Toast.makeText(
                        this@MyPencarian,
                        "membuka detail pencarian saya",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }
}