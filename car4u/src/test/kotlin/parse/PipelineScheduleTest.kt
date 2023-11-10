package parse

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class PipelineScheduleTest : DescribeSpec({
  describe("parse") {
    xit("returns a list of options described in the schedule description") {
      PipelineSchedule.parse("2023 01 1 12 00 2023 01 1 14 00 +stn:01,02 +car:car -stn:03") shouldBe listOf(
        PipelineSchedule.StationNames(listOf("01", "02"), false),
        PipelineSchedule.CarName("car"),
        PipelineSchedule.StationNames(listOf("03"), true)
      )
    }

    context("legacy options") {
      it("returns a list of options described in the schedule description") {
        PipelineSchedule.parse("2023 01 1 12 00 2023 01 1 14 00 -01,02") shouldBe listOf(
          PipelineSchedule.StationNames(listOf("01", "02"), true),
        )
      }
    }
  }
})
