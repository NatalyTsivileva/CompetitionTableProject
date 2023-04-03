package com.civileva.table.example.presentation.implementations.legends.items

import com.civileva.table.example.presentation.base.legends.ILegend


class IterationLegend(
	override val itemsCount: Int
) : ILegend {
	override val data: List<String> = (0 until itemsCount).map { (it + 1).toString() }
}