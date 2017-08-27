const getWebpackConfig = require('./get-webpack-config');
const webpack = require('webpack');
const WebpackDevServer = require('webpack-dev-server');
const proxy = require('http-proxy-middleware');

const args = process.argv.slice(2);
const isProd = args.indexOf('-p') !== -1;
const port = 8787;

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

server.use('/api', proxy({target: 'http://localhost:8888/', changeOrigin: true, pathRewrite: {'^/api' : ''}}));
server.listen(port, '0.0.0.0', () => {
  console.log(`Listening at localhost:${port}`);
});
