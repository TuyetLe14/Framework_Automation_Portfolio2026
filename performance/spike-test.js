import http from 'k6/http';
import { check, group, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '10s', target: 0 },
    { duration: '1s', target: 100 },
    { duration: '10s', target: 100 },
    { duration: '1s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<2000', 'p(99)<3000'],
    http_req_failed: ['rate<0.1'],
  },
};

export default function () {
  group('Spike Test', () => {
    const res = http.get('https://portfolio-2026.tgdd-ld9941.workers.dev/');
    check(res, {
      'status is 200': (r) => r.status === 200,
      'handled spike': (r) => r.status !== 503,
    });
    sleep(0.5);
  });
}
