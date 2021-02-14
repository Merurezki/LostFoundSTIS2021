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
import com.rengwuxian.materialedittext.MaterialEditText
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class TambahPencarian : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {
    private var sessionManager: SessionManager? = null

    private var requestQueueJbar: RequestQueue? = null
    private var stringRequestJbar: StringRequest? = null
    private var requestQueueLok: RequestQueue? = null
    private var stringRequestLok: StringRequest? = null
    private var config : Configuration = Configuration()

    private var listJbar = ArrayList<HashMap<String, String>>()
    private var listLok = ArrayList<HashMap<String, String>>()

    private var imgFoto: ImageView? = null
    private var txtJdl: MaterialEditText? = null
    private var kelBarSpin: Spinner? = null
    private var katBarSpin: Spinner? = null
    private var gdLokSpin: Spinner? = null
    private var rgLokSpin: Spinner? = null
    private var wktSpin: Spinner? = null
    private var txtTgl: DatePicker? = null
    private var txtCir: MaterialEditText? = null

    private var adapterKatBar: ArrayAdapter<*>? = null
    private var adapterRgLok: ArrayAdapter<*>? = null

    private var btnConfirmAdd: Button? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tambah_pencarian)

        /*sessionManager = SessionManager(applicationContext)*/

        val toolbar: Toolbar? = findViewById(R.id.toolbarTambah)
        setSupportActionBar(toolbar)

        val ab = supportActionBar
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true)
        } else {
            throw NullPointerException("Ada Kesalahan")
        }

        imgFoto = findViewById(R.id.fbr_dmycari)
        txtJdl = findViewById(R.id.jdl_dmycari)
        kelBarSpin = findViewById(R.id.kel_spin)
        katBarSpin = findViewById(R.id.kat_spin)
        gdLokSpin = findViewById(R.id.gd_spin)
        rgLokSpin = findViewById(R.id.rg_spin)
        wktSpin = findViewById(R.id.wkt_spin)
        txtTgl = findViewById(R.id.tgl_dmycari)
        txtCir = findViewById(R.id.cir_dmycari)

        kelBarSpin?.onItemSelectedListener = this
//        katBarSpin?.setOnClickListener(this)
//        gdLokSpin?.setOnClickListener(this)
//        rgLokSpin?.setOnClickListener(this)
//        wktSpin?.setOnClickListener(this)

        val adapterKelBar: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                this,
                R.layout.support_simple_spinner_dropdown_item
            )

        btnConfirmAdd = findViewById(R.id.btnAddConfirm)
        btnConfirmAdd?.setOnClickListener { dialogTambah() }

        // Mendapatkan data spinner jenis barang
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

        // Mendapatkan data spinner lokasi kampus
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


                val i = Intent(this, MainActivity::class.java)
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {

    }

    /**
     *
     * Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.
     *
     * Implementers can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id The row id of the item that is selected
     */
    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if (parent.id == R.id.kel_spin){
            if (position == 0){
                val listKelBar: ArrayList<Any?>? = null
                for (map in listJbar ){
                    if (map[config.TAG_KEL_BAR] == "Alat Tulis"){
                        listKelBar?.add(map[config.TAG_KEL_BAR])
                    }
                }
                adapterKatBar = ArrayAdapter(
                    this,
                    R.layout.simple_list_item,
                    listKelBar as List<Any?>
                )
            }
            adapterKatBar?.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            katBarSpin?.adapter = adapterKatBar
            /*Toast.makeText(this, "" + kelBarSpin?.selectedItem.toString(), Toast.LENGTH_SHORT)
                .show()*/
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}