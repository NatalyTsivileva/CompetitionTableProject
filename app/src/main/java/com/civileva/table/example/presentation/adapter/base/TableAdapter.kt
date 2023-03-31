package com.civileva.table.example.presentation.adapter.base

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import com.civileva.table.example.data.Cursor
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.legend.base.ILegendPanel

/**
 * Адаптер для таблицы. T - тип данных в ячейках с данными, C - ячейки
 * @param table  данные для адаптера
 * @param legendsPanels  пара, где первый элемент - список с панелями легенд,
 * второй - карта вида <PanelID, ListOfViewForPanel> (список вьюх для каждой панели)
 */
open class TableAdapter<T : Comparable<T>, C : ITableCell<T>>(
	context: Context,
	attr: AttributeSet,
	private val table: Table<T, C>,
	private var legendsPanels: Pair<List<ILegendPanel>, Map<Int, List<View>>>?
) : ITableAdapter<T, C> {


	fun setupTextColor(cellIndex: Int, isFailedInput: Boolean, ed: Editable?) {
		val isNotValidScore = !table.getCell(cellIndex).hasValidData()

		val color = if (isFailedInput || isNotValidScore) Color.RED else Color.BLACK

		ed?.setSpan(
			ForegroundColorSpan(color), 0, ed.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
		)
	}


	fun aggregateRowSum(rowNumber: Int): Cursor<T, C> {
		return table.getCursor(rowNumber)
	}

	override fun getTableData(): ArrayList<C> {
		return table.getCells()
	}

	override fun getTableData(index: Int): C {
		return table.getCell(index)
	}

	override fun getTableViews(): ArrayList<View> {
		throw NotImplementedError("Override for bind table data with its views")
	}

	override fun getTableView(index: Int): View {
		throw NotImplementedError("Override for get data from bound table view")
	}

	override fun destroyTableViews() {
		throw NotImplementedError("Override for destroy table views ")
	}

	/**
	 * return size of matrix. for example for 3x3(9) return 3
	 **/
	override fun getTableSize(): Int {
		return table.size
	}


}