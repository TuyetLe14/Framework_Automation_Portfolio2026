import http from 'k6/http';
import { check, group, sleep } from 'k6';

export const options = {
  vus: 5,
  duration: '30s',
  thresholds: {
    http_req_duration: ['p(95)<500', 'p(99)<1000'],
    http_req_failed: ['rate<0.1'],
  },
};

export default function () {
  group('Homepage Tests', () => {
    const resHome = http.get('https://portfolio-2026.tgdd-ld9941.workers.dev/');
    check(resHome, {
      'homepage status is 200': (r) => r.status === 200,
      'homepage load time < 500ms': (r) => r.timings.duration < 500,
      'homepage has content': (r) => r.body.length > 0,
    });
    sleep(1);

    const resAbout = http.get('https://portfolio-2026.tgdd-ld9941.workers.dev/#/about');
    check(resAbout, {
      'about page status is 200': (r) => r.status === 200,
      'about page load time < 500ms': (r) => r.timings.duration < 500,
    });
    sleep(1);
  });
}
