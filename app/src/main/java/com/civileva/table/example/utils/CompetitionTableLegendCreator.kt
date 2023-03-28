package com.civileva.table.example.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import com.civileva.table.example.data.CompetitionTable
import com.civileva.table.example.presentation.dashboard.*
import com.civileva.table.test.R

object CompetitionTableLegendCreator {

	fun initLegendPanel(
		context: Context,
		table: CompetitionTable
	): Pair<List<ILegendPanel>, Map<Int, List<View>>> {

		val competitionLegend = IterationLabeledLegend(
			table.size,
			context.getString(R.string.label_competing),
			ILegend.LEFT_COMPETITION
		)

		val itLeftLegend = IterationLegend(table.size, ILegend.LEFT_ITER)
		val itTopLegend = IterationLegend(table.size, ILegend.TOP_ITER)
		val scoreLegend =
			LabeledLegend(context.getString(R.string.label_score), ILegend.RIGHT_SCORE)
		val placeLegend =
			LabeledLegend(context.getString(R.string.label_place), ILegend.RIGHT_PLACE)

		val legendViewsMap = mutableMapOf<Int, List<View>>()
		legendViewsMap[competitionLegend.id] = createLegendView(context,competitionLegend)
		legendViewsMap[itLeftLegend.id] = createLegendView(context,itLeftLegend)
		legendViewsMap[itTopLegend.id] = createLegendView(context,itTopLegend)
		legendViewsMap[scoreLegend.id] = createLegendView(context,scoreLegend)
		legendViewsMap[placeLegend.id] = createLegendView(context,placeLegend)

		val legendsPanelList = mutableListOf<ILegendPanel>()

		val leftPanel = object : ILegendPanel {
			override val direction: ILegendPanel.Direction = ILegendPanel.Direction.LEFT
			override val legends: List<ILegend> = listOf(competitionLegend, itLeftLegend)
		}
		legendsPanelList.add(leftPanel)


		val topPanel = object : ILegendPanel {
			override val direction: ILegendPanel.Direction = ILegendPanel.Direction.TOP
			override val legends: List<ILegend> = listOf(itTopLegend)
		}
		legendsPanelList.add(topPanel)


		val rightPanel = object : ILegendPanel {
			override val direction: ILegendPanel.Direction = ILegendPanel.Direction.RIGHT
			override val legends: List<ILegend> = listOf(scoreLegend, placeLegend)
		}
		legendsPanelList.add(rightPanel)

		return Pair(legendsPanelList, legendViewsMap)
	}

	private fun createLegendView(context:Context, legend: ILegend): List<View> {
		val list = mutableListOf<View>()
		when (legend) {
			is LabeledLegend -> {
				list.add(invokeLegendTextView(context,legend.label))
			}

			is IterationLegend -> {
				val legendList = legend.legendTextList.map { invokeLegendTextView(context,it) }
				list.addAll(legendList)
			}

			is IterationLabeledLegend -> {
				val legendList = legend.legendTextList.map { invokeLegendTextView(context,it) }
				list.addAll(legendList)
			}
		}
		return list
	}

	private fun invokeLegendTextView(context: Context, text: String) = TextView(context).apply { setText(text) }
}