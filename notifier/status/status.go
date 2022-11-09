package status

import (
	"encoding/json"
	"fmt"
	"io"
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

func LoadFrom(reader io.Reader) (*Status, error) {
	var status Status

	err := json.NewDecoder(reader).Decode(&status)
	if err != nil {
		return nil, fmt.Errorf("could not decode text: %w", err)
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
