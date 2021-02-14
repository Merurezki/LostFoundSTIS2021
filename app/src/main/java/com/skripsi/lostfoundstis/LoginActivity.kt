@file:Suppress("DEPRECATION")

package com.skripsi.lostfoundstis


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidquery.AQuery
import com.androidquery.callback.AjaxCallback
import com.androidquery.callback.AjaxStatus
import com.rengwuxian.materialedittext.MaterialEditText
import org.json.JSONException
import org.json.JSONObject
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LoginActivity : AppCompatActivity() {
    var sessionManager: SessionManager? = null

    private var logtxtUserId: MaterialEditText? = null
    private var logtxtPassword: MaterialEditText? = null
    private var logbtnLogin: Button? = null
    private val config: Configuration = Configuration()

    var context: Context? = null
    private var aQuery: AQuery? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)
        sessionManager = SessionManager(applicationContext)

        context = this
        aQuery = AQuery(context)

        setupView()
        logbtnLogin?.setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        logtxtUserId?.error = null
        logtxtPassword?.error = null
        // cek kebaradan teks
        when {
            isEmpty(logtxtUserId) -> {
                logtxtUserId?.error = "Email masih kosong"
                logtxtUserId?.requestFocus()
            }
            isEmpty(logtxtPassword) -> {
                logtxtPassword?.error = "Password masih kosong"
                logtxtPassword?.requestFocus()
            }
            else -> {
                // kirim data ke server
                val url: String = config.URL_USER_LOGIN
                val param: MutableMap<String, String> = HashMap()
                param["id_user"] = logtxtUserId?.text.toString()
                param["password"] = logtxtPassword?.text.toString()

                // menampilkan progressbar saat mengirim data
                val pd = ProgressDialog(context)
                pd.isIndeterminate = true
                pd.setCancelable(false)
                pd.setInverseBackgroundForced(false)
                pd.setCanceledOnTouchOutside(false)
                pd.setTitle("Info")
                pd.setMessage("Login")
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
                                    if (result.equals("true", ignoreCase = true)) {
                                        sessionManager?.createSession(param["id_user"].toString())

                                        startActivity(Intent(context, MainActivity::class.java))
                                        pesan(context, msg)
                                        finish()
                                    } else {
                                        pesan(context, msg)
                                    }
                                } catch (e: JSONException) {
                                    pesan(context, "Error convert data json")
                                }

                            }
                        })
                } catch (e: Exception) {
                    pesan(context, "Gagal mengambil data")
                }
            }
        }
    }

    private fun setupView() {
        logtxtUserId = findViewById(R.id.logtxtUserId)
        logtxtPassword = findViewById(R.id.logtxtPassword)
        logbtnLogin = findViewById(R.id.logbtnLogin)
    }

    fun pesan(c: Context?, msg: String?) {
        Toast.makeText(c, msg, Toast.LENGTH_LONG).show()
    }

    // Fungsi untuk mengecek apakah isian kosong
    private fun isEmpty(editText: MaterialEditText?): Boolean {
        // Jika banyak huruf lebih dari 0
        return editText?.text.toString().trim { it <= ' ' }.isEmpty()
    }
}