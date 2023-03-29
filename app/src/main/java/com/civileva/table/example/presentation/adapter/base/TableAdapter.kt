package com.civileva.table.example.presentation.adapter.base

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.civileva.table.example.data.Cursor
import com.civileva.table.example.data.Table
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.presentation.legend.ILegendPanel

/**
 * Адаптер для таблицы. T - тип данных в ячейках с данными, C - ячейки
 * @param table  данные для адаптера
 * @param legendsPanels  пара, где первый элемент - список с панелями легенд,
 * второй - карта вида <PanelID, ListOfViewForPanel> (список вьюх для каждой панели)
 */
abstract class TableAdapter<T:Comparable<T>, C:ITableCell<T>>(
	context: Context,
	attr: AttributeSet,
	private val table: Table<T,C>,
	private var legendsPanels: Pair<List<ILegendPanel>, Map<Int, List<View>>>?
) : ITableAdapter<T, C>, ILegendAdapter {


	fun setupTextColor(cellIndex: Int, isFailedInput: Boolean, ed: Editable?) {
		val isNotValidScore = !table.getCell(cellIndex).hasValidData()

		val color = if (isFailedInput || isNotValidScore) Color.RED else Color.BLACK

		ed?.setSpan(
			ForegroundColorSpan(color), 0, ed.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
		)
	}


	fun aggregateRowSum(rowNumber: Int): Cursor<T,C> {
		return table.getCursor(rowNumber)
	}

	override fun getTableData(): ArrayList<C> {
		return table.getCells()
	}

	override fun getTableData(index: Int): C {
		return table.getCell(index)
	}

	/**
	 * return size of matrix. for example for 3x3(9) return 3
	 **/
	override fun getTableSize(): Int {
		return table.size
	}


	override fun getLegendPanels(): List<ILegendPanel> {
		return legendsPanels?.first?: emptyList()
	}

	override fun getLegendPanels(direction: ILegendPanel.Direction): List<ILegendPanel> {
		return legendsPanels?.first?.filter { it.direction == direction }?: emptyList()
	}

	override fun getLegendViews(panelId: Int): List<View> {
		return legendsPanels?.second?.get(panelId)?:emptyList()
	}


	override fun findLegendPanel(legendClass: Class<*>): ILegendPanel? {
		return legendsPanels?.first?.find { it.legend.javaClass == legendClass }
	}

	override fun destroyLegendViews() {
		legendsPanels = null
	}
}