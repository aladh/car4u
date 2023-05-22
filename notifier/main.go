package main

import (
	"fmt"
	"log"
	"os"
	"strings"

	"notifier/config"
	"notifier/status"
	"notifier/webhook"
)

func main() {
	cfg, err := config.FromEnv()
	if err != nil {
		log.Fatalf("Could not load config: %s", err)
	}

	status, err := status.LoadFrom(os.Stdin)
	if err != nil {
		log.Fatalf("Could not load status file: %s", err)
	}

	log.Printf("Running notifier for booking times %s to %s with preferred stations %s and exlcuded stations %s\n", status.BookingStart, status.BookingEnd, cfg.PreferredStations, cfg.ExcludedStations)

	err = notify(status, cfg)
	if err != nil {
		log.Fatalf("Could not notify for preferred stations: %s", err)
	}
}

func notify(status *status.Status, cfg *config.Config) error {
	for _, station := range status.Stations {
		if containsAny(station.Name, cfg.ExcludedStations) {
			log.Println("Skipping excluded station: " + station.Name)
			continue
		}

		if containsAny(station.Name, cfg.PreferredStations) && station.HasAvailableCars() {
			message := fmt.Sprintf("Car(s) available at %s from %s to %s", station.Name, status.BookingStart, status.BookingEnd)
			log.Println(message)

			if len(cfg.WebhookURL) > 0 {
				err := webhook.Send(cfg.WebhookURL, message)
				if err != nil {
					return fmt.Errorf("could not send webhook: %w", err)
				}
			}
		}
	}

	return nil
}

func containsAny(stationName string, stationIDs []string) bool {
	for _, stationID := range stationIDs {
		if strings.Contains(strings.ToLower(stationName), strings.ToLower(stationID)) {
			return true
		}
	}

	return false
}
