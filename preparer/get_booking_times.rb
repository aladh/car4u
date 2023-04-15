require 'gitlab'

Gitlab.configure do |config|
  config.endpoint = 'https://gitlab.com/api/v4'
  config.private_token = ENV['READ_SCHEDULES_TOKEN']
end

def clean_schedule_description(description)
  description.gsub(/\s*\([^)]*\)/, '')
end

schedule = Gitlab
             .pipeline_schedules(ENV['CI_PROJECT_ID'])
             .select { |schedule| schedule.active }
             .map { |schedule| Gitlab.pipeline_schedule(ENV['CI_PROJECT_ID'], schedule.id) }
             .find { |schedule| schedule.last_pipeline.id == ENV['CI_PIPELINE_ID'].to_i }

booking_times = clean_schedule_description(schedule.description)
puts booking_times
