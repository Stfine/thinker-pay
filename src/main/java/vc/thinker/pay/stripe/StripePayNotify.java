package vc.thinker.pay.stripe;

import vc.thinker.pay.PayNotify;

/**
 * 该支付没有Notify
 * @author james
 *
 */
public class StripePayNotify extends PayNotify{
	@Override
	public boolean isPaySuccess() {
		return false;
	}
}
