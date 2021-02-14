package com.skripsi.lostfoundstis

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*

class PencarianAdapter(
    mainActivity: MainActivity,
    private var listCari: ArrayList<HashMap<String, String>>
)
    : RecyclerView.Adapter<PencarianAdapter.ViewHolder>() {

    private var context: Context = mainActivity
    private var config: Configuration = Configuration()

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.pencarian_list, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(config.URL_IMG_LOC + listCari[position][config.TAG_CARI_FOTO])
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.imgFbr)
        holder.txtJdl.text = listCari[position][config.TAG_CARI_JDL]
        holder.txtJbr.text = listCari[position][config.TAG_CARI_JBAR]
        holder.txtLok.text = listCari[position][config.TAG_CARI_LOK]
        holder.txtWkt.text = listCari[position][config.TAG_CARI_WKT]
    }

    override fun getItemCount(): Int {
        return listCari.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtJdl: TextView = itemView.findViewById<View>(R.id.jdl_cari) as TextView
        var txtJbr: TextView = itemView.findViewById<View>(R.id.jbr_cari) as TextView
        var txtLok: TextView = itemView.findViewById<View>(R.id.lok_cari) as TextView
        var txtWkt: TextView = itemView.findViewById<View>(R.id.wkt_cari) as TextView
        var imgFbr: ImageView = itemView.findViewById<View>(R.id.fbr_cari) as ImageView
    }

}