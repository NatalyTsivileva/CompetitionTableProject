package com.civileva.table.example.utils

import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.Table

object TableUtils {
	fun createIntegerTable(tableSize: Int) = Table(
		size = tableSize,
		cellFactory = { index -> createCell(index, tableSize) },
		updateCell = { cell, data -> updateCell(tableSize, cell, data) },
		dataSummator = { cells -> summator(cells) }
	)


	private fun createCell(index: Int, tableSize: Int): CellInteger {
		return CellInteger(
			index = index,
			rowNumber = index / tableSize,
			columnNumber = index % tableSize,
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
				sum += it.data?:0
			}
		}
		return sum
	}
}