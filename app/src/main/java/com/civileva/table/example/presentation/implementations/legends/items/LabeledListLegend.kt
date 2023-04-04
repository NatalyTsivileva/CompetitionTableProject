package com.civileva.table.example.presentation.implementations.legends.items

import com.civileva.table.example.presentation.base.legends.ILegend
import com.civileva.table.example.utils.InputUtils

data class LabeledListLegend(
	override val data: List<String>,
	override val itemsCount: Int = data.count()
) : ILegend{
	override fun updateData(data: List<*>): ILegend {
		return copy(data = InputUtils.safeStringList(data))
	}
}


