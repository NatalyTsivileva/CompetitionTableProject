package com.civileva.table.example.presentation

import android.view.View
import com.civileva.table.example.presentation.dashboard.ILegendPanel

interface ITableAdapter<T> {
	fun getTableSize(): Int

	fun getData(): Array<T>
	fun getData(index: Int): T

	fun getTableViews(): Array<View>
	fun getTableView(index: Int): View

	fun getLegendPanels(): List<ILegendPanel>
	fun getLegendPanels(direction: ILegendPanel.Direction): List<ILegendPanel>
	fun getLegendViews(legendId: Int): List<View>
}