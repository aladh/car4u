import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class PipelineScheduleTest : DescribeSpec({
  describe("options") {
    it("returns a list of options described in the schedule description") {
      PipelineSchedule("2023 01 1 12 00 2023 01 1 14 00 +stn:01,02 +car:car -stn:03").options() shouldBe listOf(
        PipelineSchedule.StationNames(listOf("01", "02"), false),
        PipelineSchedule.CarName("car"),
        PipelineSchedule.StationNames(listOf("03"), true)
      )
    }
  }
})
