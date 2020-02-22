package vc.thinker.pay.alipay;

import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.Properties;

import vc.thinker.pay.alipay.reqs.BatchTransNotify;
import vc.thinker.pay.alipay.reqs.CreateDirectPayByUser;
import vc.thinker.pay.alipay.reqs.CreateDirectPayByUserBank;
import vc.thinker.pay.alipay.reqs.CreatePartnerTradeByBuyer;
import vc.thinker.pay.alipay.reqs.NotifyVerify;
import vc.thinker.pay.alipay.reqs.RefundFastpayByPlatformPwd;
import vc.thinker.pay.alipay.reqs.SendGoodsConfirmByPlatform;
import vc.thinker.pay.alipay.reqs.TradeCreateByBuyer;
import vc.thinker.pay.alipay.reqs.WapAuthAndExecute;
import vc.thinker.pay.alipay.reqs.WapTradeCreateDirect;

/** 创建支付宝业务的工厂类
 */
public class AlipayFactory
{
  // CONSTANT
    private static final String RESOURCE_ALIPAY_PROPERTIES = "/alipay.properties";

  // CONFIG
    protected Properties conf;

    public Properties getConf()
    {
        return(this.conf);
    }

  // CONSTRUCT
    /** Construct a new instance with blank config.
     */
    public AlipayFactory()
    {
        this.conf = new Properties();

        return;
    }

    /** Construct a new instance using a resource indicated by <code>resource</code>.
     */
    public AlipayFactory(String resource)
        throws MissingResourceException
    {
        this();

        try
        {
            this.conf.load(
                new InputStreamReader(
                    AlipayFactory.class.getResourceAsStream(resource),
                    "utf-8"
            ));

            return;
        }
        catch (Exception ex)
        {
            MissingResourceException mrex = new MissingResourceException(
                "Failed to load conf resource.",
                AlipayFactory.class.getName(),
                resource
            );
            mrex.initCause(ex);

            throw(mrex);
        }
    }

    /** Construct a new instance with a prepared config prop.
     */
    public AlipayFactory(Properties aConf)
    {
        super();

        this.conf = aConf;

        return;
    }

    /**
     * Default constructor which looking for a .properties resource in order below:
     * 1. System.getProperty("com.github.cuter44.alipay.alipay_properties")
     * 2. AlipayFactory.class.getResource("/alipay.properties")
     * Properties MUST stored in utf-8.
     */
    //public AlipayFactory()
        //throws MissingResourceException
    //{
        //String res = "";

        //try
        //{
            //res = System.getProperty("com.github.cuter44.alipay.alipay_properties");
            //res = res!=null ? res : RESOURCE_ALIPAY_PROPERTIES;

            //this.conf = new Properties();
            //this.conf.load(
                //new InputStreamReader(
                    //AlipayFactory.class.getResourceAsStream(res),
                    //"utf-8"
            //));
        //}
        //catch (Exception ex)
        //{
            //MissingResourceException mrex = new MissingResourceException(
                //"Failed to load conf resource.",
                //AlipayFactory.class.getName(),
                //res
            //);
            //mrex.initCause(ex);

            //throw(mrex);
        //}
    //}

  // SINGLETON
    private static class Singleton
    {
        public static final AlipayFactory instance = new AlipayFactory("/alipay.properties");
    }

    /** return default instance which load config from <code>/alipay.properties</code>.
     * If you are binding multi-instance of AlipayFactory in your application, DO NOT use this method.
     */
    public static AlipayFactory getDefaultInstance()
    {
        return(Singleton.instance);
    }

    /** @deprecated Please use <code>getDefaultInstance()</code> instead.
     * This method now forwarded to <code>getDefaultInstance()</code>
     */
    public static AlipayFactory getInstance()
    {
        return(
            getDefaultInstance()
        );
    }

