package com.daee.dummies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.daee.dummies.api.Sampling
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter(private val items:MutableList<Sampling>): RecyclerView.Adapter<ListAdapter.ViewHolder>() {


    private lateinit var dialog: SubsDialog

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        val holder = ViewHolder(view);
        return holder;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curSub = items[position]
        holder.itemView.apply {
            tv_name.text = curSub.name
            tv_address.text = curSub.address
            Glide.with(context)
                    .load(curSub.image_url)
                    .override(160)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(iv_avatar)

            lin_plane.setOnClickListener {
                val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                dialog = SubsDialog(curSub)
                dialog.show(ft,"SubsDialog")
            }
            lin_plane.setOnLongClickListener(object : View.OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    removeSubs(position)
                    return true
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addSubs(subSamples:Sampling){
        val index = items.size -1
        items.add(subSamples)
        notifyItemInserted(index)
    }

    fun removeSubs(index:Int){
        items.removeAt(index)
        notifyDataSetChanged()
    }

}