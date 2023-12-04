package fetch.gitlab

import PipelineSchedule
import org.gitlab4j.api.GitLabApi

private const val GITLAB_HOST = "https://gitlab.com"

fun fetchPipelineSchedule(
  readSchedulesToken: String,
  ciProjectId: String,
  ciPipelineId: String
): PipelineSchedule? {
  val gitLabApi = GitLabApi(GITLAB_HOST, readSchedulesToken)

  return gitLabApi.pipelineApi
    .getPipelineSchedules(ciProjectId)
    .filter { it.active }
    .map { gitLabApi.pipelineApi.getPipelineSchedule(ciProjectId, it.id) }
    .find { it.lastPipeline?.id == ciPipelineId.toLong() }
    ?.let { PipelineSchedule(it.description) }
}
