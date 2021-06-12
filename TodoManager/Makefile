IMAGE=app

.PHONY: build
build:
	docker-compose build
start:
	docker-compose up
stop:
	docker-compose stop
down:
	docker-compose down
shell:
	docker-compose exec ${IMAGE} /bin/bash
run:
	docker-compose exec ${IMAGE} ${args}
gradle-clean-build:
	docker-compose exec ${IMAGE} gradle clean build
clean-mysql:
	./bin/mysql/reset.sh
restart: down start