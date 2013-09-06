/*
姓名：賴冠學
系級：資工一乙
學號：499262570
*/
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.*;

public class lotterygui extends JFrame
{
	private int maxNumber = 10000;
	private int miniNumber = 1;

	private DescriptionPanel descriptionPanel = new DescriptionPanel();
	private JLabel jlMaxNumber = new JLabel("最大");
	private JTextField jtfMax = new JTextField(3);
	private JLabel jlMiniNumber = new JLabel("最小");
	private JTextField jtfMini = new JTextField(3);
	
	private int lotteryNumberIndex = 0;
	private int[][] lotteryNumber = new int[5000][5];

	private int notFromUserCount = 0;
	private int notFromUserCountWrong = 0;

	private JTextField jtfUserNumber1 = new JTextField(3);
	private JTextField jtfUserNumber2 = new JTextField(3);
	private JTextField jtfUserNumber3 = new JTextField(3);
	private JTextField jtfUserNumber4 = new JTextField(3);
	private JTextField jtfUserNumber5 = new JTextField(3);
	private JButton jbEnterLotteryNumber = new JButton("投注");

	private JLabel jlFile = new JLabel("檔案路徑及名稱");
	private JTextField jtfFile = new JTextField(9);
	private JButton jbFile = new JButton("投注");

	private JLabel lottery = new JLabel("中獎號碼");
	private JTextField L1 = new JTextField(3);
	private JTextField L2 = new JTextField(3);
	private JTextField L3 = new JTextField(3);
	private JTextField L4 = new JTextField(3);
	private JTextField L5 = new JTextField(3);
	private JButton jbCheckWinNumber = new JButton("檢查中獎號碼");

	private JButton Start = new JButton("開獎");
	private JButton Renew = new JButton("重玩");	

	private int[] repeatCheckArray=new int[10000];
	
	private int fileNumberIndex=1;
	
	private boolean notFromUser;
		
	private String[] allNumbersInArray=new String[maxNumber];

	public void cleanup()
	{
		maxNumber = 10000;
		miniNumber = 1;
		lotteryNumberIndex=0;
		fileNumberIndex=1;
		for(int i=0;i<5;i++)
		{
			lotteryNumber[0][i]=0;
		}
		jtfMax.setText("");
		jtfMini.setText("");
	
		jtfUserNumber1.setText("");
		jtfUserNumber2.setText("");
		jtfUserNumber3.setText("");
		jtfUserNumber4.setText("");
		jtfUserNumber5.setText("");

		jtfFile.setText("");

		L1.setText("");
		L2.setText("");
		L3.setText("");
		L4.setText("");
		L5.setText("");
		descriptionPanel.clear();
	}
	
	public void maxMiniCheck()
	{
		try
		{
			maxNumber = Integer.parseInt(jtfMax.getText());
		}
		catch(NumberFormatException ex)
		{
			maxNumber=10000;
		}

		try
		{
			miniNumber = Integer.parseInt(jtfMini.getText());
		}
		catch(NumberFormatException ex)
		{
			miniNumber=0;
		}

		if(maxNumber<0)
		{
			maxNumber=0;
		}
		if(miniNumber<0)
		{
			miniNumber=0;
		}
		if(maxNumber-miniNumber<=4)
		{
			maxNumber=10000;
			miniNumber=0;
		}
	}

	public void resetRepeatCheckArray()
	{
		maxMiniCheck();
		for(int i=0;i<maxNumber;i++)
		{
			int toString=i+1;
			allNumbersInArray[i]=Integer.toString(toString);			
		}

		for(int i=0;i<maxNumber;)
		{
			repeatCheckArray[i]=++i;
		}
	}

	public String reternNumberStringWithoutZero(String number)
	{
		maxMiniCheck();
		for(int i=0;i<maxNumber;i++)
		{
			int toString=i+1;
			allNumbersInArray[i]=Integer.toString(toString);			
		}

		while(number.startsWith(" ")||number.startsWith("0"))
		{
			if(number.startsWith(" "))
			{
				number=number.replaceFirst(" ","");
			}
			else
			{
				number=number.replaceFirst("0","");
			}
		}
		return number;
	}
	
	public boolean checkNumberString(String number)
	{
		maxMiniCheck();
		for(int i=0;i<maxNumber;i++)
		{
			int toString=i+1;
			allNumbersInArray[i]=Integer.toString(toString);			
		}
		return true;
	}
	
