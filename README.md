# Pandora
Demo Scala server

#### Metrics demo

##### Grafana dashboard
Run with docker: see [grafana.org](http://docs.grafana.org/installation/docker)

```
docker run -d -p 3000:3000 grafana/grafana
```

##### Prometheus
For the metrics endpoint, set up with *micrometer*.

In *pandora-sandbox/etc/prometheus*:
```
docker build -t pandora/prometheus .
```

```
docker run -p 9090:9090 pandora/prometheus
```
