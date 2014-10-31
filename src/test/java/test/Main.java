package test;

import com.travelsky.pub.util.scheduling.applications.SchedulerCommander;
import com.travelsky.pub.util.xml.JdomParser;
import com.travelsky.pub.util.xml.XMLParser;

public class Main {
	
	private static JdomParser xmlParser=null;
	private static String xmlFile="SchedulerCommands.xml";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String fileName="SchedulerCommands.xml";
//		xmlParser = new JdomParser();
//		XMLParser parser = new XMLParser();
		
//		xmlParser.main(args);
//		xmlParser.bulid("SchedulerCommands.xml");
//		InputStream inputStream = 
//		        JdomParser.class.getClassLoader().getResourceAsStream(fileName);
//		 URL url = JdomParser.class.getClassLoader().getResource(fileName);
//		 System.out.println(url);
//		System.out.println(inputStream);
//		Object root = xmlParser.getRootElement();
//	    Iterator i = xmlParser.getChildren(root, "command").iterator();
//	    while(i.hasNext()){
//	    	i.next();
//	    }
	    
		SchedulerCommander.getInstance().execute();
//		SchedulerCommander instance = SchedulerCommander.getInstance();
//		System.out.println(instance);
//		instance.main(args);
//		SchedulerCommander.getInstance().cancel();
	}
	
//	private void load()
//	  {
//		this.xmlParser = new JdomParser();
//	    this.xmlParser.bulid(this.xmlFile);
//	    Object root = this.xmlParser.getRootElement();
//	    Iterator i = this.xmlParser.getChildren(root, "command").iterator();
////	    SchedulerCommandImpl command = null;
//	    String taskName = null; String groupName = null;
//
//	    while (i.hasNext()) {
//	      root = i.next();
//
//	      taskName = this.xmlParser.getAttributeValue(root, "name");
//
//	      groupName = this.xmlParser.getAttributeValue(root, "scheduler");
//	      if (groupName == null) {
//	        groupName = SchedulerHelper.DEFAULST_SCHEDULER;
//	      }
//	      command = (SchedulerCommandImpl)this.commandHelper.createSchedulerCommand(taskName);
//	      if (command != null) {
//	        Scheduler scheduler = this.schedulerHelper.getScheduler(groupName);
//
//	        scheduler.increaseCount();
//	        command.setScheduler(scheduler);
//
//	        command.init(root, this.xmlParser);
//	      }
//	    }
//	    this.xmlParser.release();
//	  }


}
