package com.civileva.table.example.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.Sorting
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.adapter.base.TableAdapter
import com.civileva.table.example.presentation.legend.ILegendPanel
import com.civileva.table.example.utils.InputUtils

class CompetitionTableAdapter(
	private val context: Context,
	private val attr: AttributeSet,
	private val table: Table<Int, CellInteger>,
	legendsPanels: Pair<List<ILegendPanel>, Map<Int, List<View>>>
) : TableAdapter<Int, CellInteger>(context, attr, table, legendsPanels) {

	private val tableViews: ArrayList<View> = ArrayList(table.elementCount)

	init {
		 (0 until table.elementCount).forEach {index->
			 tableViews.add(createView(index))
		 }
	}


	private fun createView(cellIndex: Int): View {
		val cell = table.getCell(cellIndex)
		return if (cell.isEnabledForInput()) {
			createInputView(cell)
		} else
			createPlaceholderView()
	}


	private fun createInputView(cellInteger: CellInteger): AppCompatEditText {
		val view = AppCompatEditText(context, attr)
		view.hint = "X"
		view.isFocusableInTouchMode = true
		view.gravity = Gravity.CENTER

		view.addTextChangedListener {
			processInput(cellInteger, it)
		}

		return view
	}

	private fun processInput(cellInteger: CellInteger, ed: Editable?) {
		val newScore = InputUtils.safeInt(ed)

		val isFailedInput = newScore == InputUtils.FAILED_INT_INPUT

		if (!isFailedInput) {
			table.updateCellData(cellInteger, newScore)
			aggregateRowSum(cellInteger.rowNumber)
			checkRewards()
		}

		setupTextColor(cellInteger.index, isFailedInput, ed)
	}

	fun checkRewards(): List<Sorting<Int, CellInteger>>? {
		val rewards = table.sortTableRows(Sorting.Direction.DESC)
		var text = "МЕСТА:\n"

		if (rewards != null) {
			rewards.forEach {
				text += " N ряда=${it.cursor.rowNumber}, Место=${it.order}, Количество очков=${it.cursor.dataSum}\n"
			}
			Log.d("REWARDS", text)
			Toast.makeText(context, text, Toast.LENGTH_LONG).show()
		}
		return rewards
	}

	private fun createPlaceholderView(): View {
		return View(context, attr).apply { setBackgroundColor(Color.BLACK) }
	}

	override fun getTableViews(): ArrayList<View> {
		return tableViews
	}

	override fun getTableView(index: Int): View {
		return tableViews[index]
	}
}