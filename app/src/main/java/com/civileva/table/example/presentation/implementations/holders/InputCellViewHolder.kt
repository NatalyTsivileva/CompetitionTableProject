package com.civileva.table.example.presentation.implementations.holders

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatEditText
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.presentation.base.holders.ITableViewHolder
import com.civileva.table.test.R
import com.google.android.material.textfield.TextInputLayout

class InputCellViewHolder(
	private val context: Context,
	@LayoutRes private val layout: Int = R.layout.item_cell_input
) : ITableViewHolder<Int, CellInteger> {
	private var view: View? = null

	override fun getTableView(): View {
		val newView = if (view == null) {
			View.inflate(context, layout, null)
		} else {
			view!!
		}
		view = newView
		return newView
	}

	override fun bindData(cell: CellInteger) {
		val data = cell.data
		data?.let {
			(view as? AppCompatEditText)?.setText(it.toString())
		}
	}
}