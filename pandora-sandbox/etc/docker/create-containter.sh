# prerequisite: built with sbt assembly
cp ../../target/scala-2.12/pandora-sandbox-assembly-*.jar ./target/pandora.jar

docker build -t local/pandora-sandbox:latest .

echo 'run with: docker run -p 8080:8080 local/pandora-sandbox'
