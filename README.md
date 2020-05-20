# 这是一个水果选购系统项目，基于一个毕设的题目。
系统用户包括管理员、商家、配送员和普通用户。
系统实现了商品、订单的管理，但没有接入支付，算是一个练手的项目。
系统运行所需的环境:mysql5.6+、支持springboot的开发工具(建议vscode)
运行步骤：
1.修改src/main/resources/app.properties中的file.uploadFolder为uploadImage文件夹所在路径（可新建，但旧图片失效），
2.修改src/main/resources/applicaiton.yml中数据库配置和邮箱配置
3.修改src/main/resources/jdbc.properties中的数据库配置
4.运行项目的主函数。
项目前端项目的源码放置在vue文件中，运行方式同普通的vue-cli3.0项目，已实现编译好整合进java中，所以也可不运行。
