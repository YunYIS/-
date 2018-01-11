package tetris;
/**
 * ���������
 * @author ������
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
	 private TipBlockPanel tipBlockPanel;//Ԥ�Է�����ʾ��
	 private JPanel tipPanel,InfoPanel,buttonPanel;
	 private final JTextField levelField,scoreField; //���б༭�ı�
	 private JButton playButton,pauseButton,stopButton,
	                 turnHarderButton,turnEasilyButton;
	 
	  private EtchedBorder border=new EtchedBorder(EtchedBorder.RAISED,Color.WHITE, new Color(148, 145, 140)) ;
	  
	  private RussiaBlocksGame game;
	  private Timer timer;
	   
	 public ControlPanel(final RussiaBlocksGame game)
	 {
	  this.game = game;
	  /*
	   *ͼ�ν��沿��
	  */
	  setLayout(new GridLayout(3,1,0,4));
	  
	  tipBlockPanel = new TipBlockPanel();
	  tipPanel = new JPanel( new BorderLayout() );
	  tipPanel.add( new JLabel("��һ��:") , BorderLayout.NORTH );
	  tipPanel.add( tipBlockPanel , BorderLayout.CENTER );
	  tipPanel.setBorder(border);
	  
	  InfoPanel = new JPanel( new GridLayout(4,1,0,0) );//���񲼾�
	  levelField = new JTextField(""+RussiaBlocksGame.DEFAULT_LEVEL);
	  levelField.setEditable(false);
	  scoreField = new JTextField("0");
	  scoreField.setEditable(false);
	  InfoPanel.add(new JLabel("�Ѷ�ϵ��:"));
	  InfoPanel.add(levelField);
	  InfoPanel.add(new JLabel("����:"));
	  InfoPanel.add(scoreField);
	  InfoPanel.setBorder(border);
	  
	  buttonPanel = new JPanel(new GridLayout(5,1,0,0));
	  playButton = new JButton("��ʼ");
	  pauseButton = new JButton("��ͣ");
	  stopButton = new JButton("����");
	  turnHarderButton = new JButton("�Ѷ�����");
	  turnEasilyButton = new JButton("�ѶȽ���");
	 
	  buttonPanel.add(playButton);
	  buttonPanel.add(pauseButton);
	  buttonPanel.add(stopButton);
	  buttonPanel.add(turnHarderButton);
	  buttonPanel.add(turnEasilyButton);
	  buttonPanel.setBorder(border);
	   
	   addKeyListener(new ControlKeyListener());//���
	   
	   add(tipPanel);
	   add(InfoPanel);
	   add(buttonPanel);
	 /*
	  *����¼�������
	  */ 
	   playButton.addActionListener(
	   new ActionListener()
	   {
	    public void actionPerformed(ActionEvent event)
	    {
	     game.playGame();
	     requestFocus();//��ControlPanel���»�ý�������Ӧ�����¼�
	    }
	    });
	    
	    pauseButton.addActionListener(
	    new ActionListener()
	    {
	     public void actionPerformed(ActionEvent event)
	     {
	      if(pauseButton.getText().equals("��ͣ"))
	        game.pauseGame();
	      else
	        game.resumeGame(); 
	      requestFocus();//��ControlPanel���»�ý�������Ӧ�����¼�
	     }
	    }
	    );
	    stopButton.addActionListener(
	    new ActionListener()
	    {
	     public void actionPerformed(ActionEvent event)
	     {
	      game.stopGame();
	      requestFocus();//��ControlPanel���»�ý�������Ӧ�����¼�
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
	       requestFocus();//��ControlPanel���»�ý�������Ӧ�����¼�
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
	      requestFocus();//��ControlPanel���»�ý�������Ӧ�����¼�
	     }
	   });
	    /*
	    *  ʱ����������ÿ��500�����level,scoreֵ���и���
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
	  * ����Ԥ�Է������ʽ
	  */
	 public void setBlockStyle(int style)
	 {
	  tipBlockPanel.setStyle(style);
	  tipBlockPanel.repaint();
	 }  
	 /*
	  * ���ã����������ݻָ������ֵ
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
	  *����playButton�Ƿ����
	  */
	 public void setPlayButtonEnabled(boolean enable)
	 {
	  playButton.setEnabled(enable);
	 }
	 
	 /*
	  *����pauseButton���ı�
	  */
	 public void setPauseButtonLabel(boolean pause)
	 {
	  pauseButton.setText( pause ? "��ͣ" : "����" );
	 } 
	 
	 /*
	  *���÷���Ĵ�С,�ı䴰���Сʱ���ÿ��Զ��������鵽���ʵĳߴ�
	  */
	 public void fanning()
	 {
	  tipBlockPanel.fanning();
	 }   
	 /*
	  *����level�ı����ֵ���ص�ǰ�ļ���
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
	   * ����level�ı����ֵ
	   */
	  public void setLevel(int level)
	  {
	    if(level > 0 && level <= RussiaBlocksGame.MAX_LEVEL)
	       levelField.setText("" + level);
	  }   
	  /*
	   * �ڲ��� ΪԤ�Է��飨��һ�飩����ʾ����
	   */
	  private class TipBlockPanel extends JPanel
	  {
	    private Color bgColor = Color.darkGray, 
	                  blockColor = Color.lightGray;              
	    private RussiaBox [][]boxes = new RussiaBox[RussiaBlock.ROWS][RussiaBlock.COLS];
	    private int boxWidth, boxHeight,style;
	    private boolean isTiled = false; //һ�ε���
	    
	    /*
	     * ���캯��
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
	    * ���캯��
	    */
	   @SuppressWarnings("unused")
	public TipBlockPanel(Color bgColor, Color blockColor)
	   {
	    this();
	    this.bgColor = bgColor;
	    this.blockColor = blockColor;
	   }
	    /*
	     * ���÷���ķ��
	     */
	    public void setStyle(int style)
	    {
	     this.style = style;
	     repaint();
	    }
	    
	    /*
	     * ����Ԥ�Է���
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
	    *���÷���Ĵ�С,�ı䴰���Сʱ���ÿ��Զ��������鵽���ʵĳߴ�
	    */
	   
	   public void fanning()
	   {
	    boxWidth = getSize().width / RussiaBlock.COLS;
	    boxHeight = getSize().height /RussiaBlock.ROWS;
	    isTiled=true;
	   }
	  }
	  
	   /*
	    * �ڲ��� ���̼�����,��Ӧ�����¼�
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
			    case KeyEvent.VK_SPACE://һ������
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

