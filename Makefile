IMAGE=app

.PHONY: build
build:
	docker-compose build
start:
	docker-compose up
down:
	docker-compose down
shell:
	docker-compose exec ${IMAGE} /bin/bash
run:
	docker-compose exec ${IMAGE} ${args}
restart: down start