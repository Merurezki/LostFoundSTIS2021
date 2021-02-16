package com.skripsi.lostfoundstis.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.skripsi.lostfoundstis.LoginActivity
import com.skripsi.lostfoundstis.MainActivity

@SuppressLint("CommitPrefEdits")
class SessionManager(var context: Context) {
    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor
    private var mode = 0

    fun createSession(userId: String) {
        editor.putBoolean(is_login, true)
        editor.putString(key_id, userId)
        editor.commit()
    }

    fun checkLogin() {
        if (!isLogin()) {
            val i = Intent(context, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        } else {
            val i = Intent(context, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }
    }

    fun isLogin(): Boolean {
        return pref.getBoolean(is_login, false)
    }

    fun logout() {
        editor.clear()
        editor.commit()
        val i = Intent(context, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
    }

    /*val userDetails: HashMap<String, String?>
        get() {
            val user = HashMap<String, String?>()
            user[pref_name] =
                pref.getString(pref_name, null)
            user[key_id] =
                pref.getString(key_id, null)
            return user
        }*/

    val getUserId: String?
        get() {
            return pref.getString(key_id, null)
        }

    companion object {
        private const val pref_name = "crudpref"
        private const val is_login = "islogin"
        const val key_id = "userid"
    }

    init {
        pref = context.getSharedPreferences(pref_name, mode)
        editor = pref.edit()
    }
}