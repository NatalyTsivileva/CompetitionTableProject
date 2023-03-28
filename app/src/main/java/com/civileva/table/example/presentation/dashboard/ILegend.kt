package com.civileva.table.example.presentation.dashboard

interface ILegend {
	val id: Int

	companion object {
		const val LEFT_COMPETITION = 1
		const val LEFT_ITER = 2
		const val TOP_ITER = 3
		const val RIGHT_SCORE = 4
		const val RIGHT_PLACE = 5
	}
}

class LabeledLegend(val label: String, override val id: Int) : ILegend

class IterationLegend(val tableSize: Int, override val id: Int) : ILegend {
	val legendTextList = (0 until tableSize).map { it.toString() }
}

class IterationLabeledLegend(val tableSize: Int, val label: String, override val id: Int) : ILegend {
	val legendTextList = (0 until tableSize).map { "$label$it" }
}

