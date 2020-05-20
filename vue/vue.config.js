module.exports = {
    devServer: {
        disableHostCheck: true,
        proxy: {
            '/': {
                target: 'http://localhost:8080',
                ws: true,
                changeOrigin: true
            }
        }
    }
}