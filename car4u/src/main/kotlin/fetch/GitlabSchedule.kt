package fetch

import org.gitlab4j.api.GitLabApi
import org.gitlab4j.api.models.PipelineSchedule

private const val GITLAB_HOST = "https://gitlab.com"

class GitlabSchedule(
  private val readSchedulesToken: String,
  private val ciProjectId: String,
  private val ciPipelineId: String
) {
  fun current(): PipelineSchedule? {
    val gitLabApi = GitLabApi(GITLAB_HOST, readSchedulesToken)

    return gitLabApi.pipelineApi
      .getPipelineSchedules(ciProjectId)
      .filter { it.active }
      .map { gitLabApi.pipelineApi.getPipelineSchedule(ciProjectId, it.id) }
      .find { it.lastPipeline?.id == ciPipelineId.toLong() }
  }
}
