package org.apache.spark.sql.catalyst

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan

sealed abstract class ParseResult

final case class ParseSucceed(parsed: LogicalPlan) extends ParseResult

final case class ParseFailed(msg: String, line: Int, column: Int) extends ParseResult
