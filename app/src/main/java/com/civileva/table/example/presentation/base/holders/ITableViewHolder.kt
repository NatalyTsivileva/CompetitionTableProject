package com.civileva.table.example.presentation.base.holders

import android.view.View
import com.civileva.table.example.data.ITableCell

interface ITableViewHolder<T : Comparable<T>, C:ITableCell<T>> {
	fun getTableView(): View
	fun bindData(cell: C)
}
