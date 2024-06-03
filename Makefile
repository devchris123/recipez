
run-local:
	docker-compose -f docker-compose.yml -f docker-compose.local.yml up --build

down-local:
	docker-compose -f docker-compose.yml -f docker-compose.local.yml down -v

run-it:
	docker-compose -f docker-compose.yml -f docker-compose.test.yml up --build --abort-on-container-exit ; \
	docker-compose -f docker-compose.yml -f docker-compose.test.yml down