	public void write2Number(String numberString)
	{
		maxMiniCheck();
		for(int i=0;i<maxNumber;i++)
		{
			int toString=i+1;
			allNumbersInArray[i]=Integer.toString(toString);			
		}
		resetRepeatCheckArray();
		int[] userLotteryNumber=new int[5];
		boolean numberCheck=true;
		for(int i=0;i<5&&numberCheck;i++)
		{
			numberString=reternNumberStringWithoutZero(numberString);
			Scanner input=new Scanner(numberString);
			boolean allNumbersInArrayCheck=false;
			if(input.hasNext())
			{
				//抓取下注的第一個值
				String numberRemoveString=input.next();
				//檢查是否為數字
				for(int z=0;z<maxNumber&&allNumbersInArrayCheck==false;z++)
				{
					if(numberRemoveString.equals(allNumbersInArray[z]))
					{
						allNumbersInArrayCheck=true;
					}
				}
				if(allNumbersInArrayCheck)
				{
					//把字串值的下注號碼轉換成數字
					int number=Integer.parseInt(numberRemoveString);
					//從整行下注移除剛剛抓取的值
					numberString=numberString.replaceFirst(numberRemoveString,"");
					//檢查下注數字是否重複
					if(repeatCheckArray[--number]!=0)
					{
						userLotteryNumber[i]=++number;
						repeatCheckArray[--number]=0;
					}
					else
					{
						numberCheck=false;
					}
				}
				else
				{
					numberCheck=false;
				}
			}
			else
			{
				numberCheck=false;
			}
			//如果讀到第六個值後面還有值，則為無效下注
			if(i==5&&input.hasNext())
			{
				numberCheck=false;
			}
		}
		boolean addLotteryNumberIndex=true;
		for(int i=0;i<5&&numberCheck;i++)
		{
			lotteryNumber[lotteryNumberIndex][i]=userLotteryNumber[i];
			addLotteryNumberIndex=true;
		}
		if(numberCheck&&addLotteryNumberIndex)
		{
			if(notFromUser)
			{
				notFromUserCount++;
				lotteryNumberIndex++;
			}
			else
			{
				if(fileNumberIndex==0)
				{
					descriptionPanel.setDescription("中獎號碼\n");
				}
				lotteryNumberIndex++;
				for(int i=0;i<5&&numberCheck;i++)
				{
					String outputNumber = Integer.toString(userLotteryNumber[i]);
					descriptionPanel.setDescription(outputNumber);
					if(i!=4)
					{
						descriptionPanel.setDescription(", ");
					}
				}
				descriptionPanel.setDescription("\n\n");
			}
		}
		else
		{
			if(!notFromUser)
			{
				descriptionPanel.setDescription("無效的輸入！\n\n");
			}
			notFromUserCountWrong++;
		}
	}
	
