#!/bin/bash
echo "Starting Expense Tracker with Docker..."
echo ""
echo "This will start:"
echo "- MySQL Database (port 3306)"
echo "- Redis Cache (port 6379)"
echo "- Expense Tracker App (port 8080)"
echo ""
echo "Press Ctrl+C to stop all services"
echo ""
docker-compose up --build
