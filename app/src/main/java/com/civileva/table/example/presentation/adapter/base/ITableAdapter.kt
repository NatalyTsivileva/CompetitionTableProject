package com.civileva.table.example.presentation.adapter.base

import android.view.View
import com.civileva.table.example.data.ITableCell

interface ITableAdapter<T : Comparable<T>, C : ITableCell<T>> {
	fun getTableSize(): Int

	fun getTableData(): ArrayList<C>
	fun getTableData(index: Int): C

	fun getTableViews(): ArrayList<View>
	fun getTableView(index: Int): View

}