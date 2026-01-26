mkdir -p infra nginx apps scripts docs

nginx-gateway-lab/
├── infra/
├── nginx/
├── apps/
├── scripts/
└── docs/


nginx/
├── nginx.conf
├── includes/
│   ├── logging.conf
│   ├── proxy.conf
│   └── headers.conf
└── sites/
    └── gateway.conf
