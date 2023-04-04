package com.civileva.table.example.presentation.base.listener

import com.civileva.table.example.presentation.base.legends.ILegendPanel

interface ILegendClickListener<P : ILegendPanel> {
	fun onClick(panel: P, data: Any)
}