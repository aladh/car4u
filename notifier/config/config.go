package config

import (
	"fmt"
	"os"
	"strings"
)

const separator = ","

type Config struct {
	PreferredStations []string
	ExcludedStations  []string
	WebhookURL        string
}

func FromEnv() (*Config, error) {
	preferredStations, err := getEnvSlice("PREFERRED_STATIONS")
	if err != nil {
		return nil, err
	}

	excludedStations, err := getEnvSlice("EXCLUDED_STATIONS")
	if err != nil {
		return nil, err
	}

	webhookURL, err := getEnvString("WEBHOOK_URL")
	if err != nil {
		webhookURL = ""
	}

	return &Config{
		PreferredStations: preferredStations,
		ExcludedStations:  excludedStations,
		WebhookURL:        webhookURL,
	}, nil
}

func getEnvString(key string) (string, error) {
	value, ok := os.LookupEnv(key)
	if !ok {
		return "", fmt.Errorf("environment variable %s not set", key)
	}

	return value, nil
}

func getEnvSlice(key string) ([]string, error) {
	value, err := getEnvString(key)
	if err != nil {
		return nil, err
	}

	return strings.Split(value, separator), nil
}
