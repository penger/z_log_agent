import java.util.List;

import com.travelsky.domain.FareFlightInfo;
import com.travelsky.service.logservice.LogService;


public class S {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LogService logService = new LogService();
		List<FareFlightInfo> list = logService.getSortedListFromLog("HU^HU7876^null^CKG^HAK^null^2014-01-25 09:50:00^2014-01-25 11:50:00^2B:-^738^0^0^0^HAK969&689^null^null^Q^WEB03^AD=HU^HU7230^null^CKG^HAK^null^2014-01-25 11:20:00^2014-01-25 13:20:00^2B:-^738^0^0^1^HAK969&689^null^null^Q^WEB03^AD=HU^HU7052^null^CKG^HAK^null^2014-01-25 12:20:00^2014-01-25 14:20:00^2B:-^738^0^0^1^HAK969&689^null^null^Q^WEB03^AD=HU^HU7377^null^CKG^HAK^null^2014-01-25 14:10:00^2014-01-25 16:10:00^2B:-^738^0^0^0^HAK969&1145^null^null^Y^WEB03^AD=HU^HU7098^null^CKG^HAK^null^2014-01-25 15:50:00^2014-01-25 17:50:00^2B:-^738^0^0^0^HAK969&689^null^null^Q^WEB03^AD=HU^HU7044^null^CKG^HAK^null^2014-01-25 21:50:00^2014-01-26 00:05:00^2B:-^738^0^0^0^HAK969&572^null^null^X^WEB03^AD");
		for(FareFlightInfo f:list){
			System.out.println(f);
		}
	}

}
