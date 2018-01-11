package tetris;

/**
 * 主游戏类
 * @author 张云天
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


@SuppressWarnings("serial")
public class RussiaBlocksGame  extends JFrame{
	
	 public final static int PER_LINE_SCORE = 100; //消去一行得分
	 public final static int PER_LEVEL_SCORE = PER_LINE_SCORE*20; //升一级需要的分数
	 public final static int DEFAULT_LEVEL = 5; //默认级别
	 public final static int MAX_LEVEL = 10; //最高级别
	 private int score=0,curLevelScore = 0; //总分和本级得分
	 
	 private GameCanvas canvas;
	 private ControlPanel controlPanel;
	 private RussiaBlock block;
	 
	 private int style = 0;
	 boolean playing = false;
	 
	 private JMenuBar bar;  //菜单栏
	 private JMenu gameMenu,controlMenu,windowStyleMenu,informationMenu; //菜单；将其添加至菜单栏
	 private JMenuItem newGameItem,setBlockColorItem,setBgColorItem, turnHardItem,turnEasyItem,exitItem; //gameMenu下拉菜单项
	 private JMenuItem playItem,pauseItem,resumeItem,stopItem; //controlMenu下拉菜单项
	 private JRadioButtonMenuItem windowsRadioItem,motifRadioItem,metalRadioItem; //单选按钮菜单项
	 private JMenuItem authorItem,helpItem; //informationMenu下拉菜单项
	 private ButtonGroup buttonGroup;//多斥
	 
	  /*
	   * 构造函数
	   */
	  public RussiaBlocksGame(String title)
	  {
	   super(title);
	   
	   setSize(300,400);
	   Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
	   setLocation((scrSize.width-getSize().width)/2,(scrSize.height-getSize().height)/2);
	   
	   createMenu();
	   Container container=getContentPane();
	   container.setLayout(new BorderLayout());
	   
	   canvas = GameCanvas.getCanvasInstance();//static 获取GameCanvas函数实例
	   controlPanel = new ControlPanel(this);
	   
	   container.add(canvas,BorderLayout.CENTER);
	   container.add(controlPanel,BorderLayout.EAST);
	   
	   addWindowListener(
	   new WindowAdapter()
	   {
	    public void windowClosing(WindowEvent event)
	    {
	     stopGame();
	     System.exit(0);
	    }
	   }
	   );
	   
	   //监听组件大小变化
	   addComponentListener(
	   new ComponentAdapter()
	   {
	    public void componentResized(ComponentEvent event)
	    {
	     canvas.fanning();
	    }
	   }
	   );
	   canvas.fanning();
	   setVisible(true);
	  }
	  /*
	   * 判断游戏是否正在进行
	   */
	   public boolean isPlaying()
	   {
	    return playing;
	   }
	   /*
	    * 开始游戏并设置按钮和菜单项的可用性
	    */ 
	   public void playGame()
	   {
	    play();
	    controlPanel.setPlayButtonEnabled(false);
	    playItem.setEnabled(false);
	   }
	   /*
	    * 暂停游戏
	    */
	   public void pauseGame()
	   {
	     if(block != null) block.pauseMove();
	     controlPanel.setPauseButtonLabel(false);
	     pauseItem.setEnabled(false);
	     resumeItem.setEnabled(true);
	   }
	   /*
	    * 从暂停中恢复游戏
	    */
	   public void resumeGame()
	   {
	    if(block != null) block.resumeMove();
	    controlPanel.setPauseButtonLabel(true);
	    pauseItem.setEnabled(true);
	    resumeItem.setEnabled(false);
	   }
	   /*
	    * 停止游戏
	    */
	   public void stopGame()
	   {
	    if(block != null) block.stopMove();
	    playing = false;
	    controlPanel.setPlayButtonEnabled(true);
	    controlPanel.setPauseButtonLabel(true);
	                playItem.setEnabled(true);
	   }
	   /*
	    * 得到当前级别
	    */
	   public int  getLevel()
	   {
	    return controlPanel.getLevel();
	   }
	   /*
	    * 设置当前级别,并更新控制面板的显示
	    */
	   public void setLevel(int level)
	   {
	    if(level>0&&level<11)
	    controlPanel.setLevel(level);
	   }
	   /*
	    * 得到当前总分数
	    */
	   public int getScore()
	   {
	    if(canvas != null)
	        return score;
	     return 0;
	   }
	   /*
	    * 得到本级得分
	    */
	   public int getCurLevelScore()
	   {
	    if(canvas != null)
	        return curLevelScore;
	     return 0;
	   }
	   /*
	    * 更新等级
	    */
	   public void levelUpdate()
	   {
	    int curLevel = getLevel();
	    if(curLevel < MAX_LEVEL && curLevelScore >= PER_LEVEL_SCORE)
	    {
	      setLevel(curLevel + 1);
	      curLevelScore -= PER_LEVEL_SCORE;
	    }   
	   }
	   /*
	    * 获得当前得方块
	    */
	   public RussiaBlock getCurBlock() {
	     return block;
	  }
	   /*
	    * 开始游戏
	    */
	   private void play()
	   {
	    playing=true;
	    Thread thread = new Thread(new Game());
	    thread.start();
	    reset();
	   }
	   /*
	     * 重置
	     */
	     private void reset()
	     {
	      controlPanel.reset();
	      canvas.reset();
	                score = 0;
	                curLevelScore = 0;
	     }
	    /*
	     * 宣告游戏结束
	     */ 
	   private void reportGameOver()
	   {
	    JOptionPane.showMessageDialog(this,"游戏结束!");
	   }
	   /*
	    * 创建顶部菜单
	    */
	  private void createMenu()
	  {
	   gameMenu = new JMenu("游戏");
	   newGameItem = new JMenuItem("新游戏");
	   setBlockColorItem = new JMenuItem("方块颜色");
	   setBgColorItem = new JMenuItem("背景颜色");
	   turnHardItem = new JMenuItem("难度增加");
	   turnEasyItem = new JMenuItem("难度降低");
	   exitItem = new JMenuItem("退出");
	   gameMenu.add(newGameItem);
	   gameMenu.add(setBlockColorItem);
	   gameMenu.add(setBgColorItem);
	    gameMenu.add(turnHardItem);
	    gameMenu.add(turnEasyItem);
	    gameMenu.add(exitItem);   
	    
	   controlMenu =  new JMenu("控制");
	    playItem = new JMenuItem("开始");
	    pauseItem = new JMenuItem("暂停");
	    resumeItem = new JMenuItem("继续");
	    stopItem = new JMenuItem("结束");
	    controlMenu.add(playItem);
	    controlMenu.add(pauseItem);
	    controlMenu.add(resumeItem);
	    controlMenu.add(stopItem);
	    
	    windowStyleMenu = new JMenu("风格");
	    buttonGroup = new ButtonGroup();//风格单选
	    windowsRadioItem = new JRadioButtonMenuItem("Windows");
	    motifRadioItem = new JRadioButtonMenuItem("Motif");
	    metalRadioItem = new JRadioButtonMenuItem("Mentel",true);
	    windowStyleMenu.add(windowsRadioItem);
	    buttonGroup.add(windowsRadioItem);
	    windowStyleMenu.add(motifRadioItem);
	    buttonGroup.add(motifRadioItem);
	    windowStyleMenu.add(metalRadioItem);
	    buttonGroup.add(metalRadioItem);
	    
	    informationMenu = new JMenu("信息");
	    authorItem = new JMenuItem("Author:张云天");
	    helpItem = new JMenuItem("帮助");
	    informationMenu.add(authorItem);
	    informationMenu.add(helpItem);
	    
	    bar = new JMenuBar();
	    bar.add(gameMenu);
	   bar.add(controlMenu);
	   bar.add(windowStyleMenu);
	   bar.add(informationMenu);
	   
	   addActionListenerToMenu();//添加菜单响应
	   setJMenuBar(bar); //设置此窗体的菜单栏
	  }
	  /*
	   * 添加菜单响应
	   */
	  private void addActionListenerToMenu()
	  {
	   newGameItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    stopGame();
	    reset();
	    setLevel(DEFAULT_LEVEL);
	   }
	  });
	  
	  setBlockColorItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
		   /*
		    * Java自带的选色器， 参数（component(this), "标题", initialColor）;
		    */
	    Color newBlockColor =
	            JColorChooser.showDialog(RussiaBlocksGame.this,
	                    "设置方块颜色", canvas.getBlockColor());
	    if (newBlockColor != null)
	     canvas.setBlockColor(newBlockColor);
	   }
	  });
	  
	  setBgColorItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    Color newBgColor =
	            JColorChooser.showDialog(RussiaBlocksGame.this,"设置背景颜色", 
	                               canvas.getBgColor());
	    if (newBgColor != null)
	     canvas.setBgColor(newBgColor);
	   }
	  });
	  
	  turnHardItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    int curLevel = getLevel();
	    if (curLevel < MAX_LEVEL) setLevel(curLevel + 1);
	   }
	  });
	  
	  turnEasyItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    int curLevel = getLevel();
	    if (curLevel > 1) setLevel(curLevel - 1);
	   }
	  });
	  
	  exitItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    System.exit(0);
	   }
	  });
	  playItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    playGame();
	   }
	  });
	  
	  pauseItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    pauseGame();
	   }
	  });
	  
	  resumeItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    resumeGame();
	   }
	  });
	  
	  stopItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    stopGame();
	   }
	  });
	  
	  windowsRadioItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    String plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	    setWindowStyle(plaf);
	    canvas.fanning();
	    controlPanel.fanning();
	   }
	  });
	  
	  motifRadioItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    String plaf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	    setWindowStyle(plaf);
	    canvas.fanning();
	    controlPanel.fanning();;
	   }
	  });
	  
	   metalRadioItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    String plaf = "javax.swing.plaf.metal.MetalLookAndFeel";
	    setWindowStyle(plaf);
	    canvas.fanning();
	    controlPanel.fanning();
	   }
	  });
	  
	   }
	   /*
	    * 设定窗口风格
	    */
	   private void setWindowStyle(String plaf) 
	   {
	  try {
	   UIManager.setLookAndFeel(plaf);
	   SwingUtilities.updateComponentTreeUI(this);
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	 }
	   
	 private class Game implements Runnable
	 {
	  /*
	   *  (non-Javadoc)
	   * @see java.lang.Runnable#run()
	   * 游戏线程的run函数
	   */
	  public void run()
	  {
	   int col=(int)(Math.random()*(canvas.getCols()-3));//列随机
	   style=RussiaBlock.STYLES[(int)(Math.random()*RussiaBlock.BLOCK_KIND_NUMBER)][(int)(Math.random()*RussiaBlock.BLOCK_STATUS_NUMBER)];
	    
	         while (playing) {
	    if (block != null) {    //第一次循环时，block为空
	     if (block.isAlive()) {
	      try {
	       Thread.sleep(100);
	      } catch (InterruptedException ie) {
	       ie.printStackTrace();
	      }
	      continue;
	     }
	    }
	    
	    checkFullLine();
	    
	    if (isGameOver()) {     //检查游戏是否应该结束了
	     playItem.setEnabled(true);
	     pauseItem.setEnabled(true);
	     resumeItem.setEnabled(false);
	     controlPanel.setPlayButtonEnabled(true);
	     controlPanel.setPauseButtonLabel(true);
	     reportGameOver();
	     return;
	  }
	  block = new RussiaBlock(-1, col, getLevel(),style);
	  block.start();//run()
	  
	  col=(int)(Math.random()*(canvas.getCols()-3));
	   style=RussiaBlock.STYLES[(int)(Math.random()*RussiaBlock.BLOCK_KIND_NUMBER)][(int)(Math.random()*RussiaBlock.BLOCK_STATUS_NUMBER)];
	    controlPanel.setBlockStyle(style);
	 }
	 }
	 /*
	  * 判断是否能消去整行
	  */
	  public void checkFullLine() 
	  {
	   for (int i = 0; i < canvas.getRows(); i++) {
	    int row = -1;
	    boolean fullLineColorBox = true;
	    for (int j = 0; j < canvas.getCols(); j++) {
	     if (!canvas.getBox(i, j).isColorBox()) {
	      fullLineColorBox = false;
	      break;
	     }
	    }
	    if (fullLineColorBox) {
	     curLevelScore += PER_LINE_SCORE;
	     score += PER_LINE_SCORE;
	     row = i--;
	     canvas.removeLine(row);
	    }
	   }
	  }
	  /*
	   * 判断游戏是否结束
	   */
	  private boolean isGameOver() {
	   for (int i = 0; i < canvas.getCols(); i++) {
	    RussiaBox box = canvas.getBox(0, i);
	    if (box.isColorBox()) return true;
	   }
	   return false;
	  }
	 }
	 public static void main(String[] args) 
	 {
	   new RussiaBlocksGame("俄罗斯方块");
	 }
}
