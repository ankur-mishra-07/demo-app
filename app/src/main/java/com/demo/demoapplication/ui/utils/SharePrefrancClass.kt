package com.demo.demoapplication.ui.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.demo.demoapplication.ui.utils.SharePrefrancClass

class SharePrefrancClass {
    var preference: SharedPreferences?

    /**
     * savePref() for save
     *
     * @param key,value Key value of Shared Prefrance
     */
    fun savePref(key: String?, `val`: String?) {
        if (preference != null) {
            val editor = preference!!.edit()
            editor.putString(key, `val`)
            editor.apply()
        }
    }

    fun saveLongPref(key: String?, `val`: Long?) {
        if (preference != null) {
            val editor = preference!!.edit()
            editor.putLong(key, `val`!!)
            editor.apply()
        }
    }

    fun saveObject(key: String?, `object`: Any?) {
        if (preference != null) {
            val prefsEditor = preference!!.edit()
            val gson = Gson()
            val json = gson.toJson(`object`)
            prefsEditor.putString(key, json)
            prefsEditor.apply()
        }
    }
    /*TO RETRIEVE
    * Gson gson = new Gson();
String json = mPrefs.getString("MyObject", "");
MyObject obj = gson.fromJson(json, MyObject.class);*/
    /**
     * setBoolean() for set
     *
     * @param key,b Key value of Shared Prefrance
     */
    fun setPrefrance(key: String?, b: Boolean) {
        if (preference != null) {
            val editor = preference!!.edit()
            editor.putBoolean(key, b)
            editor.apply()
        }
    }

    /**
     * clearPrefra() for delete
     *
     * @param key Key value of Shared Prefrance
     */
    fun clearPref(key: String?) {
        if (preference != null) {
            val editor = preference!!.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    /**
     * getString() for use
     *
     * @param key Key value of Shared Prefrance
     * @return
     */
    fun getPref(key: String?): String? {
        return if (preference != null) {
            preference!!.getString(key, null)
        } else null
    }

    fun getLongPref(key: String?): String? {
        return if (preference != null) {
            preference!!.getLong(key, 0).toString()
        } else null
    }

    /**
     * getBoolean() for use
     *
     * @param name Key value of Shared Prefrance
     * @return
     */
    fun hasPreference(name: String?): Boolean {
        return if (preference != null) {
            preference!!.getBoolean(name, false)
        } else false
    }

    fun getBoolean(key: String?): Boolean? {
        return if (preference != null) {
            preference!!.getBoolean(key, false)
        } else null
    }

    companion object {
        private var ourInstance: SharePrefrancClass? = null
        private var context: Context? = null
        const val PREFERENCE_NAME = "MyPref"
        fun getInstance(mcontext: Context?): SharePrefrancClass? {
            context = mcontext
            if (ourInstance == null) {
                ourInstance = SharePrefrancClass()
            }
            return ourInstance
        }
    }

    init {
        preference = context!!.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
    }
}