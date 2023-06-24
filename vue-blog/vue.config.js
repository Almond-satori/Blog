const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  // devServer:{
	// 	port:8081,  // 端口号的配置
	// },
  publicPath: './',
  // outputDir:'dist',
  // assetsDir:'static'
})

