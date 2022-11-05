package status

import (
	"encoding/json"
	"fmt"
	"os"
)

type Car struct {
	Name      string `json:"name"`
	Available bool   `json:"available"`
}

type Station struct {
	Name string `json:"name"`
	Cars []Car  `json:"cars"`
}

type Status struct {
	BookingStart string    `json:"bookingStart"`
	BookingEnd   string    `json:"bookingEnd"`
	Stations     []Station `json:"stations"`
}

func LoadFrom(filename string) (*Status, error) {
	file, err := os.Open(filename)
	if err != nil {
		return nil, fmt.Errorf("could not open file: %w", err)
	}

	var status Status
	err = json.NewDecoder(file).Decode(&status)
	if err != nil {
		return nil, fmt.Errorf("could not decode file: %w", err)
	}

	return &status, nil
}

func (station *Station) HasAvailableCars() bool {
	for _, car := range station.Cars {
		if car.Available {
			return true
		}
	}

	return false
}
