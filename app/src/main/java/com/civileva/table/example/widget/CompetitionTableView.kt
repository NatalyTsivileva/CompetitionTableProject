package com.civileva.table.example.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.presentation.adapter.base.ILegendPanelAdapter
import com.civileva.table.example.presentation.legend.base.LabeledListLegend

open class CompetitionTableView(
	context: Context,
	attrs: AttributeSet,
) : TableView<Int, CellInteger>(context, attrs) {

	override fun getTopOffset(panelsOffsets: ILegendPanelAdapter.PanelsOffsets?): Int {
		var topOffset = super.getTopOffset(panelsOffsets)

/*

		val adapter = tableAdapter as? ILegendPanelAdapter
		if (adapter != null) {
			val legendWithHeaderPanel = adapter.findLegendPanel(LabeledListLegend::class.java)
			if (legendWithHeaderPanel != null) {
				val panelHeight = legendWithHeaderPanel.panelSize.height
				val itemsCountInPanel = legendWithHeaderPanel.legend.itemsCount
				topOffset += (panelHeight / itemsCountInPanel)
			}
		}
*/


		return topOffset
	}

}