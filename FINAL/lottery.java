/*
姓名：賴冠學
系級：資工一乙
學號：499262570
*/
import java.util.*;
import java.lang.*;
import java.io.*;
public class lottery
{
	public static void main(String[] args) throws Exception
	{
		for(int allTimeRun=1;allTimeRun!=0;allTimeRun++)
		{
			boolean machineEnd=false;
			lotteryMachine machine=new lotteryMachine();
			while(machineEnd==false)
			{
				System.out.print("f. lottery numbers from files\n" +
								 "u. lottery numbers from user\n" +
								 "r. run\n" +
								 "e. exit\n");
				Scanner input=new Scanner(System.in);
				String userInput = input.next();
				while(userInput.length()>1)
				{
					System.out.println("Oh! You shouldn't see me! Try again.");
					userInput = input.next();
				}
				userInput = userInput.toLowerCase();
				char switchCheck = userInput.charAt(0);
				switch(switchCheck)
				{
					case 'f':
						Scanner userInputFile=new Scanner(System.in);
						System.out.println("Please enter the lottery file. Empty input means done");
						boolean sentNumber;
						boolean checkEnter=false;
						machine.notFromUser=true;
						while(checkEnter==false)
						{
							String userInputFileName=userInputFile.nextLine();
							if(userInputFileName.equals(""))
							{
								checkEnter=true;
							}
							else
							{
								if(userInputFileName.endsWith(".txt")==false)
								{
									userInputFileName=userInputFileName.concat(".txt");
								}
								sentNumber=machine.getNewNumberFile(userInputFileName);	
								if(sentNumber)
								{
									userInputFileName=userInputFileName.replace(".txt","");
									System.out.println("The lottery numbers in "+userInputFileName+" have been loaded.");
								}
								else
								{
									userInputFileName=userInputFileName.replace(".txt","");
									System.out.println("Sorry, the file "+userInputFileName+" does not exist, please enter another lottery file.");
								}
							}
						}
						break;
					case 'u':
						Scanner userInputUser=new Scanner(System.in);
						System.out.println("Please enter a lottery number. Empty input means done");
						checkEnter=false;
						machine.notFromUser=false;
						while(checkEnter==false)
						{
							String userInputString=userInputUser.nextLine();
							if(userInputString.equals(""))
							{
								checkEnter=true;
							}
							else
							{
								machine.write2Number(userInputString);
							}
						}
						break;
					case 'r':
						machine.getWinLotteryNumber();
						machineEnd=true;
						break;
					case 'e':
						System.out.println("Good-Bye.");
						System.exit(0);
					default:
						System.out.println("Oh! You shouldn't see me! Try again.");
				}
			}
		}
	}
}
class lotteryMachine
{
	lotteryMachine()
	{
		for(int i=0;i<50;i++)
		{
			int toString=i+1;
			allNumbersInArray[i]=Integer.toString(toString);			
		}
	}

	int[] repeatCheckArray=new int[50];

	int lotteryNumberIndex=1;
	
	int fileNumberIndex=0;

	int[][] lotteryNumber=new int[5000][6];
	
	boolean notFromUser;
		
	String[] allNumbersInArray=new String[50];
	
	public void resetRepeatCheckArray()
	{
		for(int i=0;i<50;)
		{
			repeatCheckArray[i]=++i;
		}
	}

	public String reternNumberStringWithoutZero(String number)
	{
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
		return true;
	}
	
