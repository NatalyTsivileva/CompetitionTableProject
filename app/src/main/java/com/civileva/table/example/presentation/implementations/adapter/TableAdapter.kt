package com.civileva.table.example.presentation.implementations.adapter

import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import com.civileva.table.example.data.Cursor
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.base.adapter.ITableAdapter
import com.civileva.table.example.presentation.base.holders.ITableViewHolder
import com.civileva.table.example.presentation.base.listener.ITableClickListener

/**
 * Адаптер для таблицы. T - тип данных в ячейках с данными, C - ячейки
 * @param table  данные для адаптера
 * @param legendsPanels  пара, где первый элемент - список с панелями легенд,
 * второй - карта вида <PanelID, ListOfViewForPanel> (список вьюх для каждой панели)
 */
open class TableAdapter<T : Comparable<T>, C : ITableCell<T>>(
	private val table: Table<T, C>,
	cellHoldersList: List<ITableViewHolder<T, C>>,
	listener:ITableClickListener<T,C>
) : ITableAdapter<T, C> {

	private var cellHolders: MutableList<ITableViewHolder<T, C>> = cellHoldersList.toMutableList()


	init {
			bindHolders(cellHolders,listener)
	}

	private fun bindHolders(holders: List<ITableViewHolder<T, C>>, listener: ITableClickListener<T,C>) {
		holders.forEachIndexed { index, iTableViewHolder ->
			if (index < table.size * table.size) {
				val cell = table.getCells()[index]
				iTableViewHolder.bindData(cell,listener)
			}
		}

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

	override fun getTableHolder(cell: ITableCell<T>): ITableViewHolder<T, C>? {
		val index = table.getCells().indexOf(cell)
		return if (index != -1 && cellHolders.count() > index) {
			cellHolders[index]
		} else {
			null
		}
	}

	override fun getTableHolders(): List<ITableViewHolder<T, C>> {
		return cellHolders
	}

	/**
	 * return size of matrix. for example for 3x3(9) return 3
	 **/
	override fun getTableSize(): Int {
		return table.size
	}

}