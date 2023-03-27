package com.civileva.table.example.data

class CompetitionTable(val size: Int) {

	val cells = Array<ICompetitionTableCell>(size * size) { i: Int ->
		return@Array Cell(
			index = i,
			rowNumber = i / size,
			columnNumber = i % size
		)
	}

	fun updateScore(cell: ICompetitionTableCell, score: Int) {
		cells[cell.index] = Cell(
			index = cell.index,
			rowNumber = cell.rowNumber,
			columnNumber = cell.columnNumber,
			score = score
		)
	}

	fun aggregateRow(rowNumber: Int): RowAggregation {
		var actualFilledCount = 0
		var expectedFilledCount = size
		var rowSum = 0

		for (columnIndex: Int in 0 until size) {
			val index = (rowNumber * size) + columnIndex
			val cell = cells[index]

			when {
				cell.hasValidScore() && cell.isEnabledForInput() -> {
					actualFilledCount++
					rowSum += cell.score
				}

				!cell.isEnabledForInput() -> {
					expectedFilledCount--
				}
			}
		}

		return RowAggregation(
			rowNumber = rowNumber,
			rowSum = rowSum,
			isRowFullFilled = actualFilledCount == expectedFilledCount
		)

	}


	fun checkRewards(): List<Rewards>? {
		val results = mutableListOf<RowAggregation>()

		for (i: Int in 0 until size) {
			val aggr = aggregateRow(i)
			if (!aggr.isRowFullFilled) {
				return null
			} else {
				results.add(aggr)
			}
		}

		var place = 0
		return results
			.sortedByDescending { it.rowSum }
			.groupBy { it.rowSum }
			.flatMap { entry ->
				place++
				val rewards = entry.value.map {
					Rewards(aggr = it, place=place)
				}
				rewards
			}
	}

	inner class Cell(
		override val index: Int,
		override val rowNumber: Int,
		override val columnNumber: Int,
		override val score: Int = UNDEFINED_SCORE,
	) : ICompetitionTableCell {
		override fun isEnabledForInput() = rowNumber != columnNumber

		override fun isNewRow() = !isFirst() && (index % size == 0)

		override fun hasValidScore() = score in 0..5

		override fun isFirst() = index == 0

	}

	data class RowAggregation(
		val rowNumber: Int,
		val rowSum: Int,
		val isRowFullFilled: Boolean
	)

	data class Rewards(
		val aggr:RowAggregation,
		val place: Int
	)


	companion object {
		const val UNDEFINED_SCORE = -1
	}
}