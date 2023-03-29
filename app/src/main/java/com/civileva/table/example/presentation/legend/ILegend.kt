package com.civileva.table.example.presentation.legend

interface ILegend {
	val itemsCount: Int
}

/*Легенда вида "Заголовок и ячейки"(например, очки и места)*/
class LabeledListLegend(override val itemsCount: Int, val label: String) : ILegend


class IterationLegend(override val itemsCount: Int) : ILegend {
	val legendTextList = (0 until itemsCount).map { (it+1).toString() }
}

class IterationLabeledLegend(override val itemsCount: Int, val label: String) : ILegend {
	val legendTextList = (0 until itemsCount).map { "$label${it+1}" }
}

