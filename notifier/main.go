package main

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

func main() {
	file, err := os.Open("status.json")
	if err != nil {
		panic(err)
	}

	var status Status
	err = json.NewDecoder(file).Decode(&status)
	if err != nil {
		panic(err)
	}

	fmt.Println(status)
}
