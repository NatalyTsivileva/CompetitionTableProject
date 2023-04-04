package com.civileva.table.example.data

class CellInteger(
	override val index: Int,
	override val rowNumber: Int,
	override val columnNumber: Int,
	override val data: Int? = null,
	private val tableSize: Int
) : ITableCell<Int> {

	override fun isEnabledForInput() = rowNumber != columnNumber

	override fun hasValidData() = isValidData(data)

	override fun isNewRow() = !isFirst() && (index % tableSize == 0)

	override fun isFirst() = index == 0

	override fun isValidPartition(): Boolean =
		(data == null && !isEnabledForInput()) || isValidData(data)

	companion object {
		fun isValidData(data: Int?): Boolean {
			return data in 0..5
		}
	}
}