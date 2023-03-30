package com.civileva.table.example.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.Sorting
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.adapter.base.TableAdapter
import com.civileva.table.example.presentation.legend.base.ILegendPanel
import com.civileva.table.example.utils.InputUtils

class CompetitionTableAdapter(
	private val context: Context,
	private val attr: AttributeSet,
	private val table: Table<Int, CellInteger>,
	legendsPanels: Pair<List<ILegendPanel>, Map<Int, List<View>>>
) : TableAdapter<Int, CellInteger>(context, attr, table, legendsPanels) {

	private val tableViews: ArrayList<View> = ArrayList(table.elementCount)

	init {
		(0 until table.elementCount).forEach { index ->
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

	private fun processInput(cell: CellInteger, ed: Editable?) {
		val newScore = InputUtils.safeInt(ed)

		val isFailedInput = newScore == InputUtils.FAILED_INT_INPUT

		if (!isFailedInput) {
			table.updateCellData(cell, newScore)
			updateScoreLegend(cell)
			updateRewards()
		}

		setupTextColor(cell.index, isFailedInput, ed)
	}

	private fun updateScoreLegend(cell: CellInteger) {
		val cursor = aggregateRowSum(cell.rowNumber)
		val legendViews = getLegendViews(ILegendPanel.RIGHT_SCORE)
		val view = legendViews[cell.rowNumber + 1]

		val legendText = if (cursor.isRowFullFilled) {
			cursor.dataSum.toString()
		}else{
			""
		}

		(view as? AppCompatTextView)?.apply {
			text = legendText
			requestLayout()
			invalidate()
		}
	}


	fun updateRewards() {
		val legendViews = getLegendViews(ILegendPanel.RIGHT_PLACE)

		val rewards = table.sortTableRows(Sorting.Direction.DESC)

		rewards?.forEach {
			val index = it.cursor.rowNumber + 1
			val view = legendViews[index]

			(view as? AppCompatTextView)?.apply {
				val place = (it.order + 1).toString()
				text = place
				requestLayout()
				invalidate()
			}
		}
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

	override fun destroyTableViews() {
		tableViews.clear()
	}


}