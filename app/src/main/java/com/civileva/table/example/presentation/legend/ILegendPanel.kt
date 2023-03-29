package com.civileva.table.example.presentation.legend

interface ILegendPanel {
	val id: Int
	val direction: Direction
	val legend: ILegend


	enum class Direction {
		LEFT, TOP, RIGHT, BOTTOM
	}

	companion object {
		const val LEFT_COMPETITION = 1
		const val LEFT_ITER = 2
		const val TOP_ITER = 3
		const val RIGHT_SCORE = 4
		const val RIGHT_PLACE = 5
	}

}