	public void getWinLotteryNumber()
	{
		maxMiniCheck();
		for(int i=0;i<maxNumber;i++)
		{
			int toString=i+1;
			allNumbersInArray[i]=Integer.toString(toString);			
		}
		int[] winNumberCount=new int[3];
		for(int i=1;i<lotteryNumberIndex;i++)
		{
			int winNumberCheck=0;
			for(int j=0;j<5;j++)
			{
				for(int k=0;k<5;k++)
				{
					if(lotteryNumber[i][j]==lotteryNumber[0][k])
					{
						winNumberCheck++;
					}
				}
			}
			switch(winNumberCheck)
			{
				case 5:
					winNumberCount[0]++;
					break;
				case 4:
					winNumberCount[1]++;
					break;
				case 3:
					winNumberCount[2]++;
					break;
			}
		}
		double totalMoney=0;
		if(lotteryNumberIndex!=0)
		{
			totalMoney=(lotteryNumberIndex-1)*50;
		}
		descriptionPanel.setDescription("The winning lottery number is");
		if(lotteryNumber[0][0]!=0)
		{
			for(int i=0;i<5;i++)
			{
				String lotteryNumberString = Integer.toString(lotteryNumber[0][i]);
				descriptionPanel.setDescription(" ");
				descriptionPanel.setDescription(lotteryNumberString);
			}
			descriptionPanel.setDescription(".\n");
		}
		else
		{
			descriptionPanel.setDescription(" none!\n");
		}
		if(winNumberCount[0]!=0)
		{
			String number1 = Integer.toString(winNumberCount[0]);
			String number2 = Integer.toString((int)((totalMoney*0.4)/winNumberCount[0]));
			descriptionPanel.setDescription("There are ");
			descriptionPanel.setDescription(number1);
			descriptionPanel.setDescription(" bets winning the first prize, each of them can get ");
			descriptionPanel.setDescription(number2);
			descriptionPanel.setDescription(" dollars.\n");
		}
		else
		{
			descriptionPanel.setDescription("There are 0 bets winning the first prize, each of them can get 0.0 dollars.\n");			
		}
		if(winNumberCount[1]!=0)
		{
			String number1 = Integer.toString(winNumberCount[1]);
			String number2 = Integer.toString((int)((totalMoney*0.2)/winNumberCount[1]));
			descriptionPanel.setDescription("There are ");
			descriptionPanel.setDescription(number1);
			descriptionPanel.setDescription(" bets winning the second prize, each of them can get ");
			descriptionPanel.setDescription(number2);
			descriptionPanel.setDescription(" dollars.\n");
		}
		else
		{
			descriptionPanel.setDescription("There are 0 bets winning the second prize, each of them can get 0.0 dollars.\n");
		}
		if(winNumberCount[2]!=0)
		{
			String number1 = Integer.toString(winNumberCount[2]);
			String number2 = Integer.toString((int)((totalMoney*0.1)/winNumberCount[2]));
			descriptionPanel.setDescription("There are ");
			descriptionPanel.setDescription(number1);
			descriptionPanel.setDescription(" bets winning the third prize, each of them can get ");
			descriptionPanel.setDescription(number2);
			descriptionPanel.setDescription(" dollars.\n");
		}
		else
		{
			descriptionPanel.setDescription("There are 0 bets winning the third prize, each of them can get 0.0 dollars.\n");
		}
		if(winNumberCount[0]==winNumberCount[1]&&winNumberCount[1]==winNumberCount[2]&&winNumberCount[2]==0)
		{
			String number1 = Integer.toString((int)totalMoney);
			descriptionPanel.setDescription("There are ");
			descriptionPanel.setDescription(number1);
			descriptionPanel.setDescription(" dollars goes to philanthropies.\n\n");
		}
		else
		{
			if((winNumberCount[0]==winNumberCount[1]&&winNumberCount[1]==0)||(winNumberCount[1]==winNumberCount[2]&&winNumberCount[2]==0))
			{
				if(winNumberCount[0]==winNumberCount[1])
				{
					String number1 = Integer.toString((int)(totalMoney*0.9));
					descriptionPanel.setDescription("There are ");
					descriptionPanel.setDescription(number1);
					descriptionPanel.setDescription(" dollars goes to philanthropies.\n\n");
				}
				else
				{
					String number1 = Integer.toString((int)(totalMoney*0.6));					
					descriptionPanel.setDescription("There are ");
					descriptionPanel.setDescription(number1);
					descriptionPanel.setDescription(" dollars goes to philanthropies.\n\n");
				}
			}
			else
			{
				if(winNumberCount[0]==0||winNumberCount[1]==0||winNumberCount[2]==0)
				{
					if(winNumberCount[0]==0)
					{
						String number1 = Integer.toString((int)(totalMoney*0.7));					
						descriptionPanel.setDescription("There are ");
						descriptionPanel.setDescription(number1);
						descriptionPanel.setDescription(" dollars goes to philanthropies.\n\n");
					}
					if(winNumberCount[1]==0)
					{
						String number1 = Integer.toString((int)(totalMoney*0.5));					
						descriptionPanel.setDescription("There are ");
						descriptionPanel.setDescription(number1);
						descriptionPanel.setDescription(" dollars goes to philanthropies.\n\n");
					}
					if(winNumberCount[2]==0)
					{
						String number1 = Integer.toString((int)(totalMoney*0.4));					
						descriptionPanel.setDescription("There are ");
						descriptionPanel.setDescription(number1);
						descriptionPanel.setDescription(" dollars goes to philanthropies.\n\n");
					}
				}
				else
				{
					String number1 = Integer.toString((int)(totalMoney*0.3));					
					descriptionPanel.setDescription("There are ");
					descriptionPanel.setDescription(number1);
					descriptionPanel.setDescription(" dollars goes to philanthropies.\n\n");
				}
			}
		}

	}

