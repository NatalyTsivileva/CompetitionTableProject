package com.civileva.table.example.presentation.implementations.legends.items

import com.civileva.table.example.presentation.base.legends.ILegend


class IterationLabeledLegend(
	override val itemsCount: Int,
	val label: String
) : ILegend {
	override val data: List<String> = (0 until itemsCount).map { "$label${it + 1}" }
}