	public boolean getNewNumberFile(String fileName) throws Exception
	{
		File file=new File(fileName);
		if(file.exists())
		{
			Scanner userFileName=new Scanner(file);
			do
			{
				String numberString=userFileName.nextLine();
				write2Number(numberString);
				fileNumberIndex++;
			}while(userFileName.hasNext());
			userFileName.close();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void write2Number(String numberString)
	{
		resetRepeatCheckArray();
		
		int[] userLotteryNumber=new int[6];
		boolean numberCheck=true;
		for(int i=0;i<6&&numberCheck;i++)
		{
			numberString=reternNumberStringWithoutZero(numberString);
			Scanner input=new Scanner(numberString);
			boolean allNumbersInArrayCheck=false;
			if(input.hasNext())
			{
				//抓取下注的第一個值
				String numberRemoveString=input.next();
				//檢查是否為 1~50 的數字
				for(int z=0;z<50&&allNumbersInArrayCheck==false;z++)
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
		for(int i=0;i<6&&numberCheck;i++)
		{
			if(fileNumberIndex==0&&notFromUser)
			{
				lotteryNumber[0][i]=userLotteryNumber[i];
				addLotteryNumberIndex=false;
			}
			else
			{
				lotteryNumber[lotteryNumberIndex][i]=userLotteryNumber[i];
				addLotteryNumberIndex=true;
			}
		}
		if(numberCheck&&addLotteryNumberIndex)
		{
			lotteryNumberIndex++;
		}
	}
	
	public void getWinLotteryNumber()
	{
		int[] winNumberCount=new int[3];
		for(int i=1;i<lotteryNumberIndex;i++)
		{
			int winNumberCheck=0;
			for(int j=0;j<6;j++)
			{
				for(int k=0;k<6;k++)
				{
					if(lotteryNumber[i][j]==lotteryNumber[0][k])
					{
						winNumberCheck++;
					}
				}
			}
			switch(winNumberCheck)
			{
				case 6:
					winNumberCount[0]++;
					break;
				case 5:
					winNumberCount[1]++;
					break;
				case 4:
					winNumberCount[2]++;
					break;
			}
		}
		double totalMoney=(lotteryNumberIndex-1)*50;
		System.out.print("The winning lottery number is");
		if(lotteryNumber[0][0]!=0)
		{
			for(int i=0;i<6;i++)
			{
				System.out.print(" "+lotteryNumber[0][i]);
			}
			System.out.println(".");
		}
		else
		{
			System.out.println(" none!");
		}
		if(winNumberCount[0]!=0)
		{
			System.out.printf("There are %d bets winning the first prize, each of them can get %1.2f dollars.\n",winNumberCount[0] ,((totalMoney*0.4)/winNumberCount[0]));
		}
		else
		{
			System.out.println("There are 0 bets winning the first prize, each of them can get 0.0 dollars.");			
		}
		if(winNumberCount[1]!=0)
		{
			System.out.printf("There are %d bets winning the second prize, each of them can get %1.2f dollars.\n",winNumberCount[1] ,((totalMoney*0.2)/winNumberCount[1]));
		}
		else
		{
			System.out.println("There are 0 bets winning the second prize, each of them can get 0.0 dollars.");
		}
		if(winNumberCount[2]!=0)
		{
			System.out.printf("There are %d bets winning the third prize, each of them can get %1.2f dollars.\n",winNumberCount[2] ,((totalMoney*0.1)/winNumberCount[2]));
		}
		else
		{
			System.out.println("There are 0 bets winning the third prize, each of them can get 0.0 dollars.");
		}
		if(winNumberCount[0]==winNumberCount[1]&&winNumberCount[1]==winNumberCount[2]&&winNumberCount[2]==0)
		{
			System.out.println("There are "+totalMoney+" dollars goes to philanthropies.");
		}
		else
		{
			if((winNumberCount[0]==winNumberCount[1]&&winNumberCount[1]==0)||(winNumberCount[1]==winNumberCount[2]&&winNumberCount[2]==0))
			{
				if(winNumberCount[0]==winNumberCount[1])
				{
					System.out.println("There are "+(totalMoney*0.9)+" dollars goes to philanthropies.");
				}
				else
				{
					System.out.println("There are "+(totalMoney*0.6)+" dollars goes to philanthropies.");
				}
			}
			else
			{
				if(winNumberCount[0]==0||winNumberCount[1]==0||winNumberCount[2]==0)
				{
					if(winNumberCount[0]==0)
					{
						System.out.println("There are "+(totalMoney*0.7)+" dollars goes to philanthropies.");
					}
					if(winNumberCount[1]==0)
					{
						System.out.println("There are "+(totalMoney*0.5)+" dollars goes to philanthropies.");
					}
					if(winNumberCount[2]==0)
					{
						System.out.println("There are "+(totalMoney*0.4)+" dollars goes to philanthropies.");
					}
				}
				else
				{
					System.out.println("There are "+(totalMoney*0.3)+" dollars goes to philanthropies.");
				}
			}
		}

	}
}