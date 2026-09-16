#!/bin/bash
set -e
aws --endpoint-url=http://localhost:4566 cloudformation deploy \
    --stack-name client-management \
    --template-file "./cdk.out/localstack.template.json"

aws --endpoint-url-http://localhost:4566 elbv2 describe-load-balancers \
    --query "LoadBalancer[0].DNSName" --output text
