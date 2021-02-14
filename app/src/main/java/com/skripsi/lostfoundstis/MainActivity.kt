package com.skripsi.lostfoundstis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.skripsi.lostfoundstis.ui.main.SectionsPagerAdapter
import org.json.JSONException
import org.json.JSONObject

@Suppress("DEPRECATION", "RedundantOverride")
class MainActivity : AppCompatActivity() {
    private var sessionManager: SessionManager? = null
    private var mDrawerLayout: DrawerLayout? = null

    private var requestQueue: RequestQueue? = null
    private var stringRequest: StringRequest? = null
    private var config : Configuration = Configuration()
    private var idUser: String? = null

    private var headerView: View? = null
    private var txtNama: TextView? = null
    private var txtNim: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(applicationContext)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val mToolbar: Toolbar? = findViewById(R.id.toolbarMain)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val mNavigationView: NavigationView = findViewById<View>(R.id.navView) as NavigationView
        mNavigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = !menuItem.isChecked
            mDrawerLayout!!.closeDrawers()
            when (menuItem.itemId) {
                R.id.navProfilSaya -> {
                    Toast.makeText(applicationContext, "Ini Menu Profil Saya", Toast.LENGTH_SHORT)
                        .show()
                    true
                }
                R.id.navPencarianSaya -> {
                    startActivity(Intent(applicationContext, MyPencarian::class.java))
                    true
                }
                R.id.navPengaturan -> {
                    Toast.makeText(applicationContext, "Ini Menu Pengaturan", Toast.LENGTH_SHORT)
                        .show()
                    true
                }
                R.id.navTentang -> {
                    Toast.makeText(applicationContext, "Ini Menu Pengaturan", Toast.LENGTH_SHORT)
                        .show()
                    true
                }
                R.id.navLogout -> {
                    sessionManager?.logout()
                    finish()
                    true
                }
                else -> {
                    Toast.makeText(applicationContext, "Ada Kesalahan ", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }

        headerView = mNavigationView.getHeaderView(0)

        txtNama = headerView?.findViewById(R.id.navUsername)
        txtNim  = headerView?.findViewById(R.id.navNim)

        mDrawerLayout = findViewById<View>(R.id.drawerMenu) as DrawerLayout
        val actionBarDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            mToolbar,
            R.string.openDrawer,
            R.string.closeDrawer
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                idUser  = sessionManager?.getUserId
                requestQueue = Volley.newRequestQueue(applicationContext)
                stringRequest = StringRequest(
                    Request.Method.GET, config.URL_GET_PROFIL + idUser,
                    { response ->
                        Log.d("response ", response!!)
                        try {
                            val jsonObject = JSONObject(response)
                            val jsonArray = jsonObject.getJSONArray(config.TAG_PROFIL_JSON)
                            for (a in 0 until jsonArray.length()) {
                                val json = jsonArray.getJSONObject(a)
                                txtNama?.text = json.getString(config.TAG_NAMA_USER)
                                txtNim?.text = sessionManager?.getUserId
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                ) { error -> Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show() }
                requestQueue?.add(stringRequest)
                invalidateOptionsMenu()
            }
        }
        mDrawerLayout?.setDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }
}