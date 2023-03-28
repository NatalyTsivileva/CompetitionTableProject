package com.civileva.table.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.civileva.table.example.data.CompetitionTable
import com.civileva.table.example.data.ICompetitionTableCell
import com.civileva.table.example.presentation.CompetitionTableAdapter
import com.civileva.table.example.utils.CompetitionTableLegendCreator
import com.civileva.table.example.widget.CompetitionsTableView
import com.civileva.table.test.R

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		initTable()
	}

	private fun initTable(){
		val tableView = findViewById<CompetitionsTableView<ICompetitionTableCell>>(R.id.tableView)

		val tableData = CompetitionTable(7)

		tableView.tableAdapter = CompetitionTableAdapter(
			applicationContext,
			tableView.attrs,
			tableData,
			CompetitionTableLegendCreator.initLegendPanel(applicationContext,tableData)
		)
	}
}