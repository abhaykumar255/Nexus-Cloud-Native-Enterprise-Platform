.PHONY: help build clean test start-infra stop-infra start-services stop-services logs

# Default target
help:
	@echo "NEXUS Backend Platform - Makefile Commands"
	@echo ""
	@echo "Build Commands:"
	@echo "  make build          - Build all services"
	@echo "  make clean          - Clean all build artifacts"
	@echo "  make test           - Run all tests"
	@echo ""
	@echo "Infrastructure Commands:"
	@echo "  make start-infra    - Start PostgreSQL, MongoDB, Redis, Kafka"
	@echo "  make stop-infra     - Stop infrastructure services"
	@echo ""
	@echo "Service Commands:"
	@echo "  make start-all      - Start all services with Docker Compose"
	@echo "  make stop-all       - Stop all services"
	@echo "  make restart        - Restart all services"
	@echo "  make logs           - View logs"
	@echo ""
	@echo "Development Commands:"
	@echo "  make dev            - Start infra + build + start services locally"
	@echo "  make status         - Show running services"

# Build all services
build:
	@echo "Building all services..."
	mvn clean package -DskipTests

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	mvn clean

# Run tests
test:
	@echo "Running tests..."
	mvn test

# Start infrastructure services only
start-infra:
	@echo "Starting infrastructure services..."
	docker-compose up -d postgres mongodb redis kafka elasticsearch

# Stop infrastructure services
stop-infra:
	@echo "Stopping infrastructure services..."
	docker-compose stop postgres mongodb redis kafka elasticsearch

# Start all services
start-all:
	@echo "Starting all services..."
	docker-compose up -d

# Stop all services
stop-all:
	@echo "Stopping all services..."
	docker-compose down

# Restart all services
restart:
	@echo "Restarting all services..."
	docker-compose restart

# View logs
logs:
	docker-compose logs -f

# Development mode (infra + local services)
dev: start-infra build
	@echo "Starting services locally..."
	@echo "Please start services manually or use docker-compose"

# Show status
status:
	@echo "Service Status:"
	docker-compose ps

# Build specific service
build-service:
	@echo "Building service: $(SERVICE)"
	mvn clean package -pl $(SERVICE) -am -DskipTests

# Start specific services (analytics and admin)
start-analytics:
	@echo "Starting Analytics Service..."
	docker-compose up -d analytics-service mongodb elasticsearch

start-admin:
	@echo "Starting Admin Service..."
	docker-compose up -d admin-service postgres

# View logs for specific service
logs-service:
	@echo "Viewing logs for: $(SERVICE)"
	docker-compose logs -f $(SERVICE)

# Health check all services
health-check:
	@echo "Checking health of all services..."
	@echo "\n=== Infrastructure Services ==="
	@curl -s http://localhost:8888/actuator/health | jq '.status' || echo "Config Server: DOWN"
	@curl -s http://localhost:8761/actuator/health | jq '.status' || echo "Discovery Service: DOWN"
	@curl -s http://localhost:8080/actuator/health | jq '.status' || echo "API Gateway: DOWN"
	@echo "\n=== Core Services ==="
	@curl -s http://localhost:8082/actuator/health | jq '.status' || echo "Auth Service: DOWN"
	@curl -s http://localhost:8081/actuator/health | jq '.status' || echo "User Service: DOWN"
	@echo "\n=== E-Commerce Services ==="
	@curl -s http://localhost:8087/actuator/health | jq '.status' || echo "Product Service: DOWN"
	@curl -s http://localhost:8090/actuator/health | jq '.status' || echo "Order Service: DOWN"
	@echo "\n=== Analytics & Admin ==="
	@curl -s http://localhost:8096/actuator/health | jq '.status' || echo "Analytics Service: DOWN"
	@curl -s http://localhost:8097/actuator/health | jq '.status' || echo "Admin Service: DOWN"

# Count total services
count:
	@echo "Total Services Implemented:"
	@find . -maxdepth 1 -type d -name "*-service" -o -name "*-gateway" -o -name "*-server" | wc -l | xargs echo "Services:"
	@echo "\nService List:"
	@ls -1 | grep -E "(service|gateway|server)$$" | sort

