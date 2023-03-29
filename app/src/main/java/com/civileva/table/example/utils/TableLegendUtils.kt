package com.civileva.table.example.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.legend.*
import com.civileva.table.test.R

object TableLegendUtils {

	fun createCompetitionLegendPanel(
		context: Context,
		table: Table<Int, CellInteger>
	): Pair< List<ILegendPanel>, Map<Int, List<View>>> {

		val panelList = mutableListOf<ILegendPanel>()
		val views = mutableMapOf<Int, List<View>>()

		//Left CompetitorN
		val competitionLegend =
			IterationLabeledLegend(table.size, context.getString(R.string.label_competing))

		val competitionPanel = object : ILegendPanel {
			override val id: Int = ILegendPanel.LEFT_COMPETITION
			override val direction: ILegendPanel.Direction = ILegendPanel.Direction.LEFT
			override val legend: ILegend = competitionLegend
		}
		panelList.add(competitionPanel)
		views[competitionPanel.id] = createLegendView(context, competitionLegend)


		//Left Iterator
		val itLeftLegend = IterationLegend(table.size)
		val itLeftPanel = object : ILegendPanel {
			override val id: Int = ILegendPanel.LEFT_ITER
			override val direction: ILegendPanel.Direction = ILegendPanel.Direction.LEFT
			override val legend: ILegend = itLeftLegend
		}
		panelList.add(itLeftPanel)
		views[itLeftPanel.id] = createLegendView(context, itLeftLegend)


		// Top Iterator
		val itTopLegend = IterationLegend(table.size)
		val itTopPanel = object : ILegendPanel {
			override val id: Int = ILegendPanel.TOP_ITER
			override val direction: ILegendPanel.Direction = ILegendPanel.Direction.TOP
			override val legend: ILegend = itTopLegend
		}
		panelList.add(itTopPanel)
		views[itTopPanel.id] = createLegendView(context, itTopLegend)

		//Right Score
		val scoreLegend = LabeledListLegend(table.size, context.getString(R.string.label_score))
		val scorePanel = object : ILegendPanel {
			override val id: Int = ILegendPanel.RIGHT_SCORE
			override val direction: ILegendPanel.Direction = ILegendPanel.Direction.RIGHT
			override val legend: ILegend = scoreLegend
		}
		panelList.add(scorePanel)
		views[scorePanel.id] = createLegendView(context, scoreLegend)

		//Right Place
		val placeLegend = LabeledListLegend(table.size, context.getString(R.string.label_place))
		val placePanel = object : ILegendPanel {
			override val id: Int = ILegendPanel.RIGHT_PLACE
			override val direction: ILegendPanel.Direction = ILegendPanel.Direction.RIGHT
			override val legend: ILegend = placeLegend
		}
		panelList.add(placePanel)
		views[placePanel.id] = createLegendView(context, placeLegend)

		return Pair(panelList, views)
	}

	private fun createLegendView(
		context: Context,
		legend: ILegend
	): List<View> {

		val list = mutableListOf<View>()
		when (legend) {
			is LabeledListLegend -> {
				(0 until legend.itemsCount + 1).forEach {
					val text = if (it == 0) legend.label else "?"
					list.add(invokeLegendTextView(context, text))
				}
			}

			is IterationLegend -> {
				val legendList = legend.legendTextList.map { invokeLegendTextView(context, it) }
				list.addAll(legendList)
			}

			is IterationLabeledLegend -> {
				val legendList = legend.legendTextList.map { invokeLegendTextView(context, it) }
				list.addAll(legendList)
			}
		}
		return list
	}

	private fun invokeLegendTextView(context: Context, text: String) =
		TextView(context).apply { setText(text) }
}