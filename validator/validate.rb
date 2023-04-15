require 'gitlab'

Gitlab.configure do |config|
  config.endpoint = 'https://gitlab.com/api/v4'
  config.private_token = ENV['READ_SCHEDULES_TOKEN']
end

def clean_schedule_description(description)
  description.gsub(/\s*\([^)]*\)/, '')
end

matching_schedule = Gitlab
                      .pipeline_schedules(ENV['CI_PROJECT_ID'])
                      .find { |schedule| clean_schedule_description(schedule.description) == ENV['BOOKING_TIMES'] }

unless matching_schedule
  puts "No matching schedule found for #{ENV['BOOKING_TIMES']}"
  exit 1
end
