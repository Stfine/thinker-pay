package vc.thinker.pay.alipay;

import java.util.ArrayList;
import java.util.List;

import vc.thinker.pay.alipay.resps.NotifyBase;

public class AlipayNotifyPublisher
{
  // FIELDS
    protected List<AlipayNotifyListener> listNl;

  // CONSTRUCT
    public AlipayNotifyPublisher()
    {
        this.listNl = new ArrayList<AlipayNotifyListener>();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static AlipayNotifyPublisher instance = new AlipayNotifyPublisher();
    }


    public static AlipayNotifyPublisher getDefaultInstance()
    {
        return(
            Singleton.instance
        );
    }

  // EVENTQUEUE
    public void addListener(AlipayNotifyListener l)
    {
        this.listNl.add(l);

        return;
    }

    public void removeListener(AlipayNotifyListener l)
    {
        this.listNl.remove(l);

        return;
    }

    /**
     * @return true if one of the listener reports handled.
     */
    public boolean publish(NotifyBase notice)
    {
        for (AlipayNotifyListener l:this.listNl)
        {
            try
            {
                if (l.handle(notice))
                    return(true);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                continue;
            }
        }

        return(false);
    }
}
