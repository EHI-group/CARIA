package com.mateocvas.caria.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mateocvas.caria.R

class AdapterSimpleItem(val arrayList: ArrayList<String>,val context: Context):BaseAdapter() {

        override fun getItem(p0: Int): Any {
            return arrayList[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return arrayList.size
        }

    private val mInflator: LayoutInflater

    init {
        this.mInflator = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val vh: ListRowHolder
        if (convertView == null) {
            view = this.mInflator.inflate(R.layout.item_simple, parent, false)
            vh = ListRowHolder(view)
            view!!.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.label.text = arrayList[position]
        return view
    }
    inner class ListRowHolder(row: View?) {
         val label: TextView

        init {
            this.label = row?.findViewById(R.id.isimple_tv_city) as TextView
        }
    }
    }

