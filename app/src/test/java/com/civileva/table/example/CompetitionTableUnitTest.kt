package com.civileva.table.example

import com.civileva.table.example.data.CompetitionTable
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class CompetitionTableUnitTest {

	@Test
	fun `when fill all row by scores(=1) - while row number=3 and table size=7 - should return correct Aggregate(sum=6)`() {
		val size = 7
		val rowNumber = 3
		val expectedRowSum = 6

		val table = CompetitionTable(size)

		for (columnIndex: Int in 0 until size) {
			val index = (rowNumber * size) + columnIndex
			table.updateScore(table.cells[index], 1)
		}

		val rowAggregate = table.aggregateRow(rowNumber)

		Assert.assertTrue(rowAggregate.isRowFullFilled)
		Assert.assertEquals(expectedRowSum, rowAggregate.rowSum)
	}

	/**
	 * [  *   5   0  ]
	 * [  3   *   4  ]
	 * [  2   3   *  ]
	 */
	@Test
	fun `when filled all table - while size=3 - should return places 1,1,2`() {
		val size = 3

		val table = CompetitionTable(size)

		var cell = table.cells[1]
		table.updateScore(cell, 5)

		cell = table.cells[2]
		table.updateScore(cell, 0)

		cell = table.cells[3]
		table.updateScore(cell, 3)

		cell = table.cells[5]
		table.updateScore(cell, 4)

		cell = table.cells[6]
		table.updateScore(cell, 2)

		cell = table.cells[7]
		table.updateScore(cell, 3)

		val rewards = table.checkRewards()
		rewards?.forEach {
			println("ряд=${it.aggr.rowNumber}, сумма очков=${it.aggr.rowSum}, место=${it.place}")
		}
	}

	/**
	 * [  *  -1   0  ]
	 * [  3   *   4  ]
	 * [  2   3   *  ]
	 */
	@Test
	fun `when filled all table - while size=3 and one of rows not filled - should return null`() {
		val size = 3

		val table = CompetitionTable(size)

		var cell = table.cells[1]
		table.updateScore(cell, CompetitionTable.UNDEFINED_SCORE)

		cell = table.cells[2]
		table.updateScore(cell, 0)

		cell = table.cells[3]
		table.updateScore(cell, 3)

		cell = table.cells[5]
		table.updateScore(cell, 4)

		cell = table.cells[6]
		table.updateScore(cell, 2)

		cell = table.cells[7]
		table.updateScore(cell, 3)

		val rewards = table.checkRewards()
		Assert.assertEquals(null, rewards)
	}


}