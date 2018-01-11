package tetris;

/**
 * ��Ϸ�з�����ʾ�Ļ�����
 * @author ������
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
	 private static GameCanvas canvas=null; //����ģʽ
	 private int boxWidth, boxHeight; //Ĭ��Ϊ�㣬��Ҫ����fanning��������
	 private Color blockColor = Color.RED, bgColor = new Color(0,204,204); //����Ĭ����ɫ������Ĭ�Ϻ�ɫ
	 private EtchedBorder border=new EtchedBorder(EtchedBorder.RAISED,Color.WHITE, new Color(148, 145, 140)); //���񻯱߿�
	 
	 /*
	  *���õ���ģʽ, ʹ����ֻ��һ��ʵ��
	 */
	 private GameCanvas()
	 {
	  boxes = new RussiaBox[rows][cols];
	  
	  for(int i = 0; i < boxes.length; i ++)
	      for(int j = 0; j<boxes[i].length; j ++)
	          boxes[i][j] = new RussiaBox(false);
	          
	  setBorder(border); //���ô�����߿� 
	 }
	 /*
	  *����ģʽ�����GameCanvasʵ��
	  */
	 public static GameCanvas getCanvasInstance()
	 {
	   if(canvas == null)
	     canvas = new GameCanvas();
	     
	    return canvas;
	 }
	 /*
	  *���û����ı���ɫ
	  */
	 public void setBgColor(Color bgColor)
	 {
	      this.bgColor = bgColor;
	 }
	 /*
	 * ��û����ı���ɫ
	 */
	 public Color getBgColor()
	 {
	      return bgColor;
	 }
	 /*
	  *���÷������ɫ
	  */
	 public void setBlockColor(Color blockColor)
	 {
	       this.blockColor = blockColor;
	 }
	 /*
	  *�������ɫ
	  */
	 public Color getBlockColor()
	 { 
	       return blockColor;
	 }
	 /*
	  *���û����з��������
	  */
	 public void setRows(int rows)
	 {
	      this.rows = rows;
	 }
	 /*
	  *�õ������з��������
	  */
	 public int getRows()
	 {
	     return rows;
	 } 
	 /*
	  *���û����з��������
	  */
	 public void setCols(int cols)
	 {
	      this.cols = cols;
	 }
	 /*
	  *�õ������з��������
	  */
	 public int getCols()
	 {
	      return cols; 
	 }
	 /*
	  *�õ�row��,col�еķ��񣨶�λһ��
	  */
	 public RussiaBox getBox(int row, int col)
	 {
	   if(row >= 0 && row < rows && col >= 0 && col < cols)
	       return boxes[row][col];
	       
	   else 
	        return null;
	 }
	 /*
	  *�ڻ����л��Ʒ���
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
	  *�����row�� ������row�����ϵ��и��ǵ�ǰ�У�
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
	  *���� Ϊ��ʼʱ��״̬
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
	  * ���ݴ���Ĵ�С�Զ���������Ĵ�С
	  */
	 public void fanning()
	 {
	   boxWidth = getSize().width / cols;
	   boxHeight = getSize().height / rows;
	 } 	 
}
