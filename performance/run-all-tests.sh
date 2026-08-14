#!/bin/bash

echo "╔════════════════════════════════════════════╗"
echo "║   k6 Performance Testing Suite             ║"
echo "╚════════════════════════════════════════════╝"
echo ""

if ! command -v k6 &> /dev/null; then
    echo "❌ k6 is not installed!"
    echo "Install: brew install k6 (macOS) or sudo apt-get install k6 (Linux)"
    exit 1
fi

echo "✅ k6 found: $(k6 --version)"
echo ""

mkdir -p performance/results
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

echo "🚀 Starting performance tests..."
echo ""

echo "1️⃣  Load Test (5 users, 30s)"
k6 run performance/load-test.js --out json=performance/results/load-test_${TIMESTAMP}.json
echo ""

echo "2️⃣  Stress Test (ramp to 50 users)"
k6 run performance/stress-test.js --out json=performance/results/stress-test_${TIMESTAMP}.json
echo ""

echo "3️⃣  Spike Test (100 user spike)"
k6 run performance/spike-test.js --out json=performance/results/spike-test_${TIMESTAMP}.json
echo ""

echo "✅ All Performance Tests Completed"
echo "📊 Results saved to: performance/results/"