	public lotterygui()
	{
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
		p1.add(jlMaxNumber);
		p1.add(jtfMax);
		p1.add(jlMiniNumber);
		p1.add(jtfMini);
		p1.setBorder(new TitledBorder("投注號碼限制"));
	    //中版面
    	JPanel p2 = new JPanel(new GridLayout(1,2, 5, 5));

		JPanel p2l = new JPanel(new GridLayout(2,1, 5, 5));

		JPanel p2u = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 4));

		p2u.add(jtfUserNumber1);
		p2u.add(jtfUserNumber2);
		p2u.add(jtfUserNumber3);
		p2u.add(jtfUserNumber4);
		p2u.add(jtfUserNumber5);
		p2u.add(jbEnterLotteryNumber);
		p2u.setBorder(new TitledBorder("單筆投注"));

		JPanel p2d = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 4));

		p2d.add(jlFile);
		p2d.add(jtfFile);
		p2d.add(jbFile);
		p2d.setBorder(new TitledBorder("批次投注"));
	
		JPanel p2c = new JPanel(new BorderLayout());
		p2c.setBorder(new TitledBorder("中獎結果"));
		String description = "";
		descriptionPanel.setDescription(description);

		p2c.add(descriptionPanel, BorderLayout.CENTER);
		p2l.add(p2u);
		p2l.add(p2d);
		p2.add(p2l);
		p2.add(p2c);

		//下版面
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
		JPanel p31 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));

		p31.add(lottery);
		p31.add(L1);
		p31.add(L2);
		p31.add(L3);
		p31.add(L4);
		p31.add(L5);
		p31.add(jbCheckWinNumber);

		p31.setBorder(new TitledBorder("中獎號碼"));

		p3.add(p31);
		p3.add(Start);
		p3.add(Renew);

		//版面設置
		setLayout(new BorderLayout( 5, 10));
		add(p1,BorderLayout.NORTH);
		add(p2,BorderLayout.CENTER);
		add(p3,BorderLayout.SOUTH);

		//觸發的地方
		jtfMax.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				maxNumber = Integer.parseInt(jtfMax.getText());
			}
		});
		jtfMini.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				miniNumber = Integer.parseInt(jtfMini.getText());
			}
		});
		
		jbEnterLotteryNumber.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				descriptionPanel.setDescription("單筆投注\n");
				descriptionPanel.setDescription("投注號碼為\n");
				notFromUser = false;
				
				String lotteryNumberCheck = jtfUserNumber1.getText();
				lotteryNumberCheck = lotteryNumberCheck.concat(" ");
				lotteryNumberCheck = lotteryNumberCheck.concat(jtfUserNumber2.getText());
				lotteryNumberCheck = lotteryNumberCheck.concat(" ");
				lotteryNumberCheck = lotteryNumberCheck.concat(jtfUserNumber3.getText());
				lotteryNumberCheck = lotteryNumberCheck.concat(" ");
				lotteryNumberCheck = lotteryNumberCheck.concat(jtfUserNumber4.getText());
				lotteryNumberCheck = lotteryNumberCheck.concat(" ");
				lotteryNumberCheck = lotteryNumberCheck.concat(jtfUserNumber5.getText());
				write2Number(lotteryNumberCheck);
			}
		});
				
		jbCheckWinNumber.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				notFromUser = false;
				String numberCheck = L1.getText();
				numberCheck = numberCheck.concat(" ");
				numberCheck = numberCheck.concat(L2.getText());
				numberCheck = numberCheck.concat(" ");
				numberCheck = numberCheck.concat(L3.getText());
				numberCheck = numberCheck.concat(" ");
				numberCheck = numberCheck.concat(L4.getText());
				numberCheck = numberCheck.concat(" ");
				numberCheck = numberCheck.concat(L5.getText());
				fileNumberIndex=0;
				write2Number(numberCheck);
				fileNumberIndex=1;
			}
		});
		
		jbFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				descriptionPanel.setDescription("批次投注\n");
				notFromUser=true;
				notFromUserCount=0;
				notFromUserCountWrong=0;
				maxMiniCheck();
				for(int i=0;i<maxNumber;i++)
				{
					int toString=i+1;
					allNumbersInArray[i]=Integer.toString(toString);			
				}
				String file = jtfFile.getText();
				try
				{
					Scanner userFileName = new Scanner(new File(file));
					do
					{
						String numberString=userFileName.nextLine();
						write2Number(numberString);
					}while(userFileName.hasNext());
					userFileName.close();
					
					descriptionPanel.setDescription("檔案 ");
					descriptionPanel.setDescription(file);
					descriptionPanel.setDescription(" 讀取成功。\n");
					descriptionPanel.setDescription("共 ");
					String number1 = Integer.toString(notFromUserCount);
					descriptionPanel.setDescription(number1);
					descriptionPanel.setDescription(" 筆資料加入成功。\n\n");
					if(notFromUserCountWrong!=0)
					{
						descriptionPanel.setDescription("共 ");
						String number2 = Integer.toString(notFromUserCountWrong);
						descriptionPanel.setDescription(number2);
						descriptionPanel.setDescription(" 筆資料加入失敗。\n\n");
					}
				}
				catch(FileNotFoundException ex)
				{
					descriptionPanel.setDescription("檔案不存在！\n\n");
				}
			}
		});

		Start.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				getWinLotteryNumber();
			}
		});
		Renew.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cleanup();
			}
		});
	}

	public static void main(String[] args) throws Exception
	{
		JFrame frame = new lotterygui();
		frame.setTitle("樂透大考驗");
		frame.setSize(700, 600);
		frame.setLocationRelativeTo(null); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}