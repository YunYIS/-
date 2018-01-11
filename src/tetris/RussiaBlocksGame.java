package tetris;

/**
 * ����Ϸ��
 * @author ������
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
	
	 public final static int PER_LINE_SCORE = 100; //��ȥһ�е÷�
	 public final static int PER_LEVEL_SCORE = PER_LINE_SCORE*20; //��һ����Ҫ�ķ���
	 public final static int DEFAULT_LEVEL = 5; //Ĭ�ϼ���
	 public final static int MAX_LEVEL = 10; //��߼���
	 private int score=0,curLevelScore = 0; //�ֺܷͱ����÷�
	 
	 private GameCanvas canvas;
	 private ControlPanel controlPanel;
	 private RussiaBlock block;
	 
	 private int style = 0;
	 boolean playing = false;
	 
	 private JMenuBar bar;  //�˵���
	 private JMenu gameMenu,controlMenu,windowStyleMenu,informationMenu; //�˵�������������˵���
	 private JMenuItem newGameItem,setBlockColorItem,setBgColorItem, turnHardItem,turnEasyItem,exitItem; //gameMenu�����˵���
	 private JMenuItem playItem,pauseItem,resumeItem,stopItem; //controlMenu�����˵���
	 private JRadioButtonMenuItem windowsRadioItem,motifRadioItem,metalRadioItem; //��ѡ��ť�˵���
	 private JMenuItem authorItem,helpItem; //informationMenu�����˵���
	 private ButtonGroup buttonGroup;//���
	 
	  /*
	   * ���캯��
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
	   
	   canvas = GameCanvas.getCanvasInstance();//static ��ȡGameCanvas����ʵ��
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
	   
	   //���������С�仯
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
	   * �ж���Ϸ�Ƿ����ڽ���
	   */
	   public boolean isPlaying()
	   {
	    return playing;
	   }
	   /*
	    * ��ʼ��Ϸ�����ð�ť�Ͳ˵���Ŀ�����
	    */ 
	   public void playGame()
	   {
	    play();
	    controlPanel.setPlayButtonEnabled(false);
	    playItem.setEnabled(false);
	   }
	   /*
	    * ��ͣ��Ϸ
	    */
	   public void pauseGame()
	   {
	     if(block != null) block.pauseMove();
	     controlPanel.setPauseButtonLabel(false);
	     pauseItem.setEnabled(false);
	     resumeItem.setEnabled(true);
	   }
	   /*
	    * ����ͣ�лָ���Ϸ
	    */
	   public void resumeGame()
	   {
	    if(block != null) block.resumeMove();
	    controlPanel.setPauseButtonLabel(true);
	    pauseItem.setEnabled(true);
	    resumeItem.setEnabled(false);
	   }
	   /*
	    * ֹͣ��Ϸ
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
	    * �õ���ǰ����
	    */
	   public int  getLevel()
	   {
	    return controlPanel.getLevel();
	   }
	   /*
	    * ���õ�ǰ����,�����¿���������ʾ
	    */
	   public void setLevel(int level)
	   {
	    if(level>0&&level<11)
	    controlPanel.setLevel(level);
	   }
	   /*
	    * �õ���ǰ�ܷ���
	    */
	   public int getScore()
	   {
	    if(canvas != null)
	        return score;
	     return 0;
	   }
	   /*
	    * �õ������÷�
	    */
	   public int getCurLevelScore()
	   {
	    if(canvas != null)
	        return curLevelScore;
	     return 0;
	   }
	   /*
	    * ���µȼ�
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
	    * ��õ�ǰ�÷���
	    */
	   public RussiaBlock getCurBlock() {
	     return block;
	  }
	   /*
	    * ��ʼ��Ϸ
	    */
	   private void play()
	   {
	    playing=true;
	    Thread thread = new Thread(new Game());
	    thread.start();
	    reset();
	   }
	   /*
	     * ����
	     */
	     private void reset()
	     {
	      controlPanel.reset();
	      canvas.reset();
	                score = 0;
	                curLevelScore = 0;
	     }
	    /*
	     * ������Ϸ����
	     */ 
	   private void reportGameOver()
	   {
	    JOptionPane.showMessageDialog(this,"��Ϸ����!");
	   }
	   /*
	    * ���������˵�
	    */
	  private void createMenu()
	  {
	   gameMenu = new JMenu("��Ϸ");
	   newGameItem = new JMenuItem("����Ϸ");
	   setBlockColorItem = new JMenuItem("������ɫ");
	   setBgColorItem = new JMenuItem("������ɫ");
	   turnHardItem = new JMenuItem("�Ѷ�����");
	   turnEasyItem = new JMenuItem("�ѶȽ���");
	   exitItem = new JMenuItem("�˳�");
	   gameMenu.add(newGameItem);
	   gameMenu.add(setBlockColorItem);
	   gameMenu.add(setBgColorItem);
	    gameMenu.add(turnHardItem);
	    gameMenu.add(turnEasyItem);
	    gameMenu.add(exitItem);   
	    
	   controlMenu =  new JMenu("����");
	    playItem = new JMenuItem("��ʼ");
	    pauseItem = new JMenuItem("��ͣ");
	    resumeItem = new JMenuItem("����");
	    stopItem = new JMenuItem("����");
	    controlMenu.add(playItem);
	    controlMenu.add(pauseItem);
	    controlMenu.add(resumeItem);
	    controlMenu.add(stopItem);
	    
	    windowStyleMenu = new JMenu("���");
	    buttonGroup = new ButtonGroup();//���ѡ
	    windowsRadioItem = new JRadioButtonMenuItem("Windows");
	    motifRadioItem = new JRadioButtonMenuItem("Motif");
	    metalRadioItem = new JRadioButtonMenuItem("Mentel",true);
	    windowStyleMenu.add(windowsRadioItem);
	    buttonGroup.add(windowsRadioItem);
	    windowStyleMenu.add(motifRadioItem);
	    buttonGroup.add(motifRadioItem);
	    windowStyleMenu.add(metalRadioItem);
	    buttonGroup.add(metalRadioItem);
	    
	    informationMenu = new JMenu("��Ϣ");
	    authorItem = new JMenuItem("Author:������");
	    helpItem = new JMenuItem("����");
	    informationMenu.add(authorItem);
	    informationMenu.add(helpItem);
	    
	    bar = new JMenuBar();
	    bar.add(gameMenu);
	   bar.add(controlMenu);
	   bar.add(windowStyleMenu);
	   bar.add(informationMenu);
	   
	   addActionListenerToMenu();//��Ӳ˵���Ӧ
	   setJMenuBar(bar); //���ô˴���Ĳ˵���
	  }
	  /*
	   * ��Ӳ˵���Ӧ
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
		    * Java�Դ���ѡɫ���� ������component(this), "����", initialColor��;
		    */
	    Color newBlockColor =
	            JColorChooser.showDialog(RussiaBlocksGame.this,
	                    "���÷�����ɫ", canvas.getBlockColor());
	    if (newBlockColor != null)
	     canvas.setBlockColor(newBlockColor);
	   }
	  });
	  
	  setBgColorItem.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent ae) {
	    Color newBgColor =
	            JColorChooser.showDialog(RussiaBlocksGame.this,"���ñ�����ɫ", 
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
	    * �趨���ڷ��
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
	   * ��Ϸ�̵߳�run����
	   */
	  public void run()
	  {
	   int col=(int)(Math.random()*(canvas.getCols()-3));//�����
	   style=RussiaBlock.STYLES[(int)(Math.random()*RussiaBlock.BLOCK_KIND_NUMBER)][(int)(Math.random()*RussiaBlock.BLOCK_STATUS_NUMBER)];
	    
	         while (playing) {
	    if (block != null) {    //��һ��ѭ��ʱ��blockΪ��
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
	    
	    if (isGameOver()) {     //�����Ϸ�Ƿ�Ӧ�ý�����
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
	  * �ж��Ƿ�����ȥ����
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
	   * �ж���Ϸ�Ƿ����
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
	   new RussiaBlocksGame("����˹����");
	 }
}
