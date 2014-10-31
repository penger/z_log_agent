package com.travelsky.rmi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;

import com.travelsky.agent.Agent;
import com.travelsky.domain.FareFlightInfo;
import com.travelsky.domain.FlightInfo;
import com.travelsky.service.logservice.LogService;
import com.travelsky.service.rmi.MsiLogRemote;

public class TureLocalFile2SortedListThenPushThem {

	public static void main(String[] args) throws Exception {
		
		String filepath="d:/test/a/a.txt";
		File file=new File(filepath);
		if(!file.exists()){
			return ;
		}
		Set set = new HashSet();
		LogService logService = new LogService();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String tempLogString;
		int i=0;
		Agent agent = new Agent();
		while((tempLogString=reader.readLine())!=null){
			if(!agent.checkAvailable(tempLogString)){
				System.out.println("formate error"+tempLogString);
				continue;
			}
			tempLogString = tempLogString.substring(tempLogString.indexOf("MSI")+25);
			List<FareFlightInfo> list = logService.getSortedListFromLog(tempLogString);
			
			for(FareFlightInfo f:list){
				List<FlightInfo> infoList = f.getFlightInfoList();
				for(FlightInfo info:infoList){
						Date depart = DateUtils.parseDate(info.getDepartingdatetime(), "yyyy-MM-dd HH:mm:ss");
						Date arrival = DateUtils.parseDate(info.getArrivalingdatetime(), "yyyy-MM-dd HH:mm:ss");
						if(arrival.before(depart)){
							System.out.println(i+++"         当前读取的字符串为:"+tempLogString);
							System.out.println(info.getDepartingdatetime()+"      "+info.getArrivalingdatetime());
						}
				}
			}
			set.add(list);
		}		
		reader.close();
		System.out.println(set.size());
		MsiLogRemote remote=null;
		remote= (MsiLogRemote) Naming.lookup("rmi://localhost:8889/rmiServer");
		remote.AnalysisSet(set);
	}

}
