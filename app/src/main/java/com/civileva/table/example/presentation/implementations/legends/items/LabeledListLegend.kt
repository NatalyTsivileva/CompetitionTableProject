package com.civileva.table.example.presentation.implementations.legends.items

import com.civileva.table.example.presentation.base.legends.ILegend

class LabeledListLegend(
	override val data: List<String>
) : ILegend {

	override val itemsCount: Int = data.count()
}


