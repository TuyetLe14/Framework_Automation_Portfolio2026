import http from 'k6/http';
import { check, group, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '2m', target: 10 },
    { duration: '5m', target: 10 },
    { duration: '2m', target: 50 },
    { duration: '5m', target: 50 },
    { duration: '2m', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<1000', 'p(99)<2000'],
    http_req_failed: ['rate<0.05'],
  },
};

export default function () {
  group('Load Test - Homepage', () => {
    const res = http.get('https://portfolio-2026.tgdd-ld9941.workers.dev/');
    check(res, {
      'status is 200': (r) => r.status === 200,
      'load time < 1s': (r) => r.timings.duration < 1000,
      'has content': (r) => r.body.length > 1000,
    });
    sleep(1);
  });

  group('Load Test - About Page', () => {
    const res = http.get('https://portfolio-2026.tgdd-ld9941.workers.dev/#/about');
    check(res, {
      'status is 200': (r) => r.status === 200,
      'load time < 1s': (r) => r.timings.duration < 1000,
    });
    sleep(1);
  });
}
