.PHONY: install test generate test-docker

install:
	mvn clean package -DskipTests

test:
	mvn clean test
