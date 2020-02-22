package vc.thinker.pay.alipay.resps;

import java.util.Properties;

public class WapAuthAndExecuteResponse extends ResponseBase
{
  // CONSTANTS
    public static final String KEY_RESULT = "result";

  // CONSTRUCT
    public WapAuthAndExecuteResponse(ResponseBase resp)
    {
        this(resp.respString, resp.respProp);
    }

    public WapAuthAndExecuteResponse(String respString, Properties respProperties)
    {
        super(respString, respProperties);
    }

  // MISC
    public boolean isSuccess()
    {
        return(
            "success".equals(
                this.respProp.getProperty(KEY_RESULT)
        ));
    }
}
