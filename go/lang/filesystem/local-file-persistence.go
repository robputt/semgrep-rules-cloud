package reports

import (
	"context"
	"os"

	"github.com/aws/aws-sdk-go-v2/service/s3"
)

func SaveReport(body []byte) error {
	// ruleid: local-file-persistence
	return os.WriteFile("./reports/latest.json", body, 0o600)
}

func EnsureUploadDir() error {
	// ruleid: local-file-persistence
	return os.MkdirAll("uploads/incoming", 0o750)
}

func StageInput() (*os.File, error) {
	// ruleid: local-file-persistence
	return os.Create("/tmp/staging/input.csv")
}

func ReadConfig() ([]byte, error) {
	// ok: local-file-persistence
	return os.ReadFile("./config/settings.yaml")
}

func ScratchSpace() (string, error) {
	// ok: local-file-persistence
	return os.MkdirTemp("", "render-*")
}

func UploadReport(ctx context.Context, client *s3.Client, body []byte) error {
	// ok: local-file-persistence
	_, err := client.PutObject(ctx, &s3.PutObjectInput{})
	return err
}
