
docker build -t local/prometheus:latest .

echo 'run with: docker run -p 9090:9090 -v <absolute-path-to-data-directory>:/prometheus:rw local/prometheus'
