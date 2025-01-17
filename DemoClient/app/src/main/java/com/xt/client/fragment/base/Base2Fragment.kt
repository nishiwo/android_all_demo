package com.xt.client.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xt.client.R

abstract class Base2Fragment : Fragment() {

    companion object {
        const val TITLE = "title"
    }

    lateinit var mContent: RecyclerView;
    lateinit var mTitle: TextView;
    lateinit var mResult: TextView;
    private var mAdapter = GridAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base2_layout, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTitle = view.findViewById(R.id.title_text)
        mContent = view.findViewById(R.id.content)
        mResult = view.findViewById(R.id.result_text)
        mContent.layoutManager =
            GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)
        mContent.adapter = mAdapter
        arguments?.let {
            mTitle.text = it.getString(BaseFragment.TITLE, "title")
        }
        mAdapter.dataList = getShowData()
    }

    abstract fun clickItem(position: Int)

    abstract fun getShowData(): List<String>

    fun showResult(str: String) {
        mResult.text = str
    }

    internal inner class GridAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var dataList: List<String>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflate = View.inflate(parent.context, R.layout.base2_item, null)
            return object : RecyclerView.ViewHolder(inflate) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.text).text = dataList?.get(position)
            holder.itemView.findViewById<TextView>(R.id.text).setOnClickListener {
                clickItem(position)
            }
        }

        override fun getItemCount(): Int {
            return dataList?.size ?: 0
        }
    }

}