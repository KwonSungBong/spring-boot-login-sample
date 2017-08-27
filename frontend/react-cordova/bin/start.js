const getWebpackConfig = require('./get-webpack-config');
const webpack = require('webpack');
const WebpackDevServer = require('webpack-dev-server');
const proxy = require('http-proxy-middleware');

const args = process.argv.slice(2);
const isProd = args.indexOf('-p') !== -1;
const port = 8080;

const webpackConfig = getWebpackConfig({
  isProd,
  globals: {
    __DEVTOOLS__: !isProd
  },
  isWebpackDevServer: true,
  port
});

const server = new WebpackDevServer(webpack(webpackConfig), {
  stats: {
    colors: true
  },
  hot: true
});

function onProxyReq(proxyReq, req, res) {
  // proxyReq.setHeader('X-Auth-Token', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJseW5hcyIsImF1ZGllbmNlIjoid2ViIiwiY3JlYXRlZCI6MTUwMzgwOTg0ODM0MywiZXhwIjoxNTA0NDE0NjQ4fQ.3cil5OF0F0xJ9TyX3q3cCd1J8JAAontmhKo4DzJEmq5sgT1b-cAlfdMTGTXBjKL6h2jdruiaUehH4qRjMcivUQ');
}

server.use('/websocket', proxy('/', {target:'http://localhost:8888', changeOrigin: true, onProxyReq:onProxyReq}));
server.listen(port, '0.0.0.0', () => {
  console.log(`Listening at localhost:${port}`);
});
