package com.civileva.table.example.presentation

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.civileva.table.example.data.CompetitionTable
import com.civileva.table.example.data.ICompetitionTableCell
import com.civileva.table.example.utils.InputUtils


class CompetitionTableAdapter(
	private val table: CompetitionTable,
	private val context: Context,
	private val attr: AttributeSet
) : ITableAdapter<ICompetitionTableCell> {

	private val tableViews: Array<View> = Array(table.size * table.size, ::initialView)


	private fun initialView(cellIndex: Int): View {
		val cell = table.cells[cellIndex]
		return if (cell.isEnabledForInput()) {
			createInputView(cell)
		} else
			createPlaceholderView()
	}


	private fun createInputView(data: ICompetitionTableCell): AppCompatEditText {
		val view = AppCompatEditText(context, attr)
		view.hint = "X"
		view.isFocusableInTouchMode = true

		view.addTextChangedListener {
			processInput(data, it)
		}

		return view
	}

	private fun processInput(
		data: ICompetitionTableCell,
		ed: Editable?,
	) {
		val newScore = InputUtils.safeInt(ed)
		val isFailedInput = newScore == InputUtils.FAILED_INT_INPUT

		if (!isFailedInput) {
			table.updateScore(data, newScore)
			aggregateRowSim(data.rowNumber)
			checkRewards()
		}

		setupTextColor(data.index, isFailedInput, ed)
	}


	private fun setupTextColor(cellIndex: Int, isFailedInput: Boolean, ed: Editable?) {
		val isNotValidScore = !table.cells[cellIndex].hasValidScore()

		val color = if (isFailedInput || isNotValidScore) Color.RED else Color.BLACK

		ed?.setSpan(
			ForegroundColorSpan(color), 0, ed.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
		)
	}


	private fun aggregateRowSim(rowNumber: Int) {
		val aggregator = table.aggregateRow(rowNumber)
		if (aggregator.isRowFullFilled) {
			Toast.makeText(context, "Сумма=${aggregator.rowSum}", Toast.LENGTH_SHORT).show()
		}
	}

	private fun checkRewards() {
		val rewards = table.checkRewards()
		var text = "МЕСТА:\n"

		if (rewards != null) {
			rewards.forEach {
				text += " N ряда=${it.aggr.rowNumber}, Место=${it.place}, Количество очков=${it.aggr.rowSum}\n"
			}
			Log.d("REWARDS", text)
			Toast.makeText(context, text, Toast.LENGTH_LONG).show()
		}
	}


	private fun createPlaceholderView(): View {
		return View(context, attr).apply { setBackgroundColor(Color.BLACK) }
	}

	override fun getViews(): Array<View> {
		return tableViews
	}

	override fun getView(index: Int): View {
		return tableViews[index]
	}


	override fun getData(): Array<ICompetitionTableCell> {
		return table.cells
	}

	override fun getData(index: Int): ICompetitionTableCell {
		return table.cells[index]
	}

	/**
	 * return size of matrix. for example for 3x3(9) return 3
	 **/
	override fun getTableSize(): Int {
		return table.size
	}

}