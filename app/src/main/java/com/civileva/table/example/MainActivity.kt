package com.civileva.table.example

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.presentation.adapter.CompetitionTableAdapter
import com.civileva.table.example.presentation.legend.base.ILegendPanel
import com.civileva.table.example.utils.TableLegendUtils
import com.civileva.table.example.utils.TableUtils
import com.civileva.table.example.widget.TableView
import com.civileva.table.test.R

class MainActivity : AppCompatActivity() {
	private var tableSize = 7
	private var tableView: TableView<Int, CellInteger>? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		initTable(tableSize, excludePanelsIds = listOf(ILegendPanel.TEST))
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		MenuInflater(applicationContext).inflate(R.menu.main_menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.menuLegendFull -> {
				initTable(tableSize)
				true
			}

			R.id.menuLegendOnlyIteration -> {
				initTable(
					size = tableSize,
					excludePanelsIds = listOf(
						ILegendPanel.LEFT_COMPETITION,
						ILegendPanel.RIGHT_SCORE,
						ILegendPanel.RIGHT_PLACE,
						ILegendPanel.TEST
					)
				)
				true
			}

			R.id.menuTable2x2 -> {
				tableSize = 2
				initTable(tableSize)
				true
			}

			R.id.menuTable4x4 -> {
				tableSize = 4
				initTable(tableSize)
				true
			}

			R.id.menuTable7x7 -> {
				tableSize = 7
				initTable(tableSize)
				true
			}

			R.id.menuTable10x10 -> {
				tableSize = 10
				initTable(tableSize)
				true
			}

			R.id.menuTableDelete -> {
				tableView?.release()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	private fun initTable(
		size: Int,
		excludePanelsIds: List<Int> = emptyList()
	) {
		tableView = findViewById(R.id.tableView)

		val tableData = TableUtils.createIntegerTable(size)

		val legendPanel =
			TableLegendUtils.createCompetitionLegendPanel(applicationContext, tableData)


		tableView?.apply {
			tableAdapter = CompetitionTableAdapter(
				applicationContext,
				attrs,
				tableData,
				excludePanels(legendPanel.first, legendPanel.second, excludePanelsIds)
			)
		}
	}

	private fun excludePanels(
		panels: List<ILegendPanel>,
		viewMap: Map<Int, List<View>>,
		ids: List<Int>
	): Pair<List<ILegendPanel>, Map<Int, List<View>>> {
		val newPanels = panels.filter { panel ->
			!ids.contains(panel.id)
		}
		val newLegendViewsMap = viewMap.filter {
			!ids.contains(it.key)
		}
		return Pair(newPanels, newLegendViewsMap)
	}


}