package com.civileva.table.example.presentation.implementations.legends.items

import com.civileva.table.example.presentation.base.legends.ILegend
import com.civileva.table.example.utils.InputUtils


data class IterationLegend(
	override val itemsCount: Int,
	override val data: List<String> = (0 until itemsCount).map { (it + 1).toString() }
) : ILegend {

	override fun updateData(data: List<*>): ILegend {
		return copy(data = InputUtils.safeStringList(data))
	}
}