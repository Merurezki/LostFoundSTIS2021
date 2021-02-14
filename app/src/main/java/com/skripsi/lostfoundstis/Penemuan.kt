package com.skripsi.lostfoundstis

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
import org.json.JSONException
import org.json.JSONObject
import java.util.*


@Suppress("UNCHECKED_CAST")
class Penemuan : Fragment() {

    private var recyView: RecyclerView? = null
    private var requestQueue: RequestQueue? = null
    private var stringRequest: StringRequest? = null
    private val config: Configuration = Configuration()
    private var listTemu = ArrayList<HashMap<String, String>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.penemuan, container, false)
        recyView = rootView.findViewById(R.id.recTemu)

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
                    val intentCari = Intent(requireContext(), DetailPenemuan::class.java)
                    val map: HashMap<String, String> = listTemu[position]
                    val temuId = map[config.TAG_TEMU_ID]
                    intentCari.putExtra(config.TEMU_ID, temuId)
                    startActivity(intentCari)

                    Toast.makeText(
                        context,
                        "membuka detail penemuan",
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

        requestQueue = Volley.newRequestQueue(context)
        listTemu = ArrayList()
        stringRequest = StringRequest(
            Request.Method.GET, config.URL_GET_ALL_TEMU,
            { response ->
                Log.d("response ", response!!)
                try {
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray(config.TAG_TEMU_JSON_ARRAY)
                    for (a in 0 until jsonArray.length()) {
                        val json = jsonArray.getJSONObject(a)
                        val map = HashMap<String, String>()
                        map[config.TAG_TEMU_ID] = json.getString(config.TAG_TEMU_ID)
                        map[config.TAG_TEMU_JDL] = json.getString(config.TAG_TEMU_JDL)
                        map[config.TAG_TEMU_JBAR] =
                            json.getString(config.TAG_TEMU_KEL) + ", " + json.getString(
                                config.TAG_TEMU_KAT)
                        map[config.TAG_TEMU_LOK] =
                            json.getString(config.TAG_TEMU_GD) + ", " + json.getString(
                                config.TAG_TEMU_RG)
                        map[config.TAG_TEMU_WKT] =
                            json.getString(config.TAG_TEMU_WKT) + ", " + json.getString(
                                config.TAG_TEMU_TGL)
                        map[config.TAG_TEMU_FOTO] = json.getString(config.TAG_TEMU_FOTO)
                        listTemu.add(map)
                        val adapter = PenemuanAdapter(activity as MainActivity, listTemu)
                        recyView?.adapter = adapter
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error -> Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show() }
        requestQueue?.add(stringRequest)
    return rootView
    }
}