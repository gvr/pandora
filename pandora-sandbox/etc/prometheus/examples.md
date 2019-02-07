## Prometheus examples for Pandora

Connect to the Prometheus server: http://localhost:9090

Example expressions:

```prometheus
rate(pandora_sandbox_requests_total[10m])
```

```prometheus
histogram_quantile(0.95, sum(rate(pandora_sandbox_responses_seconds_bucket[5m])) by (le, endpoint))

```