package com.civileva.table.example.data

interface ITableCell {
	val index: Int
	val rowNumber: Int
	val columnNumber: Int

	fun isFirst(): Boolean
	fun isNewRow(): Boolean
	fun isEnabledForInput(): Boolean
}


interface ICompetitionTableCell : ITableCell {
	val score: Int
	fun hasValidScore(): Boolean
}