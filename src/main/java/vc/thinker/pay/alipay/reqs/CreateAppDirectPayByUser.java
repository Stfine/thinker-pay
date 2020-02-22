package vc.thinker.pay.alipay.reqs;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.google.common.collect.Lists;

import vc.thinker.pay.alipay.helper.RoyaltyList;
import vc.thinker.pay.alipay.resps.ResponseBase;

public class CreateAppDirectPayByUser extends CreateTradeBase
{
  // KEYS
    public static final String KEY_ROYALTY_TYPE        = "royalty_type";
    public static final String KEY_ROYALTY_TYPE_V      = "10";
    public static final String KEY_ROYALTY_PARAMETERS  = "royalty_parameters";

    private List<String> Parames_KEYS =  Lists.newArrayList();

  // CONSTRUCT
    public CreateAppDirectPayByUser(Properties prop)
    {
        super(prop);
        //this.setProperty("service", "create_direct_pay_by_user");
        Enumeration  e = this.conf.keys();
    	for (; e.hasMoreElements();) {
    		Parames_KEYS.add((String)e.nextElement());

    	    }

        return;
    }

  // SIGN
    @Override
    public CreateAppDirectPayByUser sign()
        throws UnsupportedEncodingException
    {

        this.sign(Parames_KEYS);
        return(this);
    }

  // TO_URL
    @Override
    public String toURL()
        throws UnsupportedEncodingException
    {
        String charset = this.getProperty(KEY_CHARSET);

        return(
            this.toSignedURL(Parames_KEYS, charset)
        );
    }

  // EXECUTE
    @Override
    public ResponseBase execute()
    {
        throw(new UnsupportedOperationException("This request should run on client."));
    }

  // PROPERTY
    public CreateAppDirectPayByUser setRoyalty(RoyaltyList l)
    {
        this.setProperty(KEY_ROYALTY_TYPE, KEY_ROYALTY_TYPE_V);
        this.setProperty(KEY_ROYALTY_PARAMETERS, l.getRoyaltyParameters());

        return(this);
    }
}
