package com.civileva.table.example.utils

import android.content.Context
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.implementations.holders.InputCellViewHolder

object TableUtils {
	fun createIntegerTable(tableSize: Int) = Table(
		size = tableSize,
		cellFactory = { index -> createCell(index, tableSize) },
		updateCell = { cell, data -> updateCell(tableSize, cell, data) },
		dataSummator = { cells -> summator(cells) }
	)


	private fun createCell(index: Int, tableSize: Int): CellInteger {
		val rowN = if (tableSize > 0) index / tableSize else 0
		val colN = if (tableSize > 0) index % tableSize else 0
		return CellInteger(
			index = index,
			rowNumber = rowN,
			columnNumber = colN,
			tableSize = tableSize
		)
	}

	private fun updateCell(tableSize: Int, cellInteger: CellInteger, data: Int): CellInteger {
		return CellInteger(
			index = cellInteger.index,
			rowNumber = cellInteger.rowNumber,
			columnNumber = cellInteger.columnNumber,
			data = data,
			tableSize = tableSize
		)
	}

	private fun summator(cells: List<CellInteger>): Int {
		var sum = 0
		cells.forEach {
			if (it.hasValidData() && it.isEnabledForInput()) {
				sum += it.data ?: 0
			}
		}
		return sum
	}


	fun  createCellHolders(
		table: Table<Int, CellInteger>,
		context: Context
	) = (0 until table.size * table.size).map {
		InputCellViewHolder(context)
	}

}