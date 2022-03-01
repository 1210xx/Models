package demo.webapp;


/**
 * 编写一个完善的HTTP服务器，以HTTP/1.1为例，需要考虑的包括：
 *
 * 识别正确和错误的HTTP请求；
 * 识别正确和错误的HTTP头；
 * 复用TCP连接；
 * 复用线程；
 * IO异常处理；
 * ...
 *
 * 这些基础工作需要耗费大量的时间，并且经过长期测试才能稳定运行。
 * 如果我们只需要输出一个简单的HTML页面，就不得不编写上千行底层代码，那就根本无法做到高效而可靠地开发。
 *
 * 在JavaEE平台上，处理TCP连接，解析HTTP协议这些底层工作统统扔给现成的**Web服务器**去做，我们只需要把自己的应用程序跑在Web服务器上。
 *
 * 为了实现这一目的，JavaEE提供了Servlet API，使用Servlet API编写自己的Servlet来处理HTTP请求，Web服务器实现Servlet API接口，实现底层功能.
 *
 */
public class WebGuessNum {
}
