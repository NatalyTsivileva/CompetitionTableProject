package com.civileva.table.example.presentation.implementations.holders

import android.content.Context
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.civileva.table.example.presentation.base.holders.ILegendTableViewHolder
import com.civileva.table.test.R

open class TextLegendViewHolder(
	private val context: Context,
	itemCount: Int,
	@LayoutRes val layoutRes: Int = R.layout.item_legend
) : ILegendTableViewHolder {

	private val views = (0 until itemCount).map { createTextView() }.toMutableList()

	override fun getPanelViews(): List<View> {
		return views
	}

	override fun destroyView() {
		views.clear()
	}

	override fun bindPanelData(data: List<*>) {
		for (i: Int in 0 until data.count()) {
			val itemData = data[i]
			updateView(itemData,views[i])
		}
	}

	open fun updateView(data: Any?, view: AppCompatTextView): Boolean {
		val parsed = when (data) {
			is String -> {
				view.text = data.toString()
				true
			}
			else -> {
				Log.e("bindPanelData", "Cant update view. Data=${data}")
				false
			}
		}
		return parsed
	}

	private fun createTextView(text: String = ""): AppCompatTextView {
		val view = View.inflate(context, layoutRes, null) as AppCompatTextView
		view.text = text
		return view
	}
}