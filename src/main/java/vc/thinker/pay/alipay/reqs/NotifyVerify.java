package vc.thinker.pay.alipay.reqs;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.http.client.fluent.Request;

import vc.thinker.pay.alipay.AlipayException;
import vc.thinker.pay.alipay.resps.NotifyVerifyResponse;

public class NotifyVerify extends WebRequestBase
{
  // CONSTANTS
    public static final List<String> KEYS_PARAM_NAME = Arrays.asList(
        "service",
        "notify_id",
        "partner"
    );

  // CONSTRUCT
    public NotifyVerify(Properties prop)
    {
        super(prop);
        this.setProperty("service", "notify_verify");

        return;
    }

  // BUILD
    @Override
    public NotifyVerify build()
    {
        return(this);
    }

  // SIGN
    @Override
    public NotifyVerify sign()
    {
        return(this);
    }

  // TO_URL
    @Override
    public String toURL()
    {
        try
        {
            return(
                this.toUnsignedURL(KEYS_PARAM_NAME, "utf-8")
            );
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            return(null);
        }
    }

    @Override
    public NotifyVerifyResponse execute()
        throws AlipayException
    {
        try
        {
            String params = Request.Get(this.toURL())
                .execute()
                .returnContent()
                .asString();

            return(
                new NotifyVerifyResponse(params, null)
            );
        }
        catch (Exception ex)
        {
            throw(new AlipayException(ex));
        }
    }
}
