package webhook

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
)

const contentType = "application/json"

func Send(url string, message string) error {
	body, err := json.Marshal(map[string]string{"content": message})
	if err != nil {
		return fmt.Errorf("error marshalling json: %w", err)
	}

	resp, err := http.Post(url, contentType, bytes.NewReader(body))
	if err != nil {
		return fmt.Errorf("error sending webhook: %w", err)
	}

	if resp.StatusCode != 204 {
		return fmt.Errorf("bad response from webhook: %s", resp.Status)
	}

	return nil
}
