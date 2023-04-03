package com.civileva.table.example.utils

import android.content.Context
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.implementations.holders.TextLegendViewHolder
import com.civileva.table.example.presentation.implementations.legends.items.IterationLabeledLegend
import com.civileva.table.example.presentation.implementations.legends.items.IterationLegend
import com.civileva.table.example.presentation.implementations.legends.items.LabeledListLegend
import com.civileva.table.example.presentation.implementations.legends.panels.*
import com.civileva.table.test.R

object TableAdapterUtils {

	fun createLegendsHolderMap(
		context: Context,
		table: Table<Int, CellInteger>
	): Map<ILegendPanel, TextLegendViewHolder> {

		val map = mutableMapOf<ILegendPanel, TextLegendViewHolder>()

		//Left CompetitorN
		val competitionLegend =
			IterationLabeledLegend(table.size, context.getString(R.string.label_competing))
		val competitionPanel = LeftCompetitionLegendPanel(competitionLegend)
		map[competitionPanel] = TextLegendViewHolder(context, competitionLegend.itemsCount)


		//Left Iterator
		val itLeftLegend = IterationLegend(table.size)
		val itLeftPanel = LeftIterationLegendPanel(itLeftLegend)
		map[itLeftPanel] = TextLegendViewHolder(context, itLeftLegend.itemsCount)


		//Top Iterator
		val itTopLegend = IterationLegend(table.size)
		val itTopPanel = TopIterationLegendPanel(itTopLegend)
		map[itTopPanel] = TextLegendViewHolder(context, itTopLegend.itemsCount)


		//Right Score
		val scoreHeader = context.getString(R.string.label_score)
		var data = (0 until table.size + 1).map { if (it == 0) scoreHeader else "?" }
		val scoreLegend = LabeledListLegend(data)
		val scorePanel = RightScoreLegendPanel(scoreLegend)
		map[scorePanel] = TextLegendViewHolder(context, scoreLegend.itemsCount)
		//Right Place

		val placeHeader = context.getString(R.string.label_place)
		data = (0 until table.size + 1).map { if (it == 0) placeHeader else "?" }
		val placeLegend = LabeledListLegend(data)
		val placePanel = RightPlaceLegendPanel(placeLegend)
		map[placePanel] = TextLegendViewHolder(context, placeLegend.itemsCount)


		val test = "test"
		val testLegend = IterationLabeledLegend(table.size, test)
		val testPanel = TestIterationLegendPanel(testLegend)
		map[testPanel] = TextLegendViewHolder(context, testLegend.itemsCount)


		return map
	}
}

