# Useful links
- https://www.npmjs.com/package/@silvermine/serverless-plugin-cloudfront-lambda-edge
- https://www.serverless.com/framework/docs/providers/aws/events/cloudfront

call via CloudFront distribution instead of direct API Gateway endpoint
```
GET: d1f9gcwl3dde4x.cloudfront.net/dev/mypath?text=123
```
NOTE: to support authorization, we need to provide auth headers, but ignore them for caching