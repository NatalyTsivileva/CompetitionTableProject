package com.civileva.table.example.presentation.base.legends

interface ILegendPanel {
	val id: Int
	val direction: Direction
	val legend: ILegend
	val panelSize: Size
	fun updateSize(size: Size): ILegendPanel

	data class Size(val width: Int, val height: Int) {
		fun isUndefined() = width == UNDEFINED_SIZE || height == UNDEFINED_SIZE

		companion object {
			const val UNDEFINED_SIZE = 0
			fun Undefined() = Size(UNDEFINED_SIZE, UNDEFINED_SIZE)
		}
	}


	enum class Direction {
		LEFT, TOP, RIGHT, BOTTOM
	}

	companion object {
		const val LEFT_COMPETITION = 1
		const val LEFT_ITER = 2
		const val TOP_ITER = 3
		const val RIGHT_SCORE = 4
		const val RIGHT_PLACE = 5
		const val TEST = 6
	}

}