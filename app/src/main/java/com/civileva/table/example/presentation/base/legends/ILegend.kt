package com.civileva.table.example.presentation.base.legends

interface ILegend {
	val itemsCount: Int
	val data:List<*>
	fun updateData(data: List<*>):ILegend
}
