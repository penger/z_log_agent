import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.travelsky.agent.Agent;
import com.travelsky.domain.FareFlightInfo;
import com.travelsky.domain.FlightInfo;
import com.travelsky.framework.util.DateUtil;
import com.travelsky.framework.util.FtpUtil;
import com.travelsky.service.logservice.ILogService;
import com.travelsky.service.logservice.LogService;


public class NewMain {

	public static void main(String[] args) throws Exception {

		monitorLogPath();
//		showInfo();
	}
	
	
	
	private static void showInfo() throws Exception{
		int a,b,c,d;
		a=0;
		b=0;
		c=0;
		d=0;
//		String path="d:/test/sclogs";
		String path="d:/test/logs";
		File file = new File(path);
		File[] listFiles = file.listFiles();
		Agent agent = new Agent();
		ILogService logService=new LogService();
		for(File tempfile: listFiles){
			BufferedReader br =new BufferedReader( new InputStreamReader(new FileInputStream(tempfile)));
			String templine;
			while((templine =br.readLine())!=null){
//				System.out.println(templine);
				a++;
				boolean checkAvailable = agent.checkAvailable(templine);
				if(checkAvailable){
					 templine =templine.substring(templine.indexOf("MSI")+25);
					b++;
					//验证时间
					List<FareFlightInfo> list = logService.getSortedListFromLog(templine);
					for(FareFlightInfo f:list){
						List<FlightInfo> flightInfoList = f.getFlightInfoList();
						for(FlightInfo ff:flightInfoList){
							boolean before = before(ff.getDepartingdatetime(),ff.getArrivalingdatetime());
							if(!before){
								d++;
								System.out.println("string to catch is :"+templine);
								break;
							}
						}
					}
				}else{
					System.out.println(" format error line is templine:"+templine);
					c++;
				}
			}
			br.close();
		}
		System.out.println("total line is a:"+a);
		System.out.println("formate success line is b:"+b);
		System.out.println("formate error line is c:"+c);
		System.out.println("error date (contain in b)  d:"+d);
		
	
	}
	

	private static boolean before(String departingdatetime,
			String arrivalingdatetime) {
		Timestamp timestamp2 = DateUtil.string2timestamp(arrivalingdatetime);
		 Timestamp timestamp = DateUtil.string2timestamp(departingdatetime);
		 boolean before = timestamp.before(timestamp2);
		 if(!before){
			 System.out.println(departingdatetime+"         "+arrivalingdatetime);
		 }
		 return before;
	}
	
	private static void monitorLogPath(){

		ScheduledExecutorService singleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		singleThreadScheduledExecutor.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				FtpUtil ftpUtil=null;
				ftpUtil=new FtpUtil("10.6.143.92", 21, "rduser", "123", "/opt/applog/WebSphere/msi", false);
				String[] filenames = ftpUtil.ListAllFiles();
				if(filenames.length==0){
					System.out.println(new Date()+"no file");
				}else{
					System.out.println(new Date()+"file list size is:"+filenames.length/2);
					for(String filename:filenames){
						if(filename.contains("log")){
							System.out.print(filename+"   &  ");
						}
					}
				}
			}
		}, 0, 20, TimeUnit.SECONDS);
	}
	

}
