package com.skripsi.lostfoundstis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skripsi.lostfoundstis.adapter.RecycleAdapter
import com.skripsi.lostfoundstis.util.Configuration
import org.json.JSONException
import org.json.JSONObject
import java.util.*


@Suppress("UNCHECKED_CAST", "DEPRECATION")
class Pencarian : Fragment() {

    private var recyView: RecyclerView? = null
    private var requestQueue: RequestQueue? = null
    private var stringRequest: StringRequest? = null
    private val config: Configuration = Configuration()
    private var listCari = ArrayList<HashMap<String, String>>()
    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.pencarian, container, false)
        recyView = rootView?.findViewById(R.id.recCari)

        recyView?.addOnItemTouchListener(object : OnItemTouchListener {
            var gestureDetector = GestureDetector(
                context,
                object : SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }
                })

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    val position = rv.getChildAdapterPosition(child)
                    val intentCari = Intent(requireContext(), DetailPencarian::class.java)
                    val map: HashMap<String, String> = listCari[position]
                    val cariId = map[config.TAG_CARI_ID]
                    intentCari.putExtra(config.CARI_ID, cariId)
                    startActivity(intentCari)

                    Toast.makeText(
                        context,
                        "membuka detail pencarian",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyView?.layoutManager = llm

        showPencarian()

        val fab: FloatingActionButton? = rootView?.findViewById(R.id.fabCari)
        fab?.setOnClickListener {
            val i = Intent(requireContext(), TambahPencarian::class.java)
            startActivity(i)
        }

        return rootView
    }

    private fun showPencarian(){
        requestQueue = Volley.newRequestQueue(context)
        listCari = ArrayList()
        stringRequest = StringRequest(
            Request.Method.GET, config.URL_GET_ALL_CARI,
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
                        listCari.add(map)
                        val adapter = RecycleAdapter(activity as Context, listCari)
                        recyView?.adapter = adapter
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error -> Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show() }
        requestQueue?.add(stringRequest)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if(isVisible){
            val v = rootView
            if (v == null){
                Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show()
                return
            }
        }
    }
}