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

class PenemuanAdapter(
    mainActivity: MainActivity,
    private var listTemu: ArrayList<HashMap<String, String>>
)
    : RecyclerView.Adapter<PenemuanAdapter.ViewHolder>() {

    private var context: Context = mainActivity
    private var config: Configuration = Configuration()

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.penemuan_list, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(config.URL_IMG_LOC + listTemu[position][config.TAG_TEMU_FOTO])
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.imgFbr)
        holder.txtJdl.text = listTemu[position][config.TAG_TEMU_JDL]
        holder.txtJbr.text = listTemu[position][config.TAG_TEMU_JBAR]
        holder.txtLok.text = listTemu[position][config.TAG_TEMU_LOK]
        holder.txtWkt.text = listTemu[position][config.TAG_TEMU_WKT]
    }

    override fun getItemCount(): Int {
        return listTemu.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtJdl: TextView = itemView.findViewById<View>(R.id.jdl_temu) as TextView
        var txtJbr: TextView = itemView.findViewById<View>(R.id.jbr_temu) as TextView
        var txtLok: TextView = itemView.findViewById<View>(R.id.lok_temu) as TextView
        var txtWkt: TextView = itemView.findViewById<View>(R.id.wkt_temu) as TextView
        var imgFbr: ImageView = itemView.findViewById<View>(R.id.fbr_temu) as ImageView

    }

}