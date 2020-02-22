package vc.thinker.pay.alipay.reqs;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import vc.thinker.pay.alipay.AlipayException;
import vc.thinker.pay.alipay.resps.ResponseBase;

public class TradeCreateByBuyer extends CreateTradeBase
{
  // KEYS
    public static final List<String> KEYS_PARAM_NAME = Arrays.asList(
        "_input_charset",
        "anti_phishing_key",
        "body",
        "buyer_account_name",
        "buyer_email",
        "buyer_id",
        "discount",
        "it_b_pay",
        "logistics_fee",
        "logistics_fee_1",
        "logistics_fee_2",
        "logistics_payment",
        "logistics_payment_1",
        "logistics_payment_2",
        "logistics_type",
        "logistics_type_1",
        "logistics_type_2",
        "notify_url",
        "out_trade_no",
        "partner",
        "payment_type",
        "price",
        "quantity",
        "receive_address",
        "receive_mobile",
        "receive_name",
        "receive_phone",
        "receive_zip",
        "return_url",
        "seller_account_name",
        "seller_email",
        "seller_id",
        "service",
        "show_url",
        "subject",
        "t_b_rec_post",
        "t_s_send_1",
        "t_s_send_2",
        "token",
        "total_fee"
    );

  // CONSTRUCT
    public TradeCreateByBuyer(Properties prop)
    {
        super(prop);
        this.setProperty("service",         "trade_create_by_buyer");
        this.setProperty("payment_type",    "1");

        return;
    }

  // SIGN
    @Override
    public TradeCreateByBuyer sign()
        throws UnsupportedEncodingException, IllegalStateException
    {
        this.sign(KEYS_PARAM_NAME);
        return(this);
    }

  // TO_URL
    @Override
    public String toURL()
        throws UnsupportedEncodingException
    {
        String charset = this.conf.getProperty(KEY_CHARSET);

        return(
            this.toSignedURL(KEYS_PARAM_NAME, charset)
        );
    }

  // EXECUTE
    @Override
    public ResponseBase execute()
        throws AlipayException
    {
        throw(new UnsupportedOperationException("This request should run on client."));
    }
}
