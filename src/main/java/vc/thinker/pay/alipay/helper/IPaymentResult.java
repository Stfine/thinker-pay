package vc.thinker.pay.alipay.helper;

import java.util.Date;

public interface IPaymentResult //extends IPaymentItem
{
    public abstract String getSn();
    public abstract String getAccount();
    public abstract String getName();
    public abstract Double getAmount();
    public abstract String getMemo();

    public abstract boolean getSuccess();
    public abstract String getTradeNo();
    public abstract Date getDate();
}
