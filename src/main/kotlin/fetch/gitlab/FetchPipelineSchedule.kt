package fetch.gitlab

import PipelineSchedule
import org.gitlab4j.api.GitLabApi

private const val GITLAB_HOST = "https://gitlab.com"

fun fetchPipelineSchedule(
  readSchedulesToken: String?,
  ciProjectId: String?,
  ciPipelineId: String?
): PipelineSchedule? {
  requireNotNull(readSchedulesToken) { "READ_SCHEDULES_TOKEN environment variable not set" }
  requireNotNull(ciProjectId) { "CI_PROJECT_ID environment variable not set" }
  requireNotNull(ciPipelineId) { "CI_PIPELINE_ID environment variable not set" }

  val gitLabApi = GitLabApi(GITLAB_HOST, readSchedulesToken)

  return gitLabApi.pipelineApi
    .getPipelineSchedules(ciProjectId)
    .filter { it.active }
    .map { gitLabApi.pipelineApi.getPipelineSchedule(ciProjectId, it.id) }
    .find { it.lastPipeline?.id == ciPipelineId.toLong() }
    ?.let { PipelineSchedule(it.description) }
}
