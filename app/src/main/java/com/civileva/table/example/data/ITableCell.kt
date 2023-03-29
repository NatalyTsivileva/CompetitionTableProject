package com.civileva.table.example.data

interface ITableCell<T> {
	val index: Int
	val rowNumber: Int
	val columnNumber: Int
	val data: T?

	fun isFirst(): Boolean
	fun isNewRow(): Boolean
	fun isEnabledForInput(): Boolean
	fun hasValidData(): Boolean
	fun isValidPartition():Boolean

}