  // FACTORY
    public TradeCreateByBuyer newTradeCreateByBuyer()
    {
        return(
            new TradeCreateByBuyer(
                new Properties(this.conf)
        ));
    }
    public TradeCreateByBuyer newTradeCreateByBuyer(Properties p)
    {
        return(
            new TradeCreateByBuyer(
                buildConf(p, this.conf)
        ));
    }

    public SendGoodsConfirmByPlatform newSendGoodsConfirmByPlatform()
    {
        return(
            new SendGoodsConfirmByPlatform(
                new Properties(this.conf)
        ));
    }
    public SendGoodsConfirmByPlatform newSendGoodsConfirmByPlatform(Properties p)
    {
        return(
            new SendGoodsConfirmByPlatform(
                buildConf(p, this.conf)
        ));
    }

    public WapTradeCreateDirect newWapTradeCreateDirect()
    {
        return(
            new WapTradeCreateDirect(
                new Properties(this.conf)
        ));
    }
    public WapTradeCreateDirect newWapTradeCreateDirect(Properties p)
    {
        return(
            new WapTradeCreateDirect(
                buildConf(p, this.conf)
        ));
    }

    public WapAuthAndExecute newWapAuthAndExecute()
    {
        return(
            new WapAuthAndExecute(
                new Properties(this.conf)
        ));
    }

    public WapAuthAndExecute newWapAuthAndExecute(Properties p)
    {
        return(
            new WapAuthAndExecute(
                buildConf(p, this.conf)
        ));
    }

    public CreateDirectPayByUser newCreateDirectPayByUser()
    {
        return(
            new CreateDirectPayByUser(
                new Properties(this.conf)
        ));
    }

    public CreateDirectPayByUser newCreateDirectPayByUser(Properties p)
    {
        return(
            new CreateDirectPayByUser(
                buildConf(p, this.conf)
        ));
    }

    public CreateDirectPayByUserBank newCreateDirectPayByUserBank()
    {
        return(
            new CreateDirectPayByUserBank(
                new Properties(this.conf)
        ));
    }

    public CreateDirectPayByUserBank newCreateDirectPayByUserBank(Properties p)
    {
        return(
            new CreateDirectPayByUserBank(
                buildConf(p, this.conf)
        ));
    }

    public NotifyVerify newNotifyVerify()
    {
        return(
            new NotifyVerify(
                new Properties(this.conf)
        ));
    }

    public NotifyVerify newNotifyVerify(Properties p)
    {
        return(
            new NotifyVerify(
                buildConf(p, this.conf)
        ));
    }

    /** Warning: this is not fully tested.
     */
    public BatchTransNotify newBatchTransNotify()
    {
        return(
            new BatchTransNotify(
                new Properties(this.conf)
        ));
    }

    /** Warning: this is not fully tested.
     */
    public BatchTransNotify newBatchTransNotify(Properties p)
    {
        return(
            new BatchTransNotify(
                buildConf(p, this.conf)
        ));
    }

    public RefundFastpayByPlatformPwd newRefundFastpayByPlatformPwd()
    {
        return(
            new RefundFastpayByPlatformPwd(
                new Properties(this.conf)
        ));
    }

    public RefundFastpayByPlatformPwd newRefundFastpayByPlatformPwd(Properties p)
    {
        return(
            new RefundFastpayByPlatformPwd(
                buildConf(p, this.conf)
        ));
    }

    public CreatePartnerTradeByBuyer newCreatePartnerTradeByBuyer()
    {
        return(
            new CreatePartnerTradeByBuyer(
                new Properties(this.conf)
        ));
    }

    public CreatePartnerTradeByBuyer newCreatePartnerTradeByBuyer(Properties p)
    {
        return(
            new CreatePartnerTradeByBuyer(
                buildConf(p, this.conf)
        ));
    }


  // MISC
    protected static Properties buildConf(Properties prop, Properties defaults)
    {
        Properties ret = new Properties(defaults);
        Iterator<String> iter = prop.stringPropertyNames().iterator();
        while (iter.hasNext())
        {
            String key = iter.next();
            ret.setProperty(key, prop.getProperty(key));
        }

        return(ret);
    }
}
