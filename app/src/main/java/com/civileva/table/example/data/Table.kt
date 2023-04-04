package com.civileva.table.example.data


/**
 * T- тип данных в ячейке, C - сама ячейка
 */
class Table<T : Comparable<T>, C : ITableCell<T>>(
	val size: Int,
	private val cellFactory: (index: Int) -> C,
	private val updateCell: (cell: C, data: T) -> C,
	private val dataSummator: (cells: List<C>) -> T
) {

	val elementCount = size * size

	private val cells: MutableList<C> = MutableList(elementCount, cellFactory::invoke)

	fun getCells(): ArrayList<C> = ArrayList(cells)

	fun getCell(index: Int): C = cells[index]


	fun updateCellData(cell: C, data: T) {
		cells[cell.index] = updateCell(cell, data)
	}

	fun getCursor(rowNumber: Int): Cursor<T, C> {
		val cells = mutableListOf<C>()
		var filledCells = 0

		for (columnIndex: Int in 0 until size) {
			val index = (rowNumber * size) + columnIndex
			val cell = this.cells[index]
			if (cell.isValidPartition()) filledCells++
			cells.add(cell)
		}

		return Cursor<T, C>(
			rowNumber = rowNumber,
			cellList = cells,
			isRowFullFilled = filledCells == size,
			sumator = dataSummator
		)

	}


	fun sortTableRows(sort: Sorting.Direction): List<Sorting<T, C>>? {
		val tableData = mutableListOf<Cursor<T, C>>()
		val tableSort = mutableListOf<Sorting<T, C>>()

		for (i: Int in 0 until size) {
			val cursor = getCursor(i)
			if (!cursor.isRowFullFilled) {
				return null
			} else {
				tableData.add(cursor)
			}
		}

		var cursorWithSumList: CursorsWithSum<T, C> = tableData.map { cursor ->
			Pair(cursor, cursor.dataSum)
		}

		cursorWithSumList = when (sort) {
			Sorting.Direction.ASC -> cursorWithSumList.sortedBy { it.second }
			Sorting.Direction.DESC -> cursorWithSumList.sortedByDescending { it.second }
		}

		var number = 0

		cursorWithSumList
			.groupBy { it.second }
			.map {
				val cs: CursorsWithSum<T, C> = it.value
				cs.forEach { cursorAndSum ->
					val s = Sorting(cursor = cursorAndSum.first, order = number)
					tableSort.add(s)
				}
				number++
			}
		return tableSort
	}


	companion object {
		const val UNDEFINED_SCORE = -1
	}
}
