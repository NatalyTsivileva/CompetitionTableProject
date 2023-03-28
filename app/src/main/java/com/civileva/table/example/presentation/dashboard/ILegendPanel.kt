package com.civileva.table.example.presentation.dashboard

interface ILegendPanel {
	val direction: Direction
	val legends: List<ILegend>

	enum class Direction {
		LEFT, TOP, RIGHT, BOTTOM
	}
}