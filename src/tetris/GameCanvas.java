package tetris;

/**
 * 游戏中方块显示的画布类
 * @author 张云天
 * 
 */

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class GameCanvas extends JPanel{
	
	 private RussiaBox [][]boxes;
	 private int rows = 20 , cols = 12;
	 private static GameCanvas canvas=null; //单件模式
	 private int boxWidth, boxHeight; //默认为零，需要调用fanning函数设置
	 private Color blockColor = Color.RED, bgColor = new Color(0,204,204); //背景默认蓝色，方块默认红色
	 private EtchedBorder border=new EtchedBorder(EtchedBorder.RAISED,Color.WHITE, new Color(148, 145, 140)); //浮雕化边框
	 
	 /*
	  *采用单件模式, 使该类只有一个实例
	 */
	 private GameCanvas()
	 {
	  boxes = new RussiaBox[rows][cols];
	  
	  for(int i = 0; i < boxes.length; i ++)
	      for(int j = 0; j<boxes[i].length; j ++)
	          boxes[i][j] = new RussiaBox(false);
	          
	  setBorder(border); //设置此组件边框 
	 }
	 /*
	  *单件模式，获得GameCanvas实例
	  */
	 public static GameCanvas getCanvasInstance()
	 {
	   if(canvas == null)
	     canvas = new GameCanvas();
	     
	    return canvas;
	 }
	 /*
	  *设置画布的背景色
	  */
	 public void setBgColor(Color bgColor)
	 {
	      this.bgColor = bgColor;
	 }
	 /*
	 * 获得画布的背景色
	 */
	 public Color getBgColor()
	 {
	      return bgColor;
	 }
	 /*
	  *设置方块的颜色
	  */
	 public void setBlockColor(Color blockColor)
	 {
	       this.blockColor = blockColor;
	 }
	 /*
	  *方块的颜色
	  */
	 public Color getBlockColor()
	 { 
	       return blockColor;
	 }
	 /*
	  *设置画布中方块的行数
	  */
	 public void setRows(int rows)
	 {
	      this.rows = rows;
	 }
	 /*
	  *得到画布中方块的行数
	  */
	 public int getRows()
	 {
	     return rows;
	 } 
	 /*
	  *设置画布中方块的列数
	  */
	 public void setCols(int cols)
	 {
	      this.cols = cols;
	 }
	 /*
	  *得到画布中方块的列数
	  */
	 public int getCols()
	 {
	      return cols; 
	 }
	 /*
	  *得到row行,col列的方格（定位一格）
	  */
	 public RussiaBox getBox(int row, int col)
	 {
	   if(row >= 0 && row < rows && col >= 0 && col < cols)
	       return boxes[row][col];
	       
	   else 
	        return null;
	 }
	 /*
	  *在画布中绘制方块
	  */   
	 @Override
	 public void paintComponent(Graphics g)
	 {
	   super.paintComponent(g);
	   
	   fanning();
	   for(int i = 0; i < boxes.length; i ++)
	      for(int j = 0; j < boxes[i].length; j ++)
	        {
	          Color color = boxes[i][j].isColorBox() ? blockColor : bgColor;
	          g.setColor(color);
	          g.fill3DRect(j * boxWidth, i * boxHeight , boxWidth , boxHeight , true);
	        }   
	 }
	 /*
	  *清除第row行 （将第row行以上的行覆盖当前行）
	  */
	 public void removeLine(int row)
	 {
	  for(int i = row; i > 0; i --)
	    for(int j = 0; j < cols; j ++)
	    { 
	          boxes[i][j] = (RussiaBox)boxes[i-1][j].clone();
	     }
	 }
	 /*
	  *重置 为初始时的状态
	  */
	 public void reset()
	 {
	   for(int i = 0; i < boxes.length; i++)
	       for(int j = 0 ;j < boxes[i].length; j++)
	       {
	         boxes[i][j].setColor(false);
	       }
	    repaint();
	 }
	 /*
	  * 根据窗体的大小自动调整方格的大小
	  */
	 public void fanning()
	 {
	   boxWidth = getSize().width / cols;
	   boxHeight = getSize().height / rows;
	 } 	 
}
