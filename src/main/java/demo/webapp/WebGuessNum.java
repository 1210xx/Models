package demo.webapp;


import demo.guessnum.DBUtil;
import demo.guessnum.GuessNum;

/**
 * <head>web编程GuessNum01</head>
 *
 * <p>
 *     <ol>
 *         <li>
 *             通过最原始的socket编程交互
 *         </li>
 *         <li>
 *              从最简单的GET获取参数
 *         </li>
 *         <li>
 *             返回简单的页面
 *         </li>
 *     </ol>
 * </p>
 */
public class WebGuessNum {


    public static void main(String[] args) {
        GuessNum guessNum = new GuessNum();
        guessNum.guessNumApp();
    }

}
