package com.civileva.table.example.presentation.implementations.adapter

import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.base.holders.ILegendTableViewHolder
import com.civileva.table.example.presentation.base.holders.ITableViewHolder
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.base.listener.ITableClickListener


class CompetitionLegendTableAdapter(
	private val table: Table<Int, CellInteger>,
	cellistener: ITableClickListener<Int, CellInteger>,
	cellHolders: List<ITableViewHolder<Int, CellInteger>>,
	legendHolderMap: Map<ILegendPanel, ILegendTableViewHolder>,
) : LegendTableAdapter<Int, CellInteger>(table, cellistener, cellHolders, legendHolderMap) {


}
