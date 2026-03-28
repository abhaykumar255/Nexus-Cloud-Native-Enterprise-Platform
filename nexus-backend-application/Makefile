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
	docker-compose up -d postgres mongodb redis kafka

# Stop infrastructure services
stop-infra:
	@echo "Stopping infrastructure services..."
	docker-compose stop postgres mongodb redis kafka

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

