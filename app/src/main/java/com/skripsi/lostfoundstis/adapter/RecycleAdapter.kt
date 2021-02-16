package com.skripsi.lostfoundstis.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skripsi.lostfoundstis.util.Configuration
import com.skripsi.lostfoundstis.R
import java.util.*

class RecycleAdapter(
    mainActivity: Context,
    private var touchItem: ArrayList<HashMap<String, String>>
)
    : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {

    private var context: Context = mainActivity
    private var config: Configuration = Configuration()

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycle_holder, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (touchItem[position].containsKey(config.TAG_CARI_ID)){
            Glide.with(context)
                .load(config.URL_IMG_LOC_CARI + touchItem[position][config.TAG_CARI_FOTO])
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgFbr)
            holder.txtJdl.text = touchItem[position][config.TAG_CARI_JDL]
            holder.txtJbr.text = touchItem[position][config.TAG_CARI_JBAR]
            holder.txtLok.text = touchItem[position][config.TAG_CARI_LOK]
            holder.txtWkt.text = touchItem[position][config.TAG_CARI_WKT]
        }
        else if (touchItem[position].containsKey(config.TAG_TEMU_ID)){
            Glide.with(context)
                .load(config.URL_IMG_LOC_TEMU + touchItem[position][config.TAG_TEMU_FOTO])
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgFbr)
            holder.txtJdl.text = touchItem[position][config.TAG_TEMU_JDL]
            holder.txtJbr.text = touchItem[position][config.TAG_TEMU_JBAR]
            holder.txtLok.text = touchItem[position][config.TAG_TEMU_LOK]
            holder.txtWkt.text = touchItem[position][config.TAG_TEMU_WKT]
        }
    }

    override fun getItemCount(): Int {
        return touchItem.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtJdl: TextView = itemView.findViewById<View>(R.id.jdl_holder) as TextView
        var txtJbr: TextView = itemView.findViewById<View>(R.id.jbr_holder) as TextView
        var txtLok: TextView = itemView.findViewById<View>(R.id.lok_holder) as TextView
        var txtWkt: TextView = itemView.findViewById<View>(R.id.wkt_holder) as TextView
        var imgFbr: ImageView = itemView.findViewById<View>(R.id.fbr_holder) as ImageView
    }

}