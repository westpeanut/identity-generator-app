package com.daee.dummies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.daee.dummies.api.Sampling
import kotlinx.android.synthetic.main.custom_diag_layout.*
import kotlinx.android.synthetic.main.item_list.view.*

class SubsDialog(private val sampling: Sampling): DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return (inflater.inflate(R.layout.custom_diag_layout, container, false))
    }

    override fun onStart() {
        super.onStart()
        val width: Int = (resources.displayMetrics.widthPixels * 6)/7
        val height: Int = (resources.displayMetrics.heightPixels * 4)/5
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        Glide.with(this)
                .load(sampling.image_url)
                .override(300)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(iv_avatar_diag)

        tv_company_diag.text = sampling.company
        tv_name_diag.text = sampling.name
        tv_address_diag.text =sampling.address
        tv_age_diag.text = "${sampling.age} yrs old"

    }

}