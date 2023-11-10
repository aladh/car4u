package parse

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class PipelineScheduleTest : DescribeSpec({
  describe("parse") {
    it("returns a list of options described in the schedule description") {
      PipelineSchedule.parse("+stn:01,02 +car:car -stn:03") shouldBe listOf(
        PipelineSchedule.StationNames(listOf("01", "02"), false),
        PipelineSchedule.CarName("car"),
        PipelineSchedule.StationNames(listOf("03"), true)
      )
    }
  }
})
