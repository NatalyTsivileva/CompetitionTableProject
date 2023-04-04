package com.civileva.table.example.presentation.base.holders

import android.view.View
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.presentation.base.listener.ITableClickListener

interface ITableViewHolder<T : Comparable<T>, C:ITableCell<T>> {
	fun getTableView(cell:C): View?
	fun bindData(cell: C, listener:ITableClickListener<T, C>?)

	fun destroyView()
}
