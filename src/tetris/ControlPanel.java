package tetris;
/**
 * 控制面板类
 * @author 张云天
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;


@SuppressWarnings("serial")
public class ControlPanel extends JPanel{
	 private TipBlockPanel tipBlockPanel;//预显方块显示区
	 private JPanel tipPanel,InfoPanel,buttonPanel;
	 private final JTextField levelField,scoreField; //单行编辑文本
	 private JButton playButton,pauseButton,stopButton,
	                 turnHarderButton,turnEasilyButton;
	 
	  private EtchedBorder border=new EtchedBorder(EtchedBorder.RAISED,Color.WHITE, new Color(148, 145, 140)) ;
	  
	  private RussiaBlocksGame game;
	  private Timer timer;
	   
	 public ControlPanel(final RussiaBlocksGame game)
	 {
	  this.game = game;
	  /*
	   *图形界面部分
	  */
	  setLayout(new GridLayout(3,1,0,4));
	  
	  tipBlockPanel = new TipBlockPanel();
	  tipPanel = new JPanel( new BorderLayout() );
	  tipPanel.add( new JLabel("下一块:") , BorderLayout.NORTH );
	  tipPanel.add( tipBlockPanel , BorderLayout.CENTER );
	  tipPanel.setBorder(border);
	  
	  InfoPanel = new JPanel( new GridLayout(4,1,0,0) );//网格布局
	  levelField = new JTextField(""+RussiaBlocksGame.DEFAULT_LEVEL);
	  levelField.setEditable(false);
	  scoreField = new JTextField("0");
	  scoreField.setEditable(false);
	  InfoPanel.add(new JLabel("难度系数:"));
	  InfoPanel.add(levelField);
	  InfoPanel.add(new JLabel("分数:"));
	  InfoPanel.add(scoreField);
	  InfoPanel.setBorder(border);
	  
	  buttonPanel = new JPanel(new GridLayout(5,1,0,0));
	  playButton = new JButton("开始");
	  pauseButton = new JButton("暂停");
	  stopButton = new JButton("结束");
	  turnHarderButton = new JButton("难度增加");
	  turnEasilyButton = new JButton("难度降低");
	 
	  buttonPanel.add(playButton);
	  buttonPanel.add(pauseButton);
	  buttonPanel.add(stopButton);
	  buttonPanel.add(turnHarderButton);
	  buttonPanel.add(turnEasilyButton);
	  buttonPanel.setBorder(border);
	   
	   addKeyListener(new ControlKeyListener());//添加
	   
	   add(tipPanel);
	   add(InfoPanel);
	   add(buttonPanel);
	 /*
	  *添加事件监听器
	  */ 
	   playButton.addActionListener(
	   new ActionListener()
	   {
	    public void actionPerformed(ActionEvent event)
	    {
	     game.playGame();
	     requestFocus();//让ControlPanel重新获得焦点以响应键盘事件
	    }
	    });
	    
	    pauseButton.addActionListener(
	    new ActionListener()
	    {
	     public void actionPerformed(ActionEvent event)
	     {
	      if(pauseButton.getText().equals("暂停"))
	        game.pauseGame();
	      else
	        game.resumeGame(); 
	      requestFocus();//让ControlPanel重新获得焦点以响应键盘事件
	     }
	    }
	    );
	    stopButton.addActionListener(
	    new ActionListener()
	    {
	     public void actionPerformed(ActionEvent event)
	     {
	      game.stopGame();
	      requestFocus();//让ControlPanel重新获得焦点以响应键盘事件
	     }
	    });
	   turnHarderButton.addActionListener(
	   new ActionListener()
	   {
	     public void actionPerformed(ActionEvent event)
	     {
	       int level = 0;
	       try{
	           level = Integer.parseInt(levelField.getText());
	           setLevel(level + 1);
	          }catch(NumberFormatException e)
	          {
	           e.printStackTrace();
	          }
	       requestFocus();//让ControlPanel重新获得焦点以响应键盘事件
	     }
	   });
	   turnEasilyButton.addActionListener(
	   new ActionListener()
	   {
	     public void actionPerformed(ActionEvent event)
	     {
	       int level = 0;
	       try{
	           level = Integer.parseInt(levelField.getText());
	           setLevel(level - 1); 
	          }catch(NumberFormatException e)
	          {
	           e.printStackTrace();
	          }
	      requestFocus();//让ControlPanel重新获得焦点以响应键盘事件
	     }
	   });
	    /*
	    *  时间驱动程序，每格500毫秒对level,score值进行更新
	    */
	   timer =  new Timer(500, new ActionListener()
       {
		    public void actionPerformed(ActionEvent event)
		    {
		     scoreField.setText(""+game.getScore());
		     game.levelUpdate();
		    }
		   });
	   
	   timer.start();  
	 }  
	 /*
	  * 设置预显方块的样式
	  */
	 public void setBlockStyle(int style)
	 {
	  tipBlockPanel.setStyle(style);
	  tipBlockPanel.repaint();
	 }  
	 /*
	  * 重置，将所有数据恢复到最初值
	  */
	 public void reset()
	 {
	  levelField.setText(""+RussiaBlocksGame.DEFAULT_LEVEL);
	  scoreField.setText("0");
	  setPlayButtonEnabled(true);
	  setPauseButtonLabel(true);
	  tipBlockPanel.setStyle(0);
	 }
	 
	 /*
	  *设置playButton是否可用
	  */
	 public void setPlayButtonEnabled(boolean enable)
	 {
	  playButton.setEnabled(enable);
	 }
	 
	 /*
	  *设置pauseButton的文本
	  */
	 public void setPauseButtonLabel(boolean pause)
	 {
	  pauseButton.setText( pause ? "暂停" : "继续" );
	 } 
	 
	 /*
	  *设置方块的大小,改变窗体大小时调用可自动调整方块到合适的尺寸
	  */
	 public void fanning()
	 {
	  tipBlockPanel.fanning();
	 }   
	 /*
	  *根据level文本域的值返回当前的级别
	  */
	 public int getLevel()
	  {
	     int level = 0;
	     try
	     {
	      level=Integer.parseInt(levelField.getText());
	     }catch(NumberFormatException e)
	     {
	       e.printStackTrace();
	     }
	     return level;
	  }
	  /*
	   * 设置level文本域的值
	   */
	  public void setLevel(int level)
	  {
	    if(level > 0 && level <= RussiaBlocksGame.MAX_LEVEL)
	       levelField.setText("" + level);
	  }   
	  /*
	   * 内部类 为预显方块（下一块）的显示区域
	   */
	  private class TipBlockPanel extends JPanel
	  {
	    private Color bgColor = Color.darkGray, 
	                  blockColor = Color.lightGray;              
	    private RussiaBox [][]boxes = new RussiaBox[RussiaBlock.ROWS][RussiaBlock.COLS];
	    private int boxWidth, boxHeight,style;
	    private boolean isTiled = false; //一次调整
	    
	    /*
	     * 构造函数
	     */
	   public TipBlockPanel()
	   {
	    for(int i = 0; i < boxes.length; i ++)
	       for(int j = 0; j < boxes[i].length; j ++)
	         {
	          boxes[i][j]=new RussiaBox(false);
	         }
	         style = 0x0000; 
	   }
	   /*
	    * 构造函数
	    */
	   @SuppressWarnings("unused")
	public TipBlockPanel(Color bgColor, Color blockColor)
	   {
	    this();
	    this.bgColor = bgColor;
	    this.blockColor = blockColor;
	   }
	    /*
	     * 设置方块的风格
	     */
	    public void setStyle(int style)
	    {
	     this.style = style;
	     repaint();
	    }
	    
	    /*
	     * 绘制预显方块
	     */
	   public void paintComponent(Graphics g)
	   {
	    super.paintComponent(g);
	    
	    int key = 0x8000;
	    
	    if(!isTiled)
	       fanning();
	    for(int i = 0; i < boxes.length; i ++)
	      for(int j = 0; j<boxes[i].length ;j ++)
	       {
	         Color color = (style & key) != 0  ?  blockColor : bgColor;
	         g.setColor(color);
	         g.fill3DRect(j * boxWidth, i * boxHeight, boxWidth, boxHeight, true);  
	         key >>=1;
	       }
	   }
	   /*
	    *设置方块的大小,改变窗体大小时调用可自动调整方块到合适的尺寸
	    */
	   
	   public void fanning()
	   {
	    boxWidth = getSize().width / RussiaBlock.COLS;
	    boxHeight = getSize().height /RussiaBlock.ROWS;
	    isTiled=true;
	   }
	  }
	  
	   /*
	    * 内部类 键盘键听器,响应键盘事件
	    */
	   class ControlKeyListener extends KeyAdapter{
			public void keyPressed(KeyEvent ke) 
			  {
			   if (!game.isPlaying()) return;
			      
			   RussiaBlock block = game.getCurBlock();
			   switch (ke.getKeyCode()) {
			    case KeyEvent.VK_DOWN:
			     block.moveDown();
			     break;
			    case KeyEvent.VK_LEFT:
			     block.moveLeft();
			     break;
			    case KeyEvent.VK_RIGHT:
			     block.moveRight();
			     break;
			    case KeyEvent.VK_UP:
			     block.turnNext();
			     break;
			    case KeyEvent.VK_SPACE://一键到底
			      while(block.moveDown())
			      {  
			      }
			      break;
			    default:
			     break;
			   }
			  }
		}
 }

