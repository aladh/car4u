require 'gitlab'

Gitlab.configure do |config|
  config.endpoint = 'https://gitlab.com/api/v4'
  config.private_token = ENV['READ_SCHEDULES_TOKEN']
end

schedule = Gitlab
             .pipeline_schedules(ENV['CI_PROJECT_ID'])
             .select { |schedule| schedule.active }
             .map { |schedule| Gitlab.pipeline_schedule(ENV['CI_PROJECT_ID'], schedule.id) }
             .find { |schedule| schedule.last_pipeline&.id == ENV['CI_PIPELINE_ID'].to_i }

print schedule.description
