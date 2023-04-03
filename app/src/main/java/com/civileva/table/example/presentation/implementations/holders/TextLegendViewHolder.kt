package com.civileva.table.example.presentation.implementations.holders

import android.content.Context
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.civileva.table.example.presentation.base.holders.ILegendTableViewHolder
import com.civileva.table.test.R

class TextLegendViewHolder(
	private val context: Context,
	itemCount: Int,
	@LayoutRes val layoutRes:Int = R.layout.item_legend
) : ILegendTableViewHolder {

	private val views = (0 until itemCount).map { createTextView() }.toMutableList()

	override fun getPanelViews(): List<View> {
		return views
	}

	override fun bindPanelData(data: List<*>) {
		Log.d("TextLegendViewHolder", "data=${data} ${data.javaClass}, count=${data.count()},")
			data.forEachIndexed { index, any ->
				Log.d(
					"TextLegendViewHolder",
					"data[$index]=${any}, any is String = ${any is String}"
				)

				when (any) {
					is String-> views[index].text = any.toString()
				}
			}

	}

	private fun createTextView(text: String=""): AppCompatTextView {
		val view = View.inflate(context,layoutRes,null) as AppCompatTextView
		view.text = text
		return view
	}
}