package com.civileva.table.example.presentation.implementations.holders

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.presentation.base.holders.ITableViewHolder
import com.civileva.table.example.presentation.base.listener.ITableClickListener
import com.civileva.table.example.presentation.implementations.adapter.listeners.InputCellClickListener
import com.civileva.table.example.utils.InputUtils
import com.civileva.table.test.R

class InputCellViewHolder(
	private val context: Context,
	@LayoutRes private val cellLayout: Int = R.layout.item_cell_input,
	@LayoutRes private val blockedCellLayout: Int = R.layout.item_cell_blocked
) : ITableViewHolder<Int, CellInteger> {
	private var view: View? = null

	override fun getTableView(cell: CellInteger): View {
		val tempOldView = view

		return when(tempOldView) {
			null ->  {
				when(cell.isEnabledForInput()) {
					true -> {
						val newView = View.inflate(context, cellLayout, null)
						view = newView
						newView
					}
					false -> {
						val newView = View.inflate(context, blockedCellLayout, null)
						view = newView
						newView
					}
				}
			}
			else -> {
				tempOldView
			}
		}
	}

	override fun bindData(cell: CellInteger, listener: ITableClickListener<Int, CellInteger>?) {
		val data = cell.data
		(getTableView(cell) as? AppCompatEditText)?.apply {
			data?.let { setText(it.toString())}
			addTextChangedListener {ed->
				processInput(cell, ed, listener)
				Log.d("input","bindData=${ed.toString()}")
			}
		}
	}

	private fun processInput(
		cell: CellInteger,
		ed: Editable?,
		listener: ITableClickListener<Int, CellInteger>?
	) {
		val newScore = InputUtils.safeInt(ed)

		val isFailedInput = newScore == InputUtils.FAILED_INT_INPUT
		(listener as? InputCellClickListener)?.onDataInput(cell, newScore)
		setupTextColor(newScore, isFailedInput, ed)
	}

	fun setupTextColor(data:Int, isFailedInput: Boolean, ed: Editable?) {
		val isNotValidScore = !CellInteger.isValidData(data)

		val color = if (isFailedInput || isNotValidScore) Color.RED else Color.BLACK

		ed?.setSpan(
			ForegroundColorSpan(color), 0, ed.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
		)
	}
}