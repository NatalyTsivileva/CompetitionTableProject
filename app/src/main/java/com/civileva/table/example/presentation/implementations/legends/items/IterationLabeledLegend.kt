package com.civileva.table.example.presentation.implementations.legends.items

import com.civileva.table.example.presentation.base.legends.ILegend
import com.civileva.table.example.utils.InputUtils


data class IterationLabeledLegend(
	override val itemsCount: Int,
	val label: String,
	override val data: List<String> = (0 until itemsCount).map { "$label${it + 1}" }
) : ILegend {

	override fun updateData(data: List<*>): ILegend {
		return copy(data = InputUtils.safeStringList(data))
	}
}