package com.civileva.table.example.data

data class Cursor<T : Comparable<T>, C : ITableCell<T>>(
	val rowNumber: Int,
	val cellList: List<C>,
	val isRowFullFilled: Boolean,
	val sumator: (items: List<C>) -> T
) {
	val dataSum = sumator(cellList)
}