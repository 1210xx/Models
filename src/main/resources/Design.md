- 实现单机版的demo
- 时间控制台的应用
  - 利用DBUtil，将每次猜测的系统时间、猜测的数字、猜测的结果三项数据记录到MySql数据库中
  - 采用JDBC访问数据库时，要求使用PreparedStatement并使用占位符方式绑定业务数据
  - 当用户猜中数字后，除了提示恭喜之外，还需要输出猜数的历史记录（时间倒序）

- 利用最初的sevlet实现网络编程
  - 网页交互实现前后端分离

- 微服务程序
- 编写开始游戏微服务：初始化猜测的随机数并保存到Mysql数据库表中，成功后返回提示信息
- 编写猜数微服务：根据用户提交的数字，与数据库中的随机数比较并且反馈
- 编写猜数记录查询微服务：列出所有猜数记录（时间倒序）

