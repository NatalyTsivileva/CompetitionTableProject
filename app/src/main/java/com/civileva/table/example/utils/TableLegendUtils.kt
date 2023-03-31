package com.civileva.table.example.utils

import android.content.Context
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.legend.*
import com.civileva.table.example.presentation.legend.base.*
import com.civileva.table.test.R

object TableLegendUtils {

	fun createCompetitionLegendPanel(
		context: Context,
		table: Table<Int, CellInteger>
	): Pair<List<ILegendPanel>, Map<Int, List<View>>> {

		val panelList = mutableListOf<ILegendPanel>()
		val views = mutableMapOf<Int, List<View>>()

		val paddingsM1 = context.resources.getDimension(R.dimen.m1).toInt()
		val paddingsM15 = context.resources.getDimension(R.dimen.m15).toInt()

		//Left CompetitorN
		val competitionLegend =	IterationLabeledLegend(table.size, context.getString(R.string.label_competing))
		val competitionPanel = LeftCompetitionLegendPanel(competitionLegend)
		panelList.add(competitionPanel)
		views[competitionPanel.id] = createLegendView(context, competitionLegend, null)


		//Left Iterator
		val itLeftLegend = IterationLegend(table.size)
		val itLeftPanel = LeftIterationLegendPanel(itLeftLegend)
		panelList.add(itLeftPanel)
		views[itLeftPanel.id] =	createLegendView(context, itLeftLegend, null)


		// Top Iterator
		val itTopLegend = IterationLegend(table.size)
		val itTopPanel = TopIterationLegendPanel(itTopLegend)
		panelList.add(itTopPanel)
		views[itTopPanel.id] = createLegendView(context, itTopLegend, null)


		//Right Score
		val scoreLegend = LabeledListLegend(table.size, context.getString(R.string.label_score))
		val scorePanel = RightScoreLegendPanel(scoreLegend)
		panelList.add(scorePanel)
		var viewsList = createLegendView(context,scoreLegend, null)
		views[scorePanel.id] = viewsList

		//Right Place
		val placeLegend = LabeledListLegend(table.size, context.getString(R.string.label_place))
		val placePanel = RightPlaceLegendPanel(placeLegend)
		panelList.add(placePanel)

		viewsList = createLegendView(context, placeLegend, null)
		views[placePanel.id] =  viewsList





		val testLegend = IterationLabeledLegend(table.size,context.getString(R.string.label_apponent))
		val itTestPanel = TestIterationLegendPanel(testLegend)
		panelList.add(itTestPanel)
		views[itTestPanel.id] = createLegendView(context, testLegend, null)




		return Pair(panelList, views)
	}

	private fun createLegendView(
		context: Context,
		legend: ILegend,
		paddings: Rect?
	): List<View> {

		val list = mutableListOf<View>()
		when (legend) {
			is LabeledListLegend -> {
				(0 until legend.itemsCount + 1).forEach {
					val text = if (it == 0) legend.label else "?"
					list.add(invokeLegendTextView(context, text, paddings))
				}
			}

			is IterationLegend -> {
				val legendList =
					legend.legendTextList.map { invokeLegendTextView(context, it, paddings) }
				list.addAll(legendList)
			}

			is IterationLabeledLegend -> {
				val legendList =
					legend.legendTextList.map { invokeLegendTextView(context, it, paddings) }
				list.addAll(legendList)
			}
		}
		return list
	}

	private fun invokeLegendTextView(context: Context, text: String, paddings: Rect? = null) =
		AppCompatTextView(context).apply {
			setText(text)
			gravity = Gravity.CENTER
			paddings?.let { newPadding(it) }
			setTextColor(context.getColor(R.color.teal_700))
			//setBackgroundColor(Color.GRAY)
			 setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.border))
		}

	private fun View.newPadding(paddings: Rect) {
		updatePadding(
			left = paddings.left,
			top = paddings.top,
			right = paddings.right,
			bottom = paddings.bottom
		)
	}
}

