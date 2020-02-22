package vc.thinker.pay.alipay.reqs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.http.client.fluent.Request;

import vc.thinker.pay.alipay.AlipayException;
import vc.thinker.pay.alipay.resps.SendGoodsConfirmByPlatformResponse;
import vc.thinker.pay.alipay.util.XMLParser;

public class SendGoodsConfirmByPlatform extends WebRequestBase
{
    public static final String KEY_ERROR = "error";

    public static final List<String> KEYS_PARAM_NAME = Arrays.asList(
        "_input_charset",
        "create_transport_type",
        "invoice_no",
        "logistics_name",
        "partner",
        "seller_ip",
        "service",
        "trade_no",
        "transport_type"
    );

  // CONSTRUCT
    public SendGoodsConfirmByPlatform(Properties prop)
    {
        super(prop);
        this.setProperty("service", "send_goods_confirm_by_platform");

        return;
    }

  // SIGN
    @Override
    public SendGoodsConfirmByPlatform sign()
        throws UnsupportedEncodingException
    {
        this.sign(KEYS_PARAM_NAME);
        return(this);
    }

  // TO_URL
    @Override
    public String toURL()
        throws UnsupportedEncodingException
    {
        String charset = this.getProperty(KEY_CHARSET);

        return(
            this.toSignedURL(KEYS_PARAM_NAME, charset)
        );
    }

  // EXECUTE
    @Override
    public SendGoodsConfirmByPlatformResponse execute()
    {
        try
        {
            Properties prop = new Properties();

            String content = Request.Get(this.toURL())
                .execute()
                .returnContent()
                .asString();

            prop.putAll(XMLParser.parseXML(content));

            if (prop.getProperty(KEY_ERROR)!=null)
                throw(
                    new AlipayException(
                        prop.getProperty(KEY_ERROR)
                            .toUpperCase()
                            .replace(' ','_')
                ));

            return(new SendGoodsConfirmByPlatformResponse(content, prop));
        }
        catch (IOException ex)
        {
            throw(new AlipayException(ex));
        }
    }

}