package main

import (
	"fmt"
	"log"
	"strings"

	"notifier/config"
	"notifier/status"
	"notifier/webhook"
)

func main() {
	status, err := status.LoadFrom("status.json")
	if err != nil {
		log.Fatalln(fmt.Errorf("could not load status: %w", err))
	}

	cfg, err := config.FromEnv()
	if err != nil {
		log.Fatalln(fmt.Errorf("could not load config: %w", err))
	}

	for _, station := range status.Stations {
		if station.HasAvailableCars() && containsAny(station.Name, cfg.PreferredStations) {
			message := fmt.Sprintf("Car(s) available at %s from %s to %s", station.Name, status.BookingStart, status.BookingEnd)
			log.Println(message)

			err = webhook.Send(cfg.WebhookURL, message)
			if err != nil {
				log.Fatalln(fmt.Errorf("could not send webhook: %w", err))
			}
		}
	}
}

func containsAny(stationName string, preferredStations []string) bool {
	for _, preferredStation := range preferredStations {
		if strings.Contains(strings.ToLower(stationName), strings.ToLower(preferredStation)) {
			return true
		}
	}

	return false
}
