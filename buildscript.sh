# Docker build

echo "build jar file ..."
./gradlew clean build

echo "build docker image ..."
docker build --platform linux/amd64 -t ilgolf/bank:latest .

echo "push docker image ..."
docker push ilgolf/bank:latest .

echo "remove docker image ..."
docker rmi -f ilgolf/bank:latest