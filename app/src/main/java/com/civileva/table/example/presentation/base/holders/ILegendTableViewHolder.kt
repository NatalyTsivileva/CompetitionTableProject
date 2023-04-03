package com.civileva.table.example.presentation.base.holders

import android.view.View

interface ILegendTableViewHolder {
	fun bindPanelData(data:List<*>)
	fun getPanelViews(): List<View>
}