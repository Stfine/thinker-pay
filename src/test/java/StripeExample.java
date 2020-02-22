import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccountCollection;
import com.stripe.model.Source;

public class StripeExample {
//
//    public static void main(String[] args) {
//        RequestOptions requestOptions = (new RequestOptionsBuilder())
//        		.setApiKey("sk_test_syrnz09nr5j2zjv0Yt3n2DgO").build();
//        Map<String, Object> chargeMap = new HashMap<String, Object>();
//        chargeMap.put("amount", 1000);
//        chargeMap.put("currency", "SGD");
//        chargeMap.put("source", "tok_1BVzaZI34s8l1NwD9tbv6zuR"); // obtained via Stripe.js
//        try {
//            Charge charge = Charge.create(chargeMap, requestOptions);
//            System.out.println(charge);
//        } catch (StripeException e) {
//            e.printStackTrace();
//        }
//    }

	//退款
//    public static void main(String[] args) {
//    	RequestOptions requestOptions = (new RequestOptionsBuilder())
//    			.setApiKey("sk_test_syrnz09nr5j2zjv0Yt3n2DgO").build();
//    	
//
//    	Map<String, Object> params = new HashMap<String, Object>();
//    	params.put("charge", "ch_1BVzb6I34s8l1NwD82VBnQw2");
//    	try {
//    		Refund refund = Refund.create(params,requestOptions);
//    		System.out.println(refund);
//    	} catch (StripeException e) {
//    		e.printStackTrace();
//    	}
//    }
//    public static void main(String[] args) {
//    	RequestOptions requestOptions = (new RequestOptionsBuilder())
//    			.setApiKey("sk_test_syrnz09nr5j2zjv0Yt3n2DgO").build();
//    	
//    	String token="tok_1BWKvbI34s8l1NwDGyOWvBmg";
//    	
//    	Map<String, Object> customerParams = new HashMap<String, Object>();
//    	customerParams.put("email", "86-18575541919");
//    	//customerParams.put("source", token);
//    	try {
//    		Customer customer= Customer.create(customerParams, requestOptions);
//    		System.out.println(customer);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//    }
    public static void main(String[] args) {
    	Stripe.apiKey = "sk_test_x7b0rkaYxRAN2Wf2eRJTcJaf";

    	Map<String, Object> cardParams = new HashMap<String, Object>();
//    	cardParams.put("limit", 3);
//    	cardParams.put("object", "cards");
    	cardParams.put("type", "card");
    	ExternalAccountCollection reuslt;
		try {
			
			Customer customer = Customer.retrieve("cus_CUUdEX8zIDOmm5");
			Source s=(Source)customer.getSources().retrieve("src_1C5VewHYAuR2j6QGLCU9tlVd");
			System.out.println(s.detach());
//			ExternalAccount sou=Customer.retrieve("cus_CUUdEX8zIDOmm5").getSources()
//			.retrieve("src_1C5VewHYAuR2j6QGLCU9tlVd");
//	    	System.out.println(sou.delete().toJson());
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
//    public static void main(String[] args) {
//    	RequestOptions requestOptions = (new RequestOptionsBuilder())
//    			.setApiKey("sk_test_syrnz09nr5j2zjv0Yt3n2DgO").
//    			setStripeVersion("2017-08-15").build();
//    	
//    	Map<String, Object> params = new HashMap<String, Object>();
//    	params.put("customer", "cus_Bu9LVVTQRDUiHF");
//    	try {
//    		EphemeralKey key=EphemeralKey.create(params, requestOptions);
//    		System.out.println(key.toJson());
//    	} catch (StripeException e) {
//    		e.printStackTrace();
//    	}
//    }
}