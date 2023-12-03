private const val POSITIVE_FILTER_PREFIX = "+"
private const val NEGATIVE_FILTER_PREFIX = "-"

class PipelineSchedule(private val description: String) {
  interface Option
  data class StationNames(val names: List<String>, val inverse: Boolean) : Option
  data class CarName(val name: String) : Option

  fun options(): List<Option> = parse(description)

  private fun parse(description: String): List<Option> = description
    .split(" ")
    .filter { it.startsWith(POSITIVE_FILTER_PREFIX) || it.startsWith(NEGATIVE_FILTER_PREFIX) }
    .fold(mutableListOf()) { list, option ->
      when {
        option.startsWith("+stn:") -> list.add(StationNames(option.split(":").last().split(","), false))
        option.startsWith("-stn:") -> list.add(StationNames(option.split(":").last().split(","), true))
        option.startsWith("+car:") -> list.add(CarName(option.split(":").last()))
      }

      list
    }
}
