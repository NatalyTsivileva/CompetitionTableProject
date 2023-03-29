package com.civileva.table.example.data


data class Sorting<T : Comparable<T>, C : ITableCell<T>>(
	val cursor: Cursor<T, C>,
	val order: Int
) {
	enum class Direction {
		ASC, DESC
	